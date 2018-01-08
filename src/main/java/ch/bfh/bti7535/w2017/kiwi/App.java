package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.filter.NGramRainbow;
import ch.bfh.bti7535.w2017.kiwi.filter.Preprocessor;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(App.class.getClassLoader()
                                              .getResource("txt_sentoken")
                                              .toURI()));
        Instances initialDataset = loader.getDataSet();

        List<Preprocessor> preprocessors = Arrays.asList(
                new Preprocessor.Builder("NGramm 1000/1000")
                        .withStopwordsHandler(new NGramRainbow())
                        .withWordsToKeep(1_000)
                        .withTokenizer(new NGramTokenizer())
                        .withStemmer(new SnowballStemmer())
                        .withIDFTransform(true)
                        .withTFTransform(true)
                        .withDoNotOperateOnPerClassBasis(true)
                        .withAttributeSelection(new InfoGainAttributeEval(), new Ranker(), 1000)
                        .build(),
                new Preprocessor.Builder("Ngramm 1000/100")
                        .withStopwordsHandler(new NGramRainbow())
                        .withWordsToKeep(1_000)
                        .withTokenizer(new NGramTokenizer())
                        .withStemmer(new SnowballStemmer())
                        .withIDFTransform(true)
                        .withTFTransform(true)
                        .withDoNotOperateOnPerClassBasis(true)
                        .withAttributeSelection(new InfoGainAttributeEval(), new Ranker(), 500)
                        .build(),
                new Preprocessor.Builder("Ngramm 10000/500")
                        .withStopwordsHandler(new NGramRainbow())
                        .withWordsToKeep(10_000)
                        .withTokenizer(new NGramTokenizer())
                        .withStemmer(new SnowballStemmer())
                        .withIDFTransform(true)
                        .withTFTransform(true)
                        .withDoNotOperateOnPerClassBasis(true)
                        .withAttributeSelection(new InfoGainAttributeEval(), new Ranker(), 500)
                        .build()
                , new Preprocessor.Builder("Normal Tokenize")
                        .withStopwordsHandler(new Rainbow())
                        .withWordsToKeep(1_000)
                        .withTokenizer(new WordTokenizer())
                        .withStemmer(new SnowballStemmer())
                        .withIDFTransform(true)
                        .withTFTransform(true)
                        .withDoNotOperateOnPerClassBasis(true)
                        .withAttributeSelection(new InfoGainAttributeEval(), new Ranker(), 1000)
                        .build());


        List<EvaluationResult> results = new ArrayList<>();

        Evaluator evaluator = new Evaluator();

        preprocessors.forEach((p) -> {
            try {
                Instances preprocessedInstances = p.apply(initialDataset);
                results.add(evaluator.evaluate(preprocessedInstances, p.getConfiguration()));

            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        for (EvaluationResult er : results) {
            for (Map.Entry<String, String> configEntry : er.getUsedConfiguration()
                                                           .entrySet()) {
                System.out.println(configEntry.getKey() + "\t" + configEntry.getValue());
            }
            System.out.println("Correct: " + er.getPercentCorrect());
            System.out.println("Error: " + er.getPercentIncorrect());
            System.out.println();
        }

    }

}
