package pl.mzakrze.traveler.algorithm.maps_api;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;

import java.io.IOException;
import java.util.List;

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

    public String fetch(Location start, Location end, List<Location> waypoints) {

        String reqUrl = buildRequestUrl(start, end, waypoints);

        if(this.cache == null) {
            this.cache = execute(reqUrl);
        }
        return this.cache;
    }

    private String buildRequestUrl(Location start, Location end, List<Location> waypoints) {
        final String pipSignEncoded = "%7C";
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("origin=" + start.lat + "," + start.lng);
        urlBuilder.append("&destination=" + end.lat + "," + end.lng);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&mode=" + MODE);

        urlBuilder.append("&waypoints=");
        for (int i = 0; i < waypoints.size(); i++) {
            Location p = waypoints.get(i);
            urlBuilder.append(p.lat + "," + p.lng);

            if(i != waypoints.size() - 1) {
                urlBuilder.append(pipSignEncoded);
            }
        }

        return urlBuilder.toString();
    }
}
