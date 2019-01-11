package pl.mzakrze.traveler.api;

import lombok.Data;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.algorithm.ga.FoundPlacesResult;

import java.util.List;

@Data
public class FindRouteResponse {
    public List<String> directions;
    public String places;
    public String error;
    public FoundPlacesResult selectedPlaces;
    Location endLocation;
    Location startLocation;

}
