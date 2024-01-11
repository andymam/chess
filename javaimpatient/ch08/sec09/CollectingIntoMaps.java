package ch08.sec09;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Person(int id, String name) {}

public class CollectingIntoMaps {

    public static Stream<Person> people() {
        return Stream.of(new Person(1001, "Peter"), new Person(1002, "Paul"), new Person(1003,
                "Mary"));
    }

    public static void main(String[] args) throws IOException {
        Map<Integer, String> idToName = people().collect(
                Collectors.toMap(Person::id, Person::name));
        System.out.println("idToName: " + idToName);

        Map<Integer, Person> idToPerson = people().collect(
                Collectors.toMap(Person::id, Function.identity()));
        System.out.println("idToPerson: " + idToPerson.getClass().getName() + idToPerson);

        idToPerson = people().collect(
                Collectors.toMap(Person::id, Function.identity(), (existingValue, newValue) -> {
                    throw new IllegalStateException();
                }, TreeMap::new));

        System.out.println("idToPerson: " + idToPerson.getClass().getName() + idToPerson);

        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, String> languageNames = locales.collect(Collectors.toMap(
                Locale::getDisplayLanguage, 
                loc -> loc.getDisplayLanguage(loc),
                (existingValue, newValue) -> existingValue));
        System.out.println("languageNames: " + languageNames);

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryLanguageSets = locales.collect(
                Collectors.toMap(
                        Locale::getDisplayCountry, 
                        l -> Collections.singleton(l.getDisplayLanguage()),
                        (a, b) -> { // union of a and b
                            var union = new HashSet<String>(a);
                            union.addAll(b);
                            return union;
                }));
        System.out.println("countryLanguageSets: " + countryLanguageSets);
    }
}
