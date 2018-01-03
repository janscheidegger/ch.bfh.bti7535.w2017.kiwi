package ch.bfh.bti7535.w2017.kiwi.filter;

import weka.core.Instances;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Tokenizer {

    private StringToWordVector stringToWordVector;
    private WordTokenizer wordTokenizer;
    private Instances instances;

    public Tokenizer(Instances instances) {
        this.instances = instances;
        wordTokenizer = new WordTokenizer();
        stringToWordVector = new StringToWordVector();
        stringToWordVector.setTokenizer(wordTokenizer);
        stringToWordVector.setStopwordsHandler(new Rainbow());
        try {
            stringToWordVector.setInputFormat(this.instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Instances apply() throws Exception {
        return Filter.useFilter(instances, stringToWordVector);
    }

}
