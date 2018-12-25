package pl.mzakrze.traveler.algorithm;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.maps_api.DirectionsApiFacade;
import pl.mzakrze.traveler.algorithm.maps_api.DistanceMatrixApiFacade;
import pl.mzakrze.traveler.algorithm.maps_api.NearbySearchPlacesApiFacade;
import pl.mzakrze.traveler.algorithm.maps_api.PlaceDetailsApiFacade;
import pl.mzakrze.traveler.algorithm.maps_api.model.NearbySeachPlacesApiResponse;
import pl.mzakrze.traveler.algorithm.maps_api.model.PlaceDetailsApiResponse;
import pl.mzakrze.traveler.api.FindRouteRequest;
import pl.mzakrze.traveler.api.FindRouteResponse;

import java.math.BigDecimal;
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

    public FindRouteResponse handleFindRouteRequest(FindRouteRequest req) {
        Gson gson = new Gson();

        FindRouteResponse result = new FindRouteResponse();

        // 1. Fetch places from api
        result.places = nearbySearchPlacesApiFacade.fetch(req);
        NearbySeachPlacesApiResponse fetchedPlaces = gson.fromJson(result.places, NearbySeachPlacesApiResponse.class);

        List<String> placesIds = fetchedPlaces.results.stream().map(e -> e.getPlace_id()).collect(Collectors.toList());

        // 2. Fetch distances between those places from api
        DistanceMatrixApiFacade.DistanceMatrix distanceMatrix = distanceMatrixApiFacade.fetch(placesIds);

        // 3. Fetch places details from api
        Map<String, PlaceDetailsApiResponse> placeId2DetailsMap = placesIds.stream()
                .collect(Collectors.toMap(
                        placeId -> placeId,
                        placeId -> gson.fromJson(placeDetailsApiFacade.fetch(placeId), PlaceDetailsApiResponse.class)));

        // 4. Create graph representation
        // TODO

        // 5. Solve graph(find cheapest route) via genetic algorithm
        // TODO

        // 6. Fetch directions from start to end with resolved points along
        // TODO - all here is mocked
        Location start = new Location(new BigDecimal("51.49393920967706"), new BigDecimal("-0.12016177181976673"));
        Location end = new Location(new BigDecimal("51.500778755511405"), new BigDecimal("-0.05278468136566517"));
        Location l1 = new Location(new BigDecimal("51.5056940451569"), new BigDecimal("-0.11217951779144644"));
        Location l2 = new Location(new BigDecimal("51.49132067438587"), new BigDecimal("-0.08746027951019644"));
        Location l3 = new Location(new BigDecimal("51.50948700405906"), new BigDecimal("-0.06797671322601674"));
        List<Location> pointsAlong = Arrays.asList(l1, l2, l3);

        result.directions = directionsApiFacade.fetch(start, end, pointsAlong);

        return result;
    }
}
