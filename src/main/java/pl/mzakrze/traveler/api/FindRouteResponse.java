package pl.mzakrze.traveler.api;

import lombok.Data;
import pl.mzakrze.traveler.algorithm.Location;
import pl.mzakrze.traveler.algorithm.ga.FoundPlacesResult;
import pl.mzakrze.traveler.algorithm.maps_api.model.PlaceDetailsApiResponse;

import java.util.List;
import java.util.Map;

@Data
public class FindRouteResponse {
    public List<String> directions;
    public String places;
    public Map<String, PlaceDetailsApiResponse> placeId2DetailsMap;
    public String error;
    public FoundPlacesResult selectedPlaces;
    Location endLocation;
    Location startLocation;

}
