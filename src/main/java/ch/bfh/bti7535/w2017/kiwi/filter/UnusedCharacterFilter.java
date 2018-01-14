package ch.bfh.bti7535.w2017.kiwi.filter;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 4.01.18.
 */
public class UnusedCharacterFilter {

    private static List<String> CHARS_TO_FILTER = Lists.newArrayList();
    static {
        CHARS_TO_FILTER.add(".");
        CHARS_TO_FILTER.add(",");
        CHARS_TO_FILTER.add(";");
        CHARS_TO_FILTER.add(":");
        CHARS_TO_FILTER.add("!");
        CHARS_TO_FILTER.add("?");
        CHARS_TO_FILTER.add("(");
        CHARS_TO_FILTER.add(")");
        CHARS_TO_FILTER.add("[");
        CHARS_TO_FILTER.add("]");
    }

    public static Stream<String> filter(Stream<String> stream) {
        List<String> wordlist = stream.collect(Collectors.toList());
        List<String> filtered = Lists.newArrayList();
        wordlist.forEach(w -> {
            if(!CHARS_TO_FILTER.contains(w)){
                filtered.add(w);
            }
        });
        return filtered.stream();
    }
}
