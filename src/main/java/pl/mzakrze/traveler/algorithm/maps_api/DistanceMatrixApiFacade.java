package pl.mzakrze.traveler.algorithm.maps_api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
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

    public void appendToDistanceMatrix(String originId, List<String> destinationsIds, DistanceMatrix distanceMatrix) {
        Gson gson = new Gson();
        String response = execute(buildRequestUrl(originId, destinationsIds));
        DistanceMatrixApiResponse result = gson.fromJson(response, DistanceMatrixApiResponse.class);

        for (int i = 0; i < result.getRows().get(0).getElements().size(); i++) {
            DistanceMatrixApiResponse.Element element = result.getRows().get(0).getElements().get(i);

            distanceMatrix.matrix.put(new DistanceMatrix.PlacesPair(originId, destinationsIds.get(i)), element.getDuration().getValue());
        }
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
