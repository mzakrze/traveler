package pl.mzakrze.traveler.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;
import pl.mzakrze.traveler.google_maps_integration.GoogleMapsIntegration;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/maps")
public class FindRouteController {

    @Autowired
    private GoogleApiKeyProvider googleApiKeyProvider;

    @Autowired
    private GoogleMapsIntegration googleMapsIntegration;

    @PostMapping("/find_route")
    public String simpleMapRequest(@RequestBody MapRequest req) {
        System.out.println(req.toString());
        return googleMapsIntegration.mockRequest();
    }

    static class MapRequest {
        String[] placesOfInterest;
        Location endLocation;
        Location startLocation;

        public MapRequest() {
        }

        public String[] getPlacesOfInterest() {
            return placesOfInterest;
        }

        public void setPlacesOfInterest(String[] placesOfInterest) {
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

        static class Location {
            BigDecimal lat;
            BigDecimal lng;

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

}
