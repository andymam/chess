package ch08.sec03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FilterMapDemo {
    public static <T> void show(String title, Stream<T> stream) {
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).toList();
        System.out.print(title + ": ");
        if (firstElements.size() <= SIZE)
            System.out.println(firstElements);
        else {
            firstElements.remove(SIZE);
            String out = firstElements.toString();
            System.out.println(out.substring(0, out.length() - 1) + ", ...]");
        }
    }

    public static Stream<String> codePoints(String s) {
        return s.codePoints().mapToObj(cp -> new String(new int [] { cp }, 0, 1));
    }

    public static void main(String[] args) throws IOException {
        String contents = Files.readString(Path.of("alice.txt"));
        List<String> words = List.of(contents.split("\\PL+"));
        Stream<String> longWords = words.stream().filter(w -> w.length() > 12);
        show("longWords", longWords);

        Stream<String> lowercaseWords = words.stream().map(String::toLowerCase);
        show("lowercaseWords", lowercaseWords);

        String[] song = { "row", "row", "row", "your", "boat", "gently", "down",
                "the", "stream" };
        Stream<String> firstChars = Stream.of(song).filter(w -> w.length() > 0).map(s -> s.substring(0, 1));
        show("firstChars", firstChars);

        Stream<String> letters = Stream.of(song).flatMap(w -> codePoints(w));
        show("letters", letters);
        
        Stream<String> result = words.stream().mapMulti((s, collector) -> {
            int i = 0;
            while (i < s.length()) {
                int cp = s.codePointAt(i);
                collector.accept(new String(new int [] { cp }, 0, 1));
                i += Character.charCount(cp);
            }
        });
        show("result", result);
    }
}
