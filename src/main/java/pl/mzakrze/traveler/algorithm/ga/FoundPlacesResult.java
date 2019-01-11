package pl.mzakrze.traveler.algorithm.ga;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public class FoundPlacesResult {

    public List<Place> placesToVisitInOrder;

    public List<Place> getPlacesToVisitInOrder() {
        return placesToVisitInOrder;
    }

    public void setPlacesToVisitInOrder(List<Place> placesToVisitInOrder) {
        this.placesToVisitInOrder = placesToVisitInOrder;
    }

    @Data
    @EqualsAndHashCode
    public static class Place {
        public String id;
        @EqualsAndHashCode.Exclude public Integer order; // redundancja(bo kolejność jest już w liście), ale dla kompletności
        @EqualsAndHashCode.Exclude public Integer proposedTime;

        public Place(String id, Integer order, Integer proposedTime) {
            this.id = id;
            this.order = order;
            this.proposedTime = proposedTime;
        }
    }
}
