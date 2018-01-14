package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.attributes.NumExclamationMarks;
import ch.bfh.bti7535.w2017.kiwi.baseline.opinionlexicon.OpinionLexiconBaseline;
import ch.bfh.bti7535.w2017.kiwi.baseline.sentiwordnet.SentiWordNetBaseline;
import ch.bfh.bti7535.w2017.kiwi.filter.*;
import ch.bfh.bti7535.w2017.kiwi.utils.Tokenizer;
import ch.bfh.bti7535.w2017.kiwi.utils.Utils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Application entry point.
 */
public class App {

    public static void main(String[] args) throws Exception {
        // Baseline 1 with SentiWordNet
        System.out.println("------ BASELINE 1 ------ ");
        SentiWordNetBaseline classifier = new SentiWordNetBaseline();
        System.out.println("SENTIWORDNET BASELINE NON-WEIGHTED");
        classifier.evaluate(false);
        System.out.println("SENTIWORDNET BASELINE WEIGHTED");
        classifier.evaluate(true);

        // NON-WEIGHTED
        // CLASSIFIER       POS_CORRECT POS_WRONG   NEG_CORRECT NEG_WRONG   CORRECT ERROR
        // classifyAllPOSY  911         89          176         824         0.54    0.46
        // classifyAllPOSN  905         95          813         187         0.55    0.45
        // classifyADJY     808         192         366         634         0.59    0.41
        // classifyADJN     796         204         400         600         0.6     0.4

        // WEIGHTED
        // CLASSIFIER       POS_CORRECT POS_WRONG   NEG_CORRECT NEG_WRONG   CORRECT ERROR
        // classifyAllPOSY  899         101         221         779         0.56    0.44
        // classifyAllPOSN  892         108         231         769         0.56    0.44
        // classifyADJY     856         144         341         659         0.6     0.4
        // classifyADJN     843         157         369         631         0.61    0.39
        // ***************************************************

        // Baseline 2
        System.out.println("\n\n------ BASELINE 2 ------ ");
        OpinionLexiconBaseline baseline = new OpinionLexiconBaseline();
        List<String> positiveReviews = Utils.positiveReviews();
        List<String> negativeReviews = Utils.negativeReviews();

        List<Stream<String>> positiveWords = positiveReviews
                .stream()
                .map(Tokenizer::tokenize)
                .collect(Collectors.toList());

        List<Stream<String>> negativeWords = negativeReviews
                .stream()
                .map(Tokenizer::tokenize)
                .collect(Collectors.toList());

        List<OpinionLexiconBaseline.Sentiment> positiveSentiments = positiveWords.stream()
                .map(StopwordFilter::filter)
                .map(UnusedCharacterFilter::filter)
                .map(CharacterReplacerFilter::filter)
                .map(NegationFilter::filter)
                .map(RatingFilter::filter)
                //.map(PorterStemmerMapper::stem)
                .map(baseline::classify)
                .collect(Collectors.toList());
        List<OpinionLexiconBaseline.Sentiment> negativeSentiments = negativeWords.stream()
                .map(StopwordFilter::filter)
                .map(UnusedCharacterFilter::filter)
                .map(CharacterReplacerFilter::filter)
                .map(NegationFilter::filter)
                .map(RatingFilter::filter)
                //.map(PorterStemmerMapper::stem)
                .map(baseline::classify)
                .collect(Collectors.toList());

        baseline.evaluate(positiveSentiments, negativeSentiments);
        /// baselines done ...

        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(Utils.senTokenBaseDir());
        Instances initialDataset = loader.getDataSet();

        List<Preprocessor> preprocessors = Arrays.asList(
                new Preprocessor.Builder("NGramm 1000/1000")
                        .withStopwordsHandler(new NGramRainbow())
                        .withAttributeCreators(new NumExclamationMarks())
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
                        .withAttributeCreators(new NumExclamationMarks())
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
                        .withAttributeCreators(new NumExclamationMarks())
                        .withWordsToKeep(10_000)
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
                        .build(),
                new Preprocessor.Builder("Normal Tokenize")
                        .withStopwordsHandler(new Rainbow())
                        .withAttributeCreators(new NumExclamationMarks())
                        .withWordsToKeep(10_000)
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
            System.out.println(er.getUsedConfiguration().get("Name"));
            System.out.println("Correct: " + er.getPercentCorrect());
            System.out.println("Error: " + er.getPercentIncorrect());
            System.out.println();
        }

    }

}
