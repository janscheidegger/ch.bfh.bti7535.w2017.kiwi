package ch.bfh.bti7535.w2017.kiwi;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import java.util.Map;
import java.util.Random;

public class Evaluator {

    public EvaluationResult evaluate(Instances instances, Map<String, String> usedConfig) throws Exception {
        NaiveBayes model = new NaiveBayes();

        Evaluation eval = new Evaluation(instances);
        eval.crossValidateModel(model, instances, 10, new Random(1));

        return new EvaluationResult(eval.pctCorrect(), eval.pctIncorrect(), usedConfig);

    }

}
