package pl.mzakrze.traveler.algorithm.maps_api.model;

import lombok.Data;

import java.util.List;

@Data
public class NearbySeachPlacesApiResponse {
    public List<String> html_attributions;
    public String next_page_token;
    public String status;
    public List<Result> results;

    @Data
    public static class Result {
        Geometry geometry;
        String icon;
        String id;
        String name;
        String place_id;
        String reference;
        String scope;
        List<String> types; // TODO - enuma z tego zrobić
        String vicinity;
    }

    @Data
    public static class Geometry {
        LatLng location;
        Viewport viewport;
    }

    @Data
    public static class Viewport {
        LatLng northeast;
        LatLng southwest;
    }

    @Data
    public static class Photo {
        Integer height;
        Integer width;
        List<String> html_attributions;
        String photo_reference;
    }
}