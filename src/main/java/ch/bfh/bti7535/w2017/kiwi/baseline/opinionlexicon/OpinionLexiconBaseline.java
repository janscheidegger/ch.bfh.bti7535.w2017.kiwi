package ch.bfh.bti7535.w2017.kiwi.baseline.opinionlexicon;

import ch.bfh.bti7535.w2017.kiwi.utils.Utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.bfh.bti7535.w2017.kiwi.baseline.opinionlexicon.OpinionLexiconBaseline.Sentiment.*;

/**
 * Baseline class used to count sentiment words.
 */
public class OpinionLexiconBaseline {

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL;
    }

    private final Set<String> positiveWords;
    private final Set<String> negativeWords;

    public OpinionLexiconBaseline() {
        positiveWords = Utils.readPositiveWords();
        negativeWords = Utils.readNegativeWords();
    }

    public Sentiment classify(Stream<String> wordStream) {
        List<String> list = wordStream.collect(Collectors.toList());
        int count = 0;
        for (String word : list) {
            if (positiveWords.contains(word)) {
                count++;
            } else if (negativeWords.contains(word)) {
                count--;
            }
        }
        if (count > 0) {
            return POSITIVE;
        }
        else if (count < 0) {
            return NEGATIVE;
        }
        else {
            return NEUTRAL;
        }
    }

    public void evaluate(List<Sentiment> positiveSentiments, List<Sentiment> negativeSentiments) {

        long countNegWrong = negativeSentiments.stream().filter(sentiment -> sentiment == POSITIVE).count();
        long countNegCorrect = negativeSentiments.size() - countNegWrong;

        System.out.println("Total Negative Correct: " + countNegCorrect);
        System.out.println("Total Negative Wrong: " + countNegWrong);
        System.out.println();

        long countPosWrong = positiveSentiments.stream().filter(sentiment -> sentiment == NEGATIVE).count();
        long countPosCorrect = positiveSentiments.size() - countPosWrong;

        System.out.println("Total Positive Correct: " + countPosCorrect);
        System.out.println("Total Positive Wrong: " + countPosWrong);
        System.out.println();

        System.out.println("Correct: " + (double) Math.round((countPosCorrect + countNegCorrect) / 2000.0 * 100) / 100);
        System.out.println("Error: " + (double) Math.round((countPosWrong + countNegWrong) / 2000.0 * 100) / 100);
        System.out.println();
    }
}
