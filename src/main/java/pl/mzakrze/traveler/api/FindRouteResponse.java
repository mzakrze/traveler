package pl.mzakrze.traveler.api;

public class FindRouteResponse {
    public String directions;
    public String places;

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }
}
