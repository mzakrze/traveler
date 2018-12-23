package pl.mzakrze.traveler.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.maps_api.DirectionsApiFacade;
import pl.mzakrze.traveler.algorithm.maps_api.NearbySearchPlacesApiFacade;
import pl.mzakrze.traveler.api.FindRouteRequest;
import pl.mzakrze.traveler.api.FindRouteResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class FindRouteAlgorithm {

    @Autowired
    DirectionsApiFacade directionsApiFacade;

    @Autowired
    NearbySearchPlacesApiFacade nearbySearchPlacesApiFacade;

    public FindRouteResponse handleFindRouteRequest(FindRouteRequest req) {
        FindRouteResponse result = new FindRouteResponse();

        // 1. Fetch places from api
        String fetchedPlaces = nearbySearchPlacesApiFacade.fetch(req);
        result.places = fetchedPlaces;

        // 2. Fetch distances between those places from api
        // TODO

        // 3. Fetch places details from api
        // TODO

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
