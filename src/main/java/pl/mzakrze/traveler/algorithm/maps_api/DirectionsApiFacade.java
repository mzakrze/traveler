package pl.mzakrze.traveler.algorithm.maps_api;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.algorithm.ga.FoundPlacesResult;
import pl.mzakrze.traveler.api.FindRouteRequest;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @See https://developers.google.com/maps/documentation/directions/intro
 */
@Component
public class DirectionsApiFacade extends BaseApiFacade {

    public final static String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    // settings
    public final static String MODE = "walking";

    @Autowired
    private GoogleApiKeyProvider googleApiKeyProvider;

    private String buildRequestUrl(String startPlaceId, String endPlaceId) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("origin=place_id:" + startPlaceId);
        urlBuilder.append("&destination=place_id:" + endPlaceId);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&mode=" + MODE);

        return urlBuilder.toString();
    }

    private String buildRequestUrl(String startPlaceId, Location end) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("origin=place_id:" + startPlaceId);
        urlBuilder.append("&destination=" + end.lat + "," + end.lng);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&mode=" + MODE);

        return urlBuilder.toString();
    }

    private String buildRequestUrl(Location start, String endPlaceId) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("origin=" + start.lat + "," + start.lng);
        urlBuilder.append("&destination=place_id:" + endPlaceId);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&mode=" + MODE);

        return urlBuilder.toString();
    }

    public List<String> fetch(FindRouteRequest findRouteRequest, FoundPlacesResult foundPlacesResult) {
        Preconditions.checkArgument(foundPlacesResult.getPlacesToVisitInOrder().isEmpty() == false);

        List<String> results = new ArrayList<>();
        List<FoundPlacesResult.Place> placesToVisitInOrder = foundPlacesResult.getPlacesToVisitInOrder();


        // 1. from start location to first place
        FoundPlacesResult.Place firstPlace = placesToVisitInOrder.get(0);
        results.add(execute(buildRequestUrl(findRouteRequest.getStartLocation(), firstPlace.id)));


        // 2. through all places (with staying there for a moment)
        // FIXME - czas uwzględnić
        for (int i = 0; i < placesToVisitInOrder.size() - 1; i++) {

            FoundPlacesResult.Place placeFrom = placesToVisitInOrder.get(i);
            FoundPlacesResult.Place placeTo = placesToVisitInOrder.get(i + 1);

            results.add(execute(buildRequestUrl(placeFrom.id, placeTo.id)));
        }

        // 3. from last place to end location
        FoundPlacesResult.Place lastPlace = placesToVisitInOrder.get(placesToVisitInOrder.size() - 1);

        results.add(execute(buildRequestUrl(lastPlace.id, findRouteRequest.getEndLocation())));

        return results;
    }


}
