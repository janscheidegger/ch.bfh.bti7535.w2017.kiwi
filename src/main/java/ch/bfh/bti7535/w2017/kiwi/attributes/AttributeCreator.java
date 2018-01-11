package ch.bfh.bti7535.w2017.kiwi.attributes;

import weka.core.Instances;

public interface AttributeCreator {
    Instances createAttribute(Instances instances);
}
