package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.loader.AttributeLoader;
import ch.bfh.bti7535.w2017.kiwi.loader.FolderLoader;
import weka.core.Instances;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws URISyntaxException {
        // Load Data
        URL resourceUrl = App.class.getClassLoader()
                                   .getResource("txt_sentoken");
        Path resourcePath = Paths.get(resourceUrl.toURI());
        FolderLoader loader = new FolderLoader(resourcePath, "", new AttributeLoader[]{});
        Instances instances = loader.getInstances();
        System.out.println(instances.toSummaryString());
    }
}
