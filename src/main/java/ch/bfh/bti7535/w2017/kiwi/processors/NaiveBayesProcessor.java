package ch.bfh.bti7535.w2017.kiwi.processors;

import ch.bfh.bti7535.w2017.kiwi.utils.Utils;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Created by nicolasschmid on 31.12.17.
 */
public class NaiveBayesProcessor implements Processor {

    public void process() throws Exception {
        TextDirectoryLoader textDirectoryLoader = new TextDirectoryLoader();
        textDirectoryLoader.setDirectory(Utils.senTokenBaseDir());
        Instances dataSet = textDirectoryLoader.getDataSet();

        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(dataSet);
        dataSet = Filter.useFilter(dataSet, filter);

        // 10-fold cross validation (stratified)
        /*
         * Performs a (stratified if class is nominal) cross-validation for a classifier on a set of instances.
         */
        Evaluation eval = new Evaluation(dataSet);
        eval.crossValidateModel(new NaiveBayes(), dataSet, 10, new Random(5)); // Correctly Classified Instances: 1625 : 81.25%

        System.out.println(eval.toSummaryString());
    }
}
