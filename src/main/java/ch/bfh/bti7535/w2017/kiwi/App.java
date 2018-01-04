package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.filter.NGramRainbow;
import ch.bfh.bti7535.w2017.kiwi.filter.Preprocessor;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.output.prediction.HTML;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.io.File;
import java.util.Random;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(App.class.getClassLoader()
                                              .getResource("txt_sentoken")
                                              .toURI()));
        Instances dataSet = loader.getDataSet();
        System.out.println(dataSet.toSummaryString());

//        AttributeSelection filter = new AttributeSelection();
//        CfsSubsetEval evaluator = new CfsSubsetEval();
//        GreedyStepwise search = new GreedyStepwise();
//        search.setSearchBackwards(true);
//        filter.setEvaluator(evaluator);
//        filter.setSearch(search);
//        filter.setInputFormat(dataSet);
//        // generate new data
//        Instances newDataSet = Filter.useFilter(dataSet, filter);

        Preprocessor preprocessor = new Preprocessor.Builder()
                .withStopwordsHandler(new NGramRainbow())
                .withWordsToKeep(1_000)
                .withTokenizer(new NGramTokenizer())
                .withStemmer(new SnowballStemmer())
                .build();



        Instances filteredInstances = preprocessor.apply(dataSet);
        System.out.println(filteredInstances.toSummaryString());

        NaiveBayes model = new NaiveBayes();

        // Do not pre build classifier for 10 Fold Cross Validation
        //model.buildClassifier(filteredInstances);

        Evaluation eval = new Evaluation(filteredInstances);
        HTML outputHtml = new HTML();
        StringBuffer buffer = new StringBuffer();
        outputHtml.setBuffer(buffer);

        eval.crossValidateModel(model, filteredInstances, 10, new Random(1), outputHtml);

        System.out.println(eval.toSummaryString());
        System.out.println(eval.toClassDetailsString());

    }

}
