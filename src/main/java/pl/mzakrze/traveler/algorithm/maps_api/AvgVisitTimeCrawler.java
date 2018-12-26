package pl.mzakrze.traveler.algorithm.maps_api;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import pl.mzakrze.traveler.algorithm.maps_api.model.AvgVisitTime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.mzakrze.traveler.algorithm.Configuration.AVG_TIME_FROM_SCALER;
import static pl.mzakrze.traveler.algorithm.Configuration.AVG_TIME_TO_SCALER;

@Component
public class AvgVisitTimeCrawler extends BaseApiFacade {

    private Map<String, AvgVisitTime> cache = new HashMap<>();

    public Map<String, AvgVisitTime> fetch(List<String> placesIds) {
        HashMap<String, AvgVisitTime> res = new HashMap<>();

        String jsonInput = "[" + placesIds.stream()
                .filter(placeId -> !cache.containsKey(placeId))
                .map(placeId -> "\"" + placeId + "\"")
                .collect(Collectors.joining(",")) + "]";
        String googleApiKey = googleApiKeyProvider.getGoogleApiKey();

        placesIds.stream()
                .filter(placeId -> cache.containsKey(placeId))
                .forEach(placeId -> res.put(placeId, cache.get(placeId)));

        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "crawler.sh", googleApiKey, jsonInput);
        try {
            Process proc = processBuilder.start();
            InputStream inputStream = proc.getInputStream();

            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            Gson gson = new Gson();

            ArrayList<LinkedTreeMap> timeSpentList = gson.fromJson(result, ArrayList.class);

            for (LinkedTreeMap<String, String> placeTimeSpent : timeSpentList) {
                String placeId = placeTimeSpent.get("place_id");
                Integer timeSpent = ((Double) (Object) placeTimeSpent.get("time_spent")).intValue(); // wartość jest Doublem, ale tym mapy wskazuje, że to String (sic!)

                AvgVisitTime avgVisitTime = new AvgVisitTime();
                avgVisitTime.setFromMinutes(timeSpent * AVG_TIME_FROM_SCALER);
                avgVisitTime.setToMinutes(timeSpent * AVG_TIME_TO_SCALER);

                res.put(placeId, avgVisitTime);
                cache.put(placeId, avgVisitTime);
            }

            return res;

        } catch (IOException e) {
            e.printStackTrace();

            return new HashMap<>();
        }
    }
}
