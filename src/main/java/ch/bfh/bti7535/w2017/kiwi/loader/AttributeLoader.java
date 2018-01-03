package ch.bfh.bti7535.w2017.kiwi.loader;

import weka.core.Attribute;

import java.nio.file.Path;

public interface AttributeLoader {

    /**
     * Generates an Attribute for each file passed to the Method
     * @param Path to file to bee analysed by this Attribute Loader
     * @return an Attribute to be added to the Instances
     */
    Attribute generateAttribute(Path file);
}
