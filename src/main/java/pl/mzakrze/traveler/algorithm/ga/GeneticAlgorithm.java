package pl.mzakrze.traveler.algorithm.ga;

import pl.mzakrze.traveler.algorithm.maps_api.model.AvgVisitTime;
import pl.mzakrze.traveler.algorithm.maps_api.model.NearbySeachPlacesApiResponse;
import pl.mzakrze.traveler.algorithm.maps_api.model.PlaceDetailsApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneticAlgorithm {
    public FoundPlacesResult solve(NearbySeachPlacesApiResponse fetchedPlaces, Map<String, PlaceDetailsApiResponse> placeId2DetailsMap, Map<String, AvgVisitTime> visitTimeMap, Map<String, Integer> fromStartToPlacesDistanceMap, Map<String, Integer> fromPlacesToEndDistanceMap) {
        FoundPlacesResult result = new FoundPlacesResult();
        List<FoundPlacesResult.Place> places = new ArrayList<>();

        // FIXME
        String placeId1 = fetchedPlaces.results.get(2).getPlace_id();
        int proposedTime1 = 60;
        String placeId2 = fetchedPlaces.results.get(3).getPlace_id();
        int proposedTime2 = 60;

        places.add(new FoundPlacesResult.Place(placeId1, 1, proposedTime1));
        places.add(new FoundPlacesResult.Place(placeId2, 2, proposedTime2));

        return result;
    }
}
