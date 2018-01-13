package ch.bfh.bti7535.w2017.kiwi.utils;

import weka.core.tokenizers.WordTokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolasschmid on 03.01.18.
 */
public class Tokenizer {

    public static List<String> tokenize(List<String> lines) {
        weka.core.tokenizers.Tokenizer tokenizer = new WordTokenizer();
        List<String> words = new ArrayList<>();
        tokenizer.tokenize(Utils.toPlainString(lines));
        while (tokenizer.hasMoreElements()) {
            words.add(tokenizer.nextElement());
        }
        return words;
    }
}
