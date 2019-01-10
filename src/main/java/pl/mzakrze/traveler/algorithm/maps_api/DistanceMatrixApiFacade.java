package pl.mzakrze.traveler.algorithm.maps_api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.algorithm.maps_api.model.DistanceMatrixApiResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
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

        List<AsyncTaskData> asyncTasks = new ArrayList<>();

        for (String origin : placesIds) {
            List<String> destinations = placesIds.stream().filter(e -> !e.equals(origin)).collect(Collectors.toList());
            asyncTasks.add(new AsyncTaskData(origin, destinations));
        }


        List<CompletableFuture<AsyncTaskResult>> futures = asyncTasks.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(s -> executefetchDistancesAsyncTask(s.originId, s.placesId)))
                .collect(Collectors.toList());

        ConcurrentMap<DistanceMatrix.PlacesPair, Integer> concurrentMap = new ConcurrentHashMap<>();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            try {
                for (CompletableFuture<AsyncTaskResult> future : futures) {
                    for (PlacesPairDuration duration : future.get().durations) {
                        concurrentMap.put(duration.placesPair, duration.duration);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).join();

        DistanceMatrix result = new DistanceMatrix();
        result.matrix = new HashMap<>();
        for (DistanceMatrix.PlacesPair placesPair : concurrentMap.keySet()) {
            result.matrix.put(placesPair, concurrentMap.get(placesPair));
        }

        return result;
    }

    class AsyncTaskData {
        String originId;
        List<String> placesId;

        public AsyncTaskData(String originId, List<String> placesId) {
            this.originId = originId;
            this.placesId = placesId;
        }
    }

    class AsyncTaskResult {
        List<PlacesPairDuration> durations;
    }

    class PlacesPairDuration {
        DistanceMatrix.PlacesPair placesPair;
        Integer duration;
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

    public AsyncTaskResult executefetchDistancesAsyncTask(String originId, List<String> destinationsIds) {
        List<PlacesPairDuration> durationsResult = new ArrayList<>();
        Gson gson = new Gson();
        String response = execute(buildRequestUrl(originId, destinationsIds));
        DistanceMatrixApiResponse apiResponse = gson.fromJson(response, DistanceMatrixApiResponse.class);

        // szukamy dla 1 źródła i wielu pkt docelowych => liczba wierszy = 1
        final int FROM_ORIGIN_ROW_INDEX = 0;
        List<DistanceMatrixApiResponse.Element> elements = apiResponse.getRows().get(FROM_ORIGIN_ROW_INDEX).getElements();
        for (int rowIndex = 0; rowIndex < elements.size(); rowIndex++) {
            DistanceMatrixApiResponse.Element element = elements.get(rowIndex);
            String destination = destinationsIds.get(rowIndex);

            PlacesPairDuration placesPairDuration = new PlacesPairDuration();
            placesPairDuration.duration = element.getDuration().getValue();
            placesPairDuration.placesPair = new DistanceMatrix.PlacesPair(originId, destination);
            durationsResult.add(placesPairDuration);
        }
        AsyncTaskResult asyncTaskResult = new AsyncTaskResult();
        asyncTaskResult.durations = durationsResult;

        return asyncTaskResult;
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
