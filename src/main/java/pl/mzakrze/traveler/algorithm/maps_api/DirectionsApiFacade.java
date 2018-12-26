package pl.mzakrze.traveler.algorithm.maps_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;

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

    String cache = null; // for dev purposes only (limit quotas)

    @Autowired
    private GoogleApiKeyProvider googleApiKeyProvider;  

    public String fetch(Location start, Location end, List<String> waypoints) {

        String reqUrl = buildRequestUrl(start, end, waypoints);

        if(this.cache == null) {
            this.cache = execute(reqUrl);
        }
        return this.cache;
    }

    private String buildRequestUrl(Location start, Location end, List<String> waypoints) {
        final String pipeSignEncoded = "%7C";
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("origin=" + start.lat + "," + start.lng);
        urlBuilder.append("&destination=" + end.lat + "," + end.lng);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&mode=" + MODE);

        urlBuilder.append("&waypoints=");
        urlBuilder.append(waypoints.stream()
                .map(e -> "place_id:" + e)
                .collect(Collectors.joining(pipeSignEncoded)));

        return urlBuilder.toString();
    }
}
