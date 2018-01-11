package ch.bfh.bti7535.w2017.kiwi.filter;

import ch.bfh.bti7535.w2017.kiwi.attributes.AttributeCreator;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.stemmers.Stemmer;
import weka.core.stopwords.StopwordsHandler;
import weka.core.tokenizers.Tokenizer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {

    private StringToWordVector stringToWordVector;
    private Map<String, String> configuration = new HashMap<>();
    private List<AttributeCreator> attributeCreators = new ArrayList<>();

    private AttributeSelection attributeSelection = null;

    private Preprocessor() {
        stringToWordVector = new StringToWordVector();
    }

    public Instances apply(Instances saveInstance) throws Exception {
        System.out.println("Applying filter with config: " + Arrays.toString(stringToWordVector.getOptions()));

        Instances instances = new Instances(saveInstance);

        for (AttributeCreator attributeCreator : attributeCreators) {
            instances = attributeCreator.createAttribute(instances);
        }


        try {
            stringToWordVector.setInputFormat(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instances result = Filter.useFilter(instances, stringToWordVector);

        if (attributeSelection != null) {
            result = applyAttributeSelection(result);
            configuration.put("Number of Attributes", result.numAttributes() + "");
            return result;
        } else {
            configuration.put("Number of Attributes", result.numAttributes() + "");
            return result;
        }

    }

    public Instances applyAttributeSelection(Instances instances) throws Exception {
        AttributeSelection filter = new AttributeSelection();
        GainRatioAttributeEval evaluator = new GainRatioAttributeEval();
        Ranker search = new Ranker();
        search.setNumToSelect(1000);
        filter.setEvaluator(evaluator);
        filter.setSearch(search);
        filter.setInputFormat(instances);

        return Filter.useFilter(instances, filter);
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public static class Builder {
        private Preprocessor preprocessor = new Preprocessor();

        public Builder(String name) {
            preprocessor.configuration.put("Name", name);
        }


        public Builder withAttributeSelection(ASEvaluation evaluator, Ranker search, int searchToSelect) {
            preprocessor.configuration.put("Attribuate Evaluator", evaluator.getClass()
                                                                            .getSimpleName());
            preprocessor.configuration.put("Ranker", search.getClass()
                                                           .getSimpleName());
            preprocessor.configuration.put("To select", String.valueOf(searchToSelect));
            preprocessor.attributeSelection = new AttributeSelection();
            preprocessor.attributeSelection.setEvaluator(evaluator);
            search.setNumToSelect(searchToSelect);
            preprocessor.attributeSelection.setSearch(search);
            return this;
        }

        public Builder withTFTransform(boolean tfTransform) {
            preprocessor.configuration.put("TF Transform", String.valueOf(tfTransform));
            preprocessor.stringToWordVector.setTFTransform(tfTransform);
            return this;
        }

        public Builder withDoNotOperateOnPerClassBasis(boolean doNotOperateOnPerClassBasis) {
            preprocessor.configuration.put("Do Not Operate On Per Class Basis", String.valueOf(doNotOperateOnPerClassBasis));

            preprocessor.stringToWordVector.setDoNotOperateOnPerClassBasis(doNotOperateOnPerClassBasis);
            return this;
        }

        public Builder withAttributeCreators(AttributeCreator... attributeCreators) {
            for (int i = 0; i < attributeCreators.length - 1; i++) {
                preprocessor.configuration.put("Attribute creator" + i, attributeCreators.getClass()
                                                                                         .getSimpleName());
            }
            this.preprocessor.attributeCreators.addAll(Arrays.asList(attributeCreators));
            return this;
        }

        public Builder withIDFTransform(boolean idfTransform) {
            preprocessor.configuration.put("IDF Transform", String.valueOf(idfTransform));
            preprocessor.stringToWordVector.setIDFTransform(idfTransform);
            return this;
        }


        public Builder withStopwordsHandler(StopwordsHandler stopwordsHandler) {
            preprocessor.configuration.put("Stopwords Handler", stopwordsHandler.getClass()
                                                                                .getSimpleName());
            preprocessor.stringToWordVector.setStopwordsHandler(stopwordsHandler);
            return this;
        }

        public Builder withStemmer(Stemmer stemmer) {
            preprocessor.configuration.put("Stemmer", stemmer.getClass()
                                                             .getSimpleName());
            preprocessor.stringToWordVector.setStemmer(stemmer);
            return this;
        }


        public Builder withWordsToKeep(int wordsToKeep) {
            preprocessor.configuration.put("Words To Keep", String.valueOf(wordsToKeep));
            preprocessor.stringToWordVector.setWordsToKeep(wordsToKeep);
            return this;
        }

        public Builder withTokenizer(Tokenizer tokenizer) {
            preprocessor.configuration.put("Tokenizer", tokenizer.getClass()
                                                                 .getSimpleName());
            preprocessor.stringToWordVector.setTokenizer(tokenizer);
            return this;
        }

        public Preprocessor build() {
            return preprocessor;
        }

    }

}
