package pl.mzakrze.traveler.api;

import java.math.BigDecimal;
import java.util.List;

public class FindRouteRequest {
    List<String> placesOfInterest;
    Location endLocation;
    Location startLocation;

    public FindRouteRequest() {
    }

    public List<String> getPlacesOfInterest() {
        return placesOfInterest;
    }

    public void setPlacesOfInterest(List<String> placesOfInterest) {
        this.placesOfInterest = placesOfInterest;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Places of interest: ");
        for (String s : placesOfInterest) {
            sb.append(s + ",");
        }
        sb.append("start location: " + startLocation);
        sb.append(",");
        sb.append("end location: " + endLocation);
        return sb.toString();
    }

    public static class Location {
        public BigDecimal lat;
        public BigDecimal lng;

        public Location() {
        }

        public BigDecimal getLat() {
            return lat;
        }

        public void setLat(BigDecimal lat) {
            this.lat = lat;
        }

        public BigDecimal getLng() {
            return lng;
        }

        public void setLng(BigDecimal lng) {
            this.lng = lng;
        }

        public String toString() {
            return "{lat:" + lat + ", lng: " + lng + "}";
        }
    }
}
