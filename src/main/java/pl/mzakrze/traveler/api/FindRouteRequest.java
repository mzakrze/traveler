package pl.mzakrze.traveler.api;

import lombok.Data;
import pl.mzakrze.traveler.algorithm.Location;

import java.util.List;

@Data
public class FindRouteRequest {
    List<String> placesOfInterest;
    Location endLocation;
    Location startLocation;
    String placesKeywords;
    Integer tripDuration;
    HourMinute tripStart;

    @Data
    public static class HourMinute {
        Integer hour;
        Integer minute;
    }
}
