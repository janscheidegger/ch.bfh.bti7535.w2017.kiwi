package ch.bfh.bti7535.w2017.kiwi.filter;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 4.01.18.
 */
public class CharacterReplacerFilter {

    public static Stream<String> filter(Stream<String> stream) {
        List<String> wordlist = stream.collect(Collectors.toList());
        List<String> filtered = Lists.newArrayList();
        wordlist.forEach(w -> {
            String word = w;
            if(word.startsWith("\n")){
                word = word.substring(1);
            }
            if(word.endsWith("\n")){
                word = word.substring(0, word.length()-2);
            }

            if(word.contains("'")){
                if(word.contains("'re")){
                    String replacement = word.replace("'re", " are");
                    filtered.add(replacement);
                }
                else if(word.contains("'s")){
                    String replacement = word.replace("'s", " is");
                    filtered.add(replacement);
                }
                else if(word.contains("'ll")){
                    String replacement = word.replace("'ll", " will");
                    filtered.add(replacement);
                }
                else if(word.equalsIgnoreCase("i've")){
                    String replacement = word.toLowerCase().replace("i've", "I have");
                    filtered.add(replacement);
                }
                else if("don't".equals(word) ||"can't".equals(word)  || "won't".equals(word)
                        || "wasn't".equals(word) || "hadn't".equals(word) ){
                    // don't modify, keep for negation filter
                }
            }
            else{
                filtered.add(w);
            }
        });
        return filtered.stream();
    }
}
