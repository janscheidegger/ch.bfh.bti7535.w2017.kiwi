package ch.bfh.bti7535.w2017.kiwi.attributes;

import weka.core.Instance;
import weka.core.Instances;

public class NumExclamationMarks implements AttributeCreator {
    @Override
    public Instances createAttribute(Instances instances) {

        instances.insertAttributeAt(new weka.core.Attribute("numExclamation"), instances.numAttributes());

        for (int i = 0; i < instances.numInstances() - 1; i++) {
            Instance instance = instances.get(i);
            int count = instance.stringValue(0)
                                .length() - instance.stringValue(0)
                                                    .replaceAll("!", "")
                                                    .length();
            instance.setValue(instances.numAttributes() - 1, count);
        }
        return instances;
    }
}
