package pl.mzakrze.traveler.algorithm.maps_api.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PlaceDetailsApiResponse {
    String html_attributions;
    String status;
    Result result;

    @Data
    public static class Result {
        String icon;
        BigDecimal rating;
        List<String> types; // TODO - zrobić z tego enuma
        String website;
        OpeningHours opening_hours;
    }

    @Data
    public static class OpeningHours {
        Boolean open_now;
        List<OpeninhHoursPeriod> periods;
        List<String> weekday_text;

    }

    @Data
    public static class OpeninhHoursPeriod {
        DateTime close;
        DateTime open;
    }

    @Data
    public static class DateTime {
        Integer day;
        String time;
    }
}
