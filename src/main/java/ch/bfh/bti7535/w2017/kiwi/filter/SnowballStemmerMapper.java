package ch.bfh.bti7535.w2017.kiwi.filter;

import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 14.01.18.
 */
public class SnowballStemmerMapper {

    public static Stream<String> stem(Stream<String> stream) {
        weka.core.stemmers.SnowballStemmer stemmer = new weka.core.stemmers.SnowballStemmer();
        return stream.map(stemmer::stem);
    }
}
