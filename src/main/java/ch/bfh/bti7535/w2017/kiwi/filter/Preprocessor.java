package ch.bfh.bti7535.w2017.kiwi.filter;

import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.stopwords.Rainbow;
import weka.core.stopwords.StopwordsHandler;
import weka.core.tokenizers.Tokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Arrays;

public class Preprocessor {

    private StringToWordVector stringToWordVector;

    private Preprocessor() {
        stringToWordVector = new StringToWordVector();
    }

    public Instances apply(Instances instances) throws Exception {
        System.out.println("Applying filter with config: " + Arrays.toString(stringToWordVector.getOptions()));

        try {
            stringToWordVector.setInputFormat(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Filter.useFilter(instances, stringToWordVector);
    }

    public static class Builder {

        private Preprocessor preprocessor = new Preprocessor();

        public Builder withStopwordsHandler(StopwordsHandler stopwordsHandler) {
            preprocessor.stringToWordVector.setStopwordsHandler(stopwordsHandler);
            return this;
        }

        public Builder withStemmer(Stemmer stemmer) {
            preprocessor.stringToWordVector.setStemmer(stemmer);
            return this;
        }


        public Builder withWordsToKeep(int wordsToKeep) {
            preprocessor.stringToWordVector.setWordsToKeep(wordsToKeep);
            return this;
        }

        public Builder withTokenizer(Tokenizer tokenizer) {
            preprocessor.stringToWordVector.setTokenizer(tokenizer);
            return this;
        }

        public Preprocessor build() {
            return preprocessor;
        }

    }

}
