package pl.mzakrze.traveler.google_maps_integration;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;

import java.io.IOException;

@Component
public class GoogleMapsIntegration {

    @Autowired
    private GoogleApiKeyProvider googleApiKeyProvider;

    public String mockRequest(){
        String mockRequestUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry";
        String reqUrl = mockRequestUrl + "&key=" + googleApiKeyProvider.getGoogleApiKey();

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
