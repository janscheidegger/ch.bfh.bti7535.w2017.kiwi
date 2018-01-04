package ch.bfh.bti7535.w2017.kiwi;

import ch.bfh.bti7535.w2017.kiwi.processors.DefaultBaseline;
import ch.bfh.bti7535.w2017.kiwi.processors.NaiveBayesProcessor;
import ch.bfh.bti7535.w2017.kiwi.processors.Processor;
import ch.bfh.bti7535.w2017.kiwi.utils.Utils;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import weka.core.tokenizers.Tokenizer;
import weka.core.tokenizers.WordTokenizer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws Exception
    {
        // Processor processor = new NaiveBayesProcessor();
        // processor.process();

        DefaultBaseline baseline = new DefaultBaseline();

        List<String>  positiveWords = ch.bfh.bti7535.w2017.kiwi.utils.Tokenizer.tokenize(Utils.positiveReviews());
        List<String>  negativeWords = ch.bfh.bti7535.w2017.kiwi.utils.Tokenizer.tokenize(Utils.negativeReviews());

        List<CoreNLPProtos.Sentiment> pos = positiveWords
                .stream()
                .map(baseline::count)
                .collect(Collectors.toList());
        List<CoreNLPProtos.Sentiment> neg = negativeWords
                .stream()
                .map(baseline::count)
                .collect(Collectors.toList());

    }
}
