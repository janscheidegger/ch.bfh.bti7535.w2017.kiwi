package ch.bfh.bti7535.w2017.kiwi.filter;

import opennlp.tools.stemmer.PorterStemmer;

import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 14.01.18.
 */
public class PorterStemmerMapper {

    public static Stream<String> stem(Stream<String> stream) {
        PorterStemmer stemmer = new PorterStemmer();
        return stream.map(stemmer::stem);
    }
}
