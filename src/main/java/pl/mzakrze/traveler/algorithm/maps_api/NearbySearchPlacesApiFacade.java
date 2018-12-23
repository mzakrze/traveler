package pl.mzakrze.traveler.algorithm.maps_api;

import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.api.FindRouteRequest;

import java.math.BigDecimal;

/**
 * @See https://developers.google.com/places/web-service/search#PlaceSearchRequests
 */
@Component
public class NearbySearchPlacesApiFacade extends BaseApiFacade {

    public final static String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    private String cache = null;

    public String fetch(FindRouteRequest req) {

        String reqUrl = buildRequestUrl(req);

        if(this.cache == null) {
            this.cache = execute(reqUrl);
        }
        return this.cache;
    }

    private String buildRequestUrl(FindRouteRequest req) {
        // FIXME - nie będzie działać jeśli start location blisko end location
        // Heuristic#1: search only for places "close" to / between start and end locations
        BigDecimal lat = req.getStartLocation().lat.add(req.getEndLocation().lat).divide(new BigDecimal("2"));
        BigDecimal lng = req.getStartLocation().lng.add(req.getEndLocation().lng).divide(new BigDecimal("2"));

        BigDecimal radius = new BigDecimal("5000"); // [m]

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("location=" + lat + "," + lng);
        urlBuilder.append("&radius=" + radius);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        if(req.getPlacesOfInterest().isEmpty() == false) {
            urlBuilder.append("&keyword=");
            for (int i = 0; i < req.getPlacesOfInterest().size(); i++) {
                String s = req.getPlacesOfInterest().get(i);
                urlBuilder.append(s);
                if(i != req.getPlacesOfInterest().size() - 1) {
                    urlBuilder.append(",");
                }
            }
        }
        return urlBuilder.toString();
    }

}
