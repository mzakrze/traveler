package pl.mzakrze.traveler.algorithm.maps_api.model;

import java.math.BigDecimal;

public class LatLng {
    public BigDecimal lat;
    public BigDecimal lng;

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
}
