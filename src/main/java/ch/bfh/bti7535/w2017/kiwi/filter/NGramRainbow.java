package ch.bfh.bti7535.w2017.kiwi.filter;

import weka.core.stopwords.Rainbow;

public class NGramRainbow extends Rainbow {
    @Override
    protected boolean is(String word) {
        for(String splittedWord : word.split(" ")) {
            if(m_Words.contains(splittedWord)) return true;
        }
        return false;
    }
}
