package pl.mzakrze.traveler.algorithm;

import com.google.common.collect.ImmutableMap;
import pl.mzakrze.traveler.algorithm.maps_api.model.AvgVisitTime;
import pl.mzakrze.traveler.algorithm.maps_api.model.NearbySeachPlacesApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Zahardokowane wartości: typ miejsca -> przeciętny czas ile osoba spędza tam czasu(w minutach)
 * Typy miejsc: wybrane bardziej istotne z: https://developers.google.com/places/web-service/supported_types
 */
public class PlaceVisitTimeHardMap {

    Map<String, Integer> PLACE_TYPE_AVG_VISIT_TIME_MAP = ImmutableMap.<String, Integer>builder()
        .put("amusement_park", 20)
        .put("aquarium", 20)
        .put("art_gallery", 60)
        .put("bar", 60)
        .put("beauty_salon", 45)
        .put("book_store", 15)
        .put("bowling_alley", 45)
        .put("cafe", 15)
        .put("casino", 60)
        .put("church", 45)
        .put("city_hall", 60)
        .put("clothing_store", 30)
        .put("gym", 60)
        .put("hair_care", 30)
        .put("laundry", 20)
        .put("meal_takeaway", 5)
        .put("movie_rental", 5)
        .put("movie_theater", 120)
        .put("museum", 60)
        .put("night_club", 120)
        .put("park", 30)
        .put("pharmacy", 10)
        .put("store", 15)
        .put("supermarket", 15)
        .put("travel_agency", 15)
        .put("zoo", 60)
        .build();


    public Map<String, AvgVisitTime> fillEmptyVisitTimes(Map<String, AvgVisitTime> avgVisitTimeMap, NearbySeachPlacesApiResponse places) {

        for (NearbySeachPlacesApiResponse.Result result : places.results) {

            if(avgVisitTimeMap.containsKey(result.getPlace_id())) {
                continue;
            }

            double avgVisitTimeMinutes = result.getTypes().stream().mapToDouble(type -> PLACE_TYPE_AVG_VISIT_TIME_MAP.get(type)).average().getAsDouble();

            AvgVisitTime avgVisitTime = new AvgVisitTime();
            avgVisitTime.setFromMinutes(avgVisitTimeMinutes * Configuration.AVG_TIME_FROM_SCALER);
            avgVisitTime.setToMinutes(avgVisitTimeMinutes * Configuration.AVG_TIME_TO_SCALER);

            avgVisitTimeMap.put(result.getPlace_id(), avgVisitTime);

        }

        return avgVisitTimeMap;
    }
}
