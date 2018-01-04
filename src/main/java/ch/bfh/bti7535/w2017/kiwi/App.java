package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.filter.Tokenizer;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.output.prediction.HTML;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

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
        System.out.println(dataSet);
        Tokenizer tokenizer = new Tokenizer(dataSet);
        Instances filteredInstances = tokenizer.apply();
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
