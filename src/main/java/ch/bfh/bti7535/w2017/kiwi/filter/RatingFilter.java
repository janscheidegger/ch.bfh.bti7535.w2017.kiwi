package ch.bfh.bti7535.w2017.kiwi.filter;

import com.google.common.collect.Lists;
import edu.stanford.nlp.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 4.01.18.
 * <p>
 * <p>
 * Filter replacing numeric ratings with sentiments.
 */
public class RatingFilter {

    public static Stream<String> filter(Stream<String> stream) {
        List<String> wordlist = stream.collect(Collectors.toList());
        List<String> filtered = Lists.newArrayList();
        wordlist.forEach(w -> {
            if (w.contains("/")) {
                String[] parts = w.split("/");
                if (parts.length == 2 && StringUtils.isNumeric(parts[0]) && StringUtils.isNumeric(parts[1])) {
                    int rating = Integer.parseInt(parts[0]);
                    int of = Integer.parseInt(parts[1]);
                    int ratio = 100 / of * rating;
                    if (ratio >= 80) {
                        filtered.add("amazing");
                    } else if (ratio >= 60) {
                        filtered.add("good");
                    } else if (ratio >= 50) {
                        filtered.add("average");
                    } else if (ratio >= 40) {
                        filtered.add("sufficient");
                    } else if (ratio >= 20) {
                        filtered.add("poor");
                    } else {
                        filtered.add("very poor");
                    }
                } else {
                    filtered.add(w);
                }
            } else {
                filtered.add(w);
            }
        });
        return filtered.stream();
    }
}
