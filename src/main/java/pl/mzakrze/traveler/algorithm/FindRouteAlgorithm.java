package pl.mzakrze.traveler.algorithm;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.maps_api.*;
import pl.mzakrze.traveler.algorithm.maps_api.model.AvgVisitTime;
import pl.mzakrze.traveler.algorithm.maps_api.model.NearbySeachPlacesApiResponse;
import pl.mzakrze.traveler.api.FindRouteRequest;
import pl.mzakrze.traveler.api.FindRouteResponse;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FindRouteAlgorithm {

    @Autowired
    DirectionsApiFacade directionsApiFacade;

    @Autowired
    NearbySearchPlacesApiFacade nearbySearchPlacesApiFacade;

    @Autowired
    PlaceDetailsApiFacade placeDetailsApiFacade;

    @Autowired
    DistanceMatrixApiFacade distanceMatrixApiFacade;

    @Autowired
    AvgVisitTimeCrawler avgVisitTimeCrawler;

    public FindRouteResponse handleFindRouteRequest(FindRouteRequest req) {
        Gson gson = new Gson();

        FindRouteResponse result = new FindRouteResponse();

        // 1. Fetch places from api
        result.places = nearbySearchPlacesApiFacade.fetch(req);
        NearbySeachPlacesApiResponse fetchedPlaces = gson.fromJson(result.places, NearbySeachPlacesApiResponse.class);

        List<String> placesIds = fetchedPlaces.results.stream().map(e -> e.getPlace_id()).collect(Collectors.toList());

        // 2. Fetch distances between those places from api
//        DistanceMatrixApiFacade.DistanceMatrix distanceMatrix = distanceMatrixApiFacade.fetch(placesIds);

        // 3. Fetch places details from api
//        Map<String, PlaceDetailsApiResponse> placeId2DetailsMap = placesIds.stream()
//                .collect(Collectors.toMap(
//                        placeId -> placeId,
//                        placeId -> gson.fromJson(placeDetailsApiFacade.fetch(placeId), PlaceDetailsApiResponse.class)));

        Map<String, AvgVisitTime> visitTimeMap = avgVisitTimeCrawler.fetch(placesIds);

        // 4. Create graph representation
        // TODO

        // 5. Solve graph(find cheapest route) via genetic algorithm
        // TODO -  mocked
        List<String> selectedWaypoints = placesIds.subList(2, 6);

        // 6. Fetch directions from start to end with resolved points along
        result.directions = directionsApiFacade.fetch(req.getStartLocation(), req.getEndLocation(), selectedWaypoints);

        return result;
    }
}
