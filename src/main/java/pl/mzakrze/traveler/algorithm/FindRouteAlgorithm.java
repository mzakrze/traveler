package pl.mzakrze.traveler.algorithm;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.ga.FoundPlacesResult;
import pl.mzakrze.traveler.algorithm.ga.GeneticAlgorithm;
import pl.mzakrze.traveler.algorithm.maps_api.*;
import pl.mzakrze.traveler.algorithm.maps_api.model.AvgVisitTime;
import pl.mzakrze.traveler.algorithm.maps_api.model.NearbySeachPlacesApiResponse;
import pl.mzakrze.traveler.algorithm.maps_api.model.PlaceDetailsApiResponse;
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

        if(placesIds.size() == 0) {
            result.error = "No places found, please unrestrict your searches";
            return result;
        }

        // 2. Fetch distances between those places from api
        DistanceMatrixApiFacade.DistanceMatrix distanceMatrix = null;
        if(placesIds.size() == 1){
            distanceMatrix = distanceMatrixApiFacade.fetch(placesIds);
        }

        Map<String, Integer> fromStartToPlacesDistanceMap = distanceMatrixApiFacade.fetch(req.getStartLocation(), placesIds);
        Map<String, Integer> fromPlacesToEndDistanceMap = distanceMatrixApiFacade.fetch(req.getEndLocation(), placesIds);

        // 3. Fetch places details from api
        Map<String, PlaceDetailsApiResponse> placeId2DetailsMap = placesIds.stream()
                .collect(Collectors.toMap(
                        placeId -> placeId,
                        placeId -> gson.fromJson(placeDetailsApiFacade.fetch(placeId), PlaceDetailsApiResponse.class)));

        // 4. Fetch average visit time via web crawler + fill those not found
        Map<String, AvgVisitTime> visitTimeMap = avgVisitTimeCrawler.fetch(placesIds);
        visitTimeMap = new PlaceVisitTimeHardMap().fillEmptyVisitTimes(visitTimeMap, fetchedPlaces);

        // 5. Find "best" route
        FoundPlacesResult foundPlacesResult = new GeneticAlgorithm().solve(fetchedPlaces, placeId2DetailsMap, visitTimeMap, fromStartToPlacesDistanceMap, fromPlacesToEndDistanceMap);

        // 7. Fetch directions from start to end with resolved points along
        result.directions = directionsApiFacade.fetch(req.getStartLocation(), req.getEndLocation(), foundPlacesResult);

        return result;
    }
}
