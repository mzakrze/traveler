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
public class DirectionsApiFacade {

    // settings
    public final static String MODE = "walking";


    public final static String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    @Autowired
    private GoogleApiKeyProvider googleApiKeyProvider;

    public String fetch(Location start, Location end, List<Location> waypoints) {

        String reqUrl = buildRequestUrl(start, end, waypoints);

        return execute(reqUrl);
    }

    private String buildRequestUrl(Location start, Location end, List<Location> waypoints) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("origin=" + start.lat + "," + start.lng);
        urlBuilder.append("&destination=" + end.lat + "," + end.lng);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("&mode=" + MODE);

        urlBuilder.append("&waypoints=");
        for (int i = 0; i < waypoints.size(); i++) {
            Location p = waypoints.get(i);
            urlBuilder.append(p.lat + "," + p.lng);

            if(i - 1 != waypoints.size()) {
                urlBuilder.append("|");
            }
        }

        return urlBuilder.toString();
    }

    private String execute(String reqUrl) {
        final HttpClient client = new HttpClient();
        final HttpMethod method = new GetMethod(reqUrl);
        try
        {
            final int status = client.executeMethod(method);
            if ( status != HttpStatus.SC_OK )
            {
                throw new RuntimeException ( "Method failed with error " + status + " " + method.getStatusLine () );
            }
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            method.releaseConnection ();
        }
        return null;
    }


}
