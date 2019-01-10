package pl.mzakrze.traveler.algorithm.maps_api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.algorithm.maps_api.model.DistanceMatrixApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @See https://developers.google.com/maps/documentation/distance-matrix/intro
 */
@Component
public class DistanceMatrixApiFacade extends BaseApiFacade {

    public final static String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    public final Integer MAX_ORIGINS_ALLOWED = 5;
    public final Integer MAX_PLACES_ALLOWED = 21;
    private String cache = null;

    public DistanceMatrix fetch(List<String> placesIds) {
        Preconditions.checkArgument(placesIds.size() <= MAX_PLACES_ALLOWED, "Too much places");

        DistanceMatrix result = new DistanceMatrix();
        for (String origin : placesIds) {
            List<String> destinations = placesIds.stream().filter(e -> !e.equals(origin)).collect(Collectors.toList());
            appendToDistanceMatrix(origin, destinations, result);
        }

        return result;
    }

    private String buildRequestUrl(String origin, List<String> destinations) {
        final String pipeSignEncoded = "%7C";
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);

        String placesArg = destinations.stream().map(e -> "place_id:" + e).collect(Collectors.joining(pipeSignEncoded));
        urlBuilder.append("key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&origins=" + "place_id:" + origin);
        urlBuilder.append("&destinations=" + placesArg);

        return urlBuilder.toString();
    }

    private String buildRequestUrl(Location origin, List<String> destinations) {
        final String pipeSignEncoded = "%7C";
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);

        String placesArg = destinations.stream().map(e -> "place_id:" + e).collect(Collectors.joining(pipeSignEncoded));
        urlBuilder.append("key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&origins=" + origin.lat + "," + origin.lng);
        urlBuilder.append("&destinations=" + placesArg);

        return urlBuilder.toString();
    }

    public void appendToDistanceMatrix(String originId, List<String> destinationsIds, DistanceMatrix distanceMatrix) {
        Gson gson = new Gson();
        String response = execute(buildRequestUrl(originId, destinationsIds));
        DistanceMatrixApiResponse result = gson.fromJson(response, DistanceMatrixApiResponse.class);

        // szukamy dla 1 źródła i wielu pkt docelowych => liczba wierszy = 1
        final int FROM_ORIGIN_ROW_INDEX = 0;
        List<DistanceMatrixApiResponse.Element> elements = result.getRows().get(FROM_ORIGIN_ROW_INDEX).getElements();
        for (int rowIndex = 0; rowIndex < elements.size(); rowIndex++) {
            DistanceMatrixApiResponse.Element element = elements.get(rowIndex);
            String destination = destinationsIds.get(rowIndex);

            distanceMatrix.matrix.put(new DistanceMatrix.PlacesPair(originId, destination), element.getDuration().getValue());
        }
    }

    public Map<String, Integer> fetch(Location location, List<String> placesIds) {
        Map<String, Integer> result = new HashMap<>();

        String response = execute(buildRequestUrl(location, placesIds));

        DistanceMatrixApiResponse apiResponse = new Gson().fromJson(response, DistanceMatrixApiResponse.class);

        final int FROM_ORIGIN_ROW_INDEX = 0;
        List<DistanceMatrixApiResponse.Element> elements = apiResponse.getRows().get(FROM_ORIGIN_ROW_INDEX).getElements();
        for (int rowIndex = 0; rowIndex < elements.size(); rowIndex++) {
            DistanceMatrixApiResponse.Element element = elements.get(rowIndex);
            String destination = placesIds.get(rowIndex);

            result.put(destination, element.getDuration().getValue());
        }

        return result;
    }

    public static class DistanceMatrix {
        Map<PlacesPair, Integer> matrix = new HashMap<>();

        public static class PlacesPair {
            String placeIdFrom;
            String placeIdTo;

            public PlacesPair(String placeIdFrom, String placeIdTo) {
                this.placeIdFrom = placeIdFrom;
                this.placeIdTo = placeIdTo;
            }
        }
    }

}
