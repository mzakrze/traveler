package pl.mzakrze.traveler.algorithm.maps_api;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.maps_api.model.PlaceDetailsApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @See https://developers.google.com/places/web-service/details
 */
@Component
public class PlaceDetailsApiFacade extends BaseApiFacade {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    // settings
    public static final String FIELDS = "opening_hours,icon,rating,types,website";

    private String buildRequestUrl(String placeId) {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("placeid=" + placeId);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&fields=" + FIELDS);

        return urlBuilder.toString();
    }

    public Map<String, PlaceDetailsApiResponse> fetch(List<String> placesIds) {
        Map<String, PlaceDetailsApiResponse> result = new HashMap<>();

        Gson gson = new Gson();

        List<CompletableFuture<AsyncApiDetailsTaskData>> futures = placesIds.stream()
            .map(placeId -> CompletableFuture.completedFuture(placeId)
                    .thenApplyAsync(pId -> {
                        String apiResponse = execute(buildRequestUrl(pId));
                        return new AsyncApiDetailsTaskData(pId, apiResponse);
                    }))
            .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            try {
                for (CompletableFuture<AsyncApiDetailsTaskData> future : futures) {
                    AsyncApiDetailsTaskData data = future.get();
                    result.put(data.placeId, gson.fromJson(data.apiResponse, PlaceDetailsApiResponse.class));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).join();

        return result;
    }

    class AsyncApiDetailsTaskData {
        String placeId;
        String apiResponse;

        public AsyncApiDetailsTaskData(String placeId, String apiResponse) {
            this.placeId = placeId;
            this.apiResponse = apiResponse;
        }
    }
}
