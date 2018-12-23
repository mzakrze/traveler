package pl.mzakrze.traveler.algorithm;

import java.math.BigDecimal;

public class Location {
    public BigDecimal lat;
    public BigDecimal lng;

    public Location(BigDecimal lat, BigDecimal lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
