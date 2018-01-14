package ch.bfh.bti7535.w2017.kiwi.filter;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 4.01.18.
 *
 * Filter to replace common characters and replace these with their full term
 */
public class CharacterReplacerFilter {

    public static Stream<String> filter(Stream<String> stream) {
        List<String> wordlist = stream.collect(Collectors.toList());
        List<String> filtered = Lists.newArrayList();
        wordlist.forEach(word -> {
            if (word.contains("'re"))
            {
                replaceAndAdd(word,"'re", " are", filtered);
            }
            else if (word.contains("'s"))
            {
                replaceAndAdd(word,"'s", " is", filtered);
            }
            else if (word.contains("'ll"))
            {
                replaceAndAdd(word,"'ll", " will", filtered);
            }
            else if (word.equalsIgnoreCase("i've"))
            {
                replaceAndAdd(word,"i've", "I have", filtered);
            }
            else if (word.startsWith("\n"))
            {
                word = word.substring(1);
                filtered.add(word);
            }
            else if (word.endsWith("\n"))
            {
                word = word.substring(0, word.length() - 2);
                filtered.add(word);
            }
            else if ("don't".equals(word) || "can't".equals(word) || "won't".equals(word)
                    || "wasn't".equals(word) || "hadn't".equals(word))
            {
                // don't modify, keep for negation filter
                // ignore
            }
            else {
                filtered.add(word);
            }
        });
        return filtered.stream();
    }

    private static void replaceAndAdd(String word, String target, String replaceWith, List<String> filtered) {
        String replacement = word.replace(target, replaceWith);
        String[] parts = replacement.split(" ");
        if(parts.length > 0){
            filtered.add(parts[0]);
        }
        if(parts.length > 1){
            filtered.add(parts[1]);
        }
    }
}
