package ch08.sec11;

import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

record Pair<S, T>(S first, T second) {}

record City(String name, String state, int population) {
}

public class DownstreamCollectors {
    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Path.of(filename)).map(l -> l.split(", ")).map(a -> new City(a[0], a[1], Integer.parseInt(a[2]))); 
    }
    
    public static Stream<String> codePoints(String s) {
        var result = new ArrayList<String>();
        int i = 0;
        while (i < s.length()) {
            int j = s.offsetByCodePoints(i, 1);
            result.add(s.substring(i, j));
            i = j;
        }
        return result.stream();
    }

    public static void main(String[] args) throws IOException {
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<Locale>> countryToLocaleSet = locales.collect(
           groupingBy(Locale::getCountry, toSet()));
        System.out.println("countryToLocaleSet: " + countryToLocaleSet);   

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Long> countryToLocaleCounts = locales.collect(
           groupingBy(Locale::getCountry, counting()));
        System.out.println("countryToLocaleCounts: " + countryToLocaleCounts);   

        Stream<City> cities = readCities("cities.txt");
        Map<String, Integer> stateToCityPopulation = cities.collect(
           groupingBy(City::state, summingInt(City::population)));
        System.out.println("stateToCityPopulation: " + stateToCityPopulation);

        cities = readCities("cities.txt");
        Map<String, Optional<City>> stateToLargestCity = cities.collect(
            groupingBy(City::state,
            maxBy(Comparator.comparing(City::population))));
        System.out.println("stateToLargestCity: " + stateToLargestCity);        
        
        cities = readCities("cities.txt");
        Map<String, Optional<String>> stateToLongestCityName = cities.collect(
           groupingBy(City::state, 
              mapping(City::name,
                 maxBy(Comparator.comparing(String::length)))));

        System.out.println("stateToLongestCityName: " + stateToLongestCityName);

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryToLanguages = locales.collect(
           groupingBy(Locale::getDisplayCountry, 
              mapping(Locale::getDisplayLanguage,
                 toSet())));
        System.out.println("countryToLanguages: " + countryToLanguages);   

        cities = readCities("cities.txt");
        Map<String, Set<String>> codePointsByState
            = cities.collect(
                groupingBy(City::state,
                    flatMapping(c -> codePoints(c.name().toLowerCase()),
                        toSet())));
        System.out.println("codePointsByState: " + codePointsByState);   

        cities = readCities("cities.txt");
        Map<String, Set<City>> largeCitiesByState
            = cities.collect(
                groupingBy(City::state,
                    filtering(c -> c.population() > 500000,
                        toSet())));

        System.out.println("largeCitiesByState: " + largeCitiesByState);
        
        cities = readCities("cities.txt");
        Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities.collect(
           groupingBy(City::state,
              summarizingInt(City::population)));
        System.out.println(stateToCityPopulationSummary.get("NY"));

        cities = readCities("cities.txt");
        Map<String, String> stateToCityNames = cities.collect(
           groupingBy(City::state,
              reducing("", City::name,
                 (s, t) -> s.length() == 0 ? t : s + ", " + t)));

        cities = readCities("cities.txt");
        stateToCityNames = cities.collect(
           groupingBy(City::state,
              mapping(City::name,
                 joining(", "))));
        System.out.println("stateToCityNames: " + stateToCityNames); 
        
        cities = readCities("cities.txt");
        Pair<List<String>, Double> result = cities.filter(c -> c.state().equals("NV"))
           .collect(teeing(
              mapping(City::name, toList()), // First downstream collector
              averagingDouble(City::population), // Second downstream collector
              (list, avg) -> new Pair<>(list,  avg))); // Combining function        
        System.out.println("result: " + result); 
    }
}
