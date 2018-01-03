package ch.bfh.bti7535.w2017.kiwi.loader;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FolderLoader {

    private Instances data;

    /**
     * @param folder           Pass name of folder with one level of subfolders, each folder representing one class
     * @param name             Name of the Instance set
     * @param attributeLoaders Create an Attribute for each Loader
     */
    public FolderLoader(Path folder, String name, AttributeLoader... attributeLoaders) {

        ArrayList<Attribute> attributes = new ArrayList<>();
        Attribute classifierAttribute = new Attribute("classifier", true);
        attributes.add(classifierAttribute);
        // Capacity is only used for internal Arraylist creation and has only small performance inpact in Java
        data = new Instances(name, attributes, 15);
        data.setClass(classifierAttribute);

        try {
            Files.walk(folder, 2, FileVisitOption.FOLLOW_LINKS)
                 .map(this::createInstance)
                 .forEach(data::add);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private Instance createInstance(Path file) {
        String classifierClass = file.getParent()
                                     .getFileName()
                                     .toString();

        Instance instance = new DenseInstance(1);
        instance.setDataset(data);
        instance.setClassValue(classifierClass);
        return instance;
    }

    /**
     *
     * @return Instances created from the current folder to work with weka
     */
    public Instances getInstances() {
        return data;
    }

}
