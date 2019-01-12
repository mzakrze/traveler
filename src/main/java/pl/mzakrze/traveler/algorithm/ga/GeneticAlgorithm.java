package pl.mzakrze.traveler.algorithm.ga;

import pl.mzakrze.traveler.algorithm.maps_api.DistanceMatrixApiFacade;
import pl.mzakrze.traveler.algorithm.maps_api.model.AvgVisitTime;
import pl.mzakrze.traveler.algorithm.maps_api.model.NearbySeachPlacesApiResponse;
import pl.mzakrze.traveler.algorithm.maps_api.model.PlaceDetailsApiResponse;
import pl.mzakrze.traveler.api.FindRouteRequest;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GeneticAlgorithm {

    final FindRouteRequest request;
    final NearbySeachPlacesApiResponse fetchedPlaces;
    final Map<String, PlaceDetailsApiResponse> placeId2DetailsMap;
    final Map<String, AvgVisitTime> visitTimeMap;
    final Map<String, Integer> fromStartToPlacesDistanceMap;
    final Map<String, Integer> fromPlacesToEndDistanceMap;
    final DistanceMatrixApiFacade.DistanceMatrix distanceMatrix;

    final Random randomSeed;

    public GeneticAlgorithm(FindRouteRequest request,
                            NearbySeachPlacesApiResponse fetchedPlaces,
                            Map<String, PlaceDetailsApiResponse> placeId2DetailsMap,
                            Map<String, AvgVisitTime> visitTimeMap,
                            DistanceMatrixApiFacade.DistanceMatrix distanceMatrix,
                            Map<String, Integer> fromStartToPlacesDistanceMap,
                            Map<String, Integer> fromPlacesToEndDistanceMap) {
        this.request = request;
        this.fetchedPlaces = fetchedPlaces;
        this.placeId2DetailsMap = placeId2DetailsMap;
        this.visitTimeMap = visitTimeMap;
        this.distanceMatrix = distanceMatrix;
        this.fromStartToPlacesDistanceMap = fromStartToPlacesDistanceMap;
        this.fromPlacesToEndDistanceMap = fromPlacesToEndDistanceMap;

        this.randomSeed = new Random();
    }


    public FoundPlacesResult solve() {

        if(fetchedPlaces.results.size() == 1) {
            NearbySeachPlacesApiResponse.Result singlePlace = fetchedPlaces.results.get(0);
            FoundPlacesResult result = new FoundPlacesResult();
            result.placesToVisitInOrder = new ArrayList<>();
            AvgVisitTime avgVisitTime = visitTimeMap.get(singlePlace.getPlace_id());
            double time = (avgVisitTime.getFromMinutes() + avgVisitTime.getToMinutes()) / 2.0;
            result.placesToVisitInOrder.add(new FoundPlacesResult.Place(singlePlace.getPlace_id(), 0, (int) time));
            return result;
        }

        List<FoundPlacesResult> generationZero = generateRandom(Constants.POPULATION_SIZE * 2);

        List<FoundPlacesResult> currentGeneration = generationZero;
        for(int population = 0; population < Constants.POPULATIONS_NO; population++) {

            currentGeneration = selection(currentGeneration);

            List<FoundPlacesResult> newGeneration = crossover(currentGeneration);

            newGeneration = mutate(newGeneration);

            currentGeneration.addAll(newGeneration);
        }

        return currentGeneration.get(0);
    }

    private List<FoundPlacesResult> generateRandom(int populationSize) {
        List<FoundPlacesResult> result = new ArrayList<>();

        for(int i = 0; i < populationSize; i++) {
            int placesNo = randomSeed.nextInt(fetchedPlaces.results.size() - 1) + 1;
            Collections.shuffle(fetchedPlaces.results);
            FoundPlacesResult res = new FoundPlacesResult();
            res.placesToVisitInOrder = new ArrayList<>();
            int counter = 0;
            for (NearbySeachPlacesApiResponse.Result place : fetchedPlaces.results.subList(0, placesNo)) {
                AvgVisitTime avgVisitTime = visitTimeMap.get(place.getPlace_id());
                double time = (avgVisitTime.getFromMinutes() + avgVisitTime.getToMinutes()) / 2.0;
                res.placesToVisitInOrder.add(new FoundPlacesResult.Place(place.getPlace_id(), counter++, (int) time));
            }
            result.add(res);
            assertIsOk(res.placesToVisitInOrder);
        }

        return result;
    }

    void assertIsOk(List<FoundPlacesResult.Place> places) {

        if(places.size()  == 0) {
            throw new IllegalStateException();
        }

        HashSet<Object> someSet = new HashSet<>();
        someSet.addAll(places);

        if(places.size() != someSet.size()) {
            throw new IllegalStateException();
        }
    }

    private Long fitnessFunction(FoundPlacesResult result) {

        long fitness = 0;

        // 1. nagrody
        // 1.1 za liczbę miejsc
        int placesNo = result.placesToVisitInOrder.size();
        fitness += 10 * placesNo * placesNo;
        // 1.2 za "jakość" miejsc
        fitness += result.placesToVisitInOrder.stream()
                .map(place -> place.id)
                .map(placeId2DetailsMap::get)
                .filter(placeDetails -> placeDetails.getResult().getRating() != null)
                .mapToInt(placeDetails -> placeDetails.getResult().getRating().intValue())
                .average().orElseGet(() -> 0.0) * Constants.PLACES_QUALITY_COEF;
        // 1.3 za "różnorodność" miejsc
        long distinctPlacesTypesVisited = result.placesToVisitInOrder.stream()
                .map(place -> place.id)
                .map(placeId2DetailsMap::get)
                .map(placeDetails -> placeDetails.getResult().getTypes())
                .flatMap(List::stream)
                .distinct()
                .count();
        fitness += distinctPlacesTypesVisited * Constants.DISTINCT_PLACES_TYPES_COEF;
        // 1.4 za spełnienie "keyword'a" z request'a
        boolean hardcodedKeywordFulfilled = result.placesToVisitInOrder.stream()
                .map(place -> place.id)
                .map(placeId2DetailsMap::get)
                .anyMatch(placeDetails -> placeDetails.getHtml_attributions().contains(request.getPlacesKeywords()));

        // 2. kary
        // 2.1 za czas
        int totalTime = 0;
        for (int index = 0; index < result.placesToVisitInOrder.size(); index++) {
            FoundPlacesResult.Place destinationPlace = result.placesToVisitInOrder.get(index);
            if(index == 0) {
                totalTime += fromStartToPlacesDistanceMap.get(destinationPlace.id);
            } else {
                FoundPlacesResult.Place sourcePlace = result.placesToVisitInOrder.get(index - 1);
                totalTime += distanceMatrix.matrix.get(new DistanceMatrixApiFacade.DistanceMatrix.PlacesPair(destinationPlace.id, sourcePlace.id));
            }

            totalTime += visitTimeMap.get(destinationPlace.id).getToMinutes();
        }
        totalTime += fromPlacesToEndDistanceMap.get(result.placesToVisitInOrder.get(result.placesToVisitInOrder.size() - 1).id);

        // czasem zdarza się przekręcić int'a
        fitness -= Math.abs(Math.abs(5 * Math.abs(request.getTripDuration() - totalTime) * Math.abs(request.getTripDuration() - totalTime)));

        return fitness;
    }

    private List<FoundPlacesResult> mutate(List<FoundPlacesResult> generation) {

        for (FoundPlacesResult singleIndividual : generation) {

            List<FoundPlacesResult.Place> places = singleIndividual.placesToVisitInOrder;
            assertIsOk(places);
            if (randomSeed.nextBoolean()) {
                // swap 2 places
                if (places.size() >= 2) {
                    int indexFirstToSwap = randomSeed.nextInt(places.size() - 1);
                    Collections.swap(singleIndividual.placesToVisitInOrder, indexFirstToSwap, indexFirstToSwap + 1);
                    assertIsOk(places);
                }
            }

            if(randomSeed.nextBoolean()) {
                // remove place
                if(singleIndividual.placesToVisitInOrder.size() > 2) {
                    int toRemoveIndex = randomSeed.nextInt(singleIndividual.placesToVisitInOrder.size());
                    singleIndividual.placesToVisitInOrder.remove(toRemoveIndex);
                }
                assertIsOk(singleIndividual.placesToVisitInOrder);
            }
        }

        return generation;
    }

    private List<FoundPlacesResult> selection(List<FoundPlacesResult> generation) {
        List<FoundPlacesResult> afterSelection = generation.stream()
                .sorted((r1, r2) -> -1 * Long.compare(fitnessFunction(r1), fitnessFunction(r2)))
                .collect(Collectors.toList())
                .subList(0, Constants.POPULATION_SIZE);
        Collections.shuffle(afterSelection);
        return afterSelection;
    }

    private List<FoundPlacesResult> crossover(List<FoundPlacesResult> generation) {

        List<FoundPlacesResult> children = new ArrayList<>();
        Collections.shuffle(generation);

        for(int childNo = 0; childNo < Constants.POPULATION_SIZE; childNo++) {
            List<FoundPlacesResult.Place> parent1 = generation.get(randomSeed.nextInt(generation.size())).placesToVisitInOrder;
            List<FoundPlacesResult.Place> parent2 = generation.get(randomSeed.nextInt(generation.size())).placesToVisitInOrder;

            int parent1Rnd1 = randomSeed.nextInt(parent1.size());
            int parent1Rnd2 = randomSeed.nextInt(parent1.size());
            int parent2Rnd1 = randomSeed.nextInt(parent2.size());
            int parent2Rnd2 = randomSeed.nextInt(parent2.size());
            List<FoundPlacesResult.Place> places1 = parent1.subList(Math.min(parent1Rnd1, parent1Rnd2), Math.max(parent1Rnd1, parent1Rnd2));
            List<FoundPlacesResult.Place> places2 = parent2.subList(Math.min(parent2Rnd1, parent2Rnd2), Math.max(parent2Rnd1, parent2Rnd2));

            List<FoundPlacesResult.Place> resultPlaces = new ArrayList<>();
            resultPlaces.addAll(places1);
            resultPlaces.addAll(places2);

            if(resultPlaces.isEmpty()) {
                resultPlaces.add((randomSeed.nextBoolean() ? parent1 : parent2).get(0));
            }

            FoundPlacesResult child = new FoundPlacesResult();
            child.placesToVisitInOrder = resultPlaces.stream().distinct().collect(Collectors.toList());

            assertIsOk(child.placesToVisitInOrder);

            children.add(child);
        }

        generation.addAll(children);
        return generation;
    }
}
