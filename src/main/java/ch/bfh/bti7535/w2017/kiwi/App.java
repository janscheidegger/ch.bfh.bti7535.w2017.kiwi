package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.filter.Tokenizer;
import ch.bfh.bti7535.w2017.kiwi.loader.AttributeLoader;
import ch.bfh.bti7535.w2017.kiwi.loader.FolderLoader;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(App.class.getClassLoader()
                                              .getResource("txt_sentoken").toURI()));
        Instances dataSet = loader.getDataSet();
        System.out.println(dataSet.toSummaryString());
        System.out.println(dataSet);
        Tokenizer tokenizer = new Tokenizer(dataSet);
        try {
            Instances filteredInstances = tokenizer.apply();
            System.out.println(filteredInstances.toSummaryString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instances structure = loader.getStructure();
        structure.setClassIndex(structure.numAttributes() - 1);

    }

    public static void main2(String[] args) throws URISyntaxException {
        // Load Data
        URL resourceUrl = App.class.getClassLoader()
                                   .getResource("txt_sentoken");
        Path resourcePath = Paths.get(resourceUrl.toURI());
        FolderLoader loader = new FolderLoader(resourcePath, "", new AttributeLoader[]{});
        Instances instances = loader.getInstances();
        System.out.println(instances.toSummaryString());


        // Filter and Tokenize Data
        Tokenizer tokenizer = new Tokenizer(instances);
        try {
            Instances filteredInstances = tokenizer.apply();
            System.out.println(filteredInstances.toSummaryString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
