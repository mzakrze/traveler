package pl.mzakrze.traveler.algorithm.maps_api.model;

import lombok.Data;

import java.util.List;

@Data
public class DistanceMatrixApiResponse {
    List<String> destination_addresses;
    List<String> origin_addresses;
    List<Row> rows;

    @Data
    public static class Row {
        List<Element> elements;
    }

    @Data
    public static class Element {
        TextValue distance;
        TextValue duration;
        String status;
    }

    @Data
    public static class TextValue {
        String text;
        Integer value;
    }
}
