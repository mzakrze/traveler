package pl.mzakrze.traveler.algorithm.ga;

import java.util.List;

public class FoundPlacesResult {

    private List<Place> placesToVisitInOrder;

    public List<Place> getPlacesToVisitInOrder() {
        return placesToVisitInOrder;
    }

    public void setPlacesToVisitInOrder(List<Place> placesToVisitInOrder) {
        this.placesToVisitInOrder = placesToVisitInOrder;
    }

    public static class Place {
        public String id;
        public Integer order; // redundancja(bo kolejność jest już w liście), ale dla kompletności
        public Integer proposedTime;

        public Place(String id, Integer order, Integer proposedTime) {
            this.id = id;
            this.order = order;
            this.proposedTime = proposedTime;
        }
    }
}
