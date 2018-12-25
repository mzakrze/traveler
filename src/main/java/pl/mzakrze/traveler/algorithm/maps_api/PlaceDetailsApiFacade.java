package pl.mzakrze.traveler.algorithm.maps_api;

import org.springframework.stereotype.Component;

/**
 * @See https://developers.google.com/places/web-service/details
 */
@Component
public class PlaceDetailsApiFacade extends BaseApiFacade {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    // settings
    public static final String FIELDS = "opening_hours,icon,rating,types,website";

    public String fetch(String placeId) {
        String reqUrl = buildRequestUrl(placeId);

        return execute(reqUrl);
    }

    private String buildRequestUrl(String placeId) {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("placeid=" + placeId);
        urlBuilder.append("&key=" + googleApiKeyProvider.getGoogleApiKey());
        urlBuilder.append("$fields=" + FIELDS);

        return urlBuilder.toString();
    }


}
