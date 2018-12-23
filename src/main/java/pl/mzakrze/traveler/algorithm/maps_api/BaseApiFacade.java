package pl.mzakrze.traveler.algorithm.maps_api;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;

import java.io.IOException;

public class BaseApiFacade {

    @Autowired
    protected GoogleApiKeyProvider googleApiKeyProvider;

    protected String execute(String reqUrl) {
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
