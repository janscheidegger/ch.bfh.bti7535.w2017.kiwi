package ch.bfh.bti7535.w2017.kiwi.processors;

import ch.bfh.bti7535.w2017.kiwi.utils.Utils;
import edu.stanford.nlp.pipeline.CoreNLPProtos;

import java.util.List;
import java.util.Set;

/**
 * Baseline class used to count sentiment words.
 */
public class DefaultBaseline {

    private final Set<String> positiveWords;
    private final Set<String> negativeWords;

    public DefaultBaseline() {
        positiveWords = Utils.readPositiveWords();
        negativeWords = Utils.readNegativeWords();
    }

    public CoreNLPProtos.Sentiment count(List<String> words) {
        int count = 0;
        for (String word : words) {
            if (positiveWords.contains(word)) {
                count++;
            } else if (negativeWords.contains(word)) {
                count--;
            }
        }
        return count >= 0 ? CoreNLPProtos.Sentiment.STRONG_POSITIVE : CoreNLPProtos.Sentiment.STRONG_NEGATIVE;
    }

    public CoreNLPProtos.Sentiment count(String word) {
        int count = 0;
        if (positiveWords.contains(word)) {
            count++;
        } else if (negativeWords.contains(word)) {
            count--;
        }
        return count >= 0 ? CoreNLPProtos.Sentiment.STRONG_POSITIVE : CoreNLPProtos.Sentiment.STRONG_NEGATIVE;
    }

}
