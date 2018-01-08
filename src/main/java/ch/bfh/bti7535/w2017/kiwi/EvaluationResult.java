package ch.bfh.bti7535.w2017.kiwi;

import java.util.Map;

public class EvaluationResult {

    private double percentCorrect;
    private double percentIncorrect;
    private Map<String, String> usedConfiguration;

    public EvaluationResult(double percentCorrect, double percentIncorrect, Map<String, String> usedConfiguration) {
        this.percentCorrect = percentCorrect;
        this.percentIncorrect = percentIncorrect;
        this.usedConfiguration = usedConfiguration;
    }

    public double getPercentIncorrect() {
        return percentIncorrect;
    }

    public void setPercentIncorrect(double percentIncorrect) {
        this.percentIncorrect = percentIncorrect;
    }

    public double getPercentCorrect() {
        return percentCorrect;
    }

    public void setPercentCorrect(double percentCorrect) {
        this.percentCorrect = percentCorrect;
    }

    public Map<String, String> getUsedConfiguration() {
        return usedConfiguration;
    }

    public void setUsedConfiguration(Map<String, String> usedConfiguration) {
        this.usedConfiguration = usedConfiguration;
    }
}
