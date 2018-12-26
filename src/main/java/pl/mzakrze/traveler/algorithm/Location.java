package pl.mzakrze.traveler.algorithm;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Location {
    public BigDecimal lat;
    public BigDecimal lng;
}
