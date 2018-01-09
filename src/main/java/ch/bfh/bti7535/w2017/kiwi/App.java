package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.filter.NGramRainbow;
import ch.bfh.bti7535.w2017.kiwi.filter.Preprocessor;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import ch.bfh.bti7535.w2017.kiwi.baseline.SentiWordNetDemo;
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
        // BASELINE
        SentiWordNetDemo classifier = new SentiWordNetDemo();

        // negative Files
        int countNegCorrect=0, countNegWrong=0;
        File[] negFiles = new File(App.class.getClassLoader().getResource("txt_sentoken/neg").getPath()).listFiles();
        for (File file: negFiles) {
            classifier.load(App.class.getClassLoader()
                    .getResource("txt_sentoken/neg/" + file.getName()).getPath());
            if( classifier.classifyAllPOSN() == "no") countNegCorrect++;
            else countNegWrong++;
        }
        System.out.println("countNegCorrect: " + countNegCorrect);
        System.out.println("countNegWrong: " + countNegWrong);
        System.out.println();

        // positive Files
        int countPosCorrect=0, countPosWrong=0;
        File[] posFiles = new File(App.class.getClassLoader().getResource("txt_sentoken/pos").getPath()).listFiles();
        for (File file: posFiles) {
            classifier.load(App.class.getClassLoader()
                    .getResource("txt_sentoken/pos/" + file.getName()).getPath());
            if( classifier.classifyAllPOSN() == "yes") countPosCorrect++;
            else countPosWrong++;
        }
        System.out.println("countPosCorrect: " + countPosCorrect);
        System.out.println("countPosWrong: " + countPosWrong);
        System.out.println();

        System.out.println("Correct: " + (double) Math.round( (countPosCorrect+countNegCorrect) / 2000.0 * 100) / 100);
        System.out.println("Error: " + (double) Math.round( (countPosWrong+countNegWrong) / 2000.0 * 100) / 100);
        System.out.println();
        // CLASSIFIER       POS_CORRECT POS_WRONG   NEG_CORRECT NEG_WRONG   CORRECT ERROR
        // classifyAllPOSY  899         101         221         779         0.56    0.44
        // classifyAllPOSN  892         108         231         769         0.56    0.44
        // classifyADJY     856         144         341         659         0.6     0.4
        // classifyADJN     843         157         369         631         0.61    0.39
        // ***************************************************

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
