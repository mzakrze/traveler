package pl.mzakrze.traveler.algorithm.maps_api.model;

import lombok.Data;

@Data
public class AvgVisitTime {
    Boolean isInfoAvailable;
    Double fromMinutes;
    Double toMinutes;
}
