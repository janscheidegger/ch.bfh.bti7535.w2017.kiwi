package ch.bfh.bti7535.w2017.kiwi.utils;

import com.google.common.collect.Lists;
import weka.core.tokenizers.WordTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 03.01.18.
 */
public class Tokenizer {

    /**
     * Tokenize input lines to words
     * @param line
     * @return
     */
    public static Stream<String> tokenize(String line) {
        List<String> words = new ArrayList<>();
        String[] tokenized = line.split(" ");
        words.addAll(Lists.newArrayList(tokenized));
        return words.stream();
    }

    public static Stream<String> wordtokenizer(String line) {
        weka.core.tokenizers.Tokenizer tokenizer = new WordTokenizer();
        List<String> words = new ArrayList<>();
        tokenizer.tokenize(line);
        while (tokenizer.hasMoreElements()) {
            words.add(tokenizer.nextElement());
        }
        return words.stream();
    }

}
