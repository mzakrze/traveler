package pl.mzakrze.traveler.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mzakrze.traveler.algorithm.FindRouteAlgorithm;

@RestController
@RequestMapping("/api/maps")
public class FindRouteController {

    @Autowired
    private FindRouteAlgorithm findRouteAlgorithm;

    @PostMapping("/find_route")
    public FindRouteResponse simpleMapRequest(@RequestBody FindRouteRequest req) {
        return findRouteAlgorithm.handleFindRouteRequest(req);
    }
}
