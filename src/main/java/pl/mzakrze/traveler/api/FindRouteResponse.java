package pl.mzakrze.traveler.api;

import java.util.List;

public class FindRouteResponse {
    public List<String> directions;
    public String places;
    public String error;

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
