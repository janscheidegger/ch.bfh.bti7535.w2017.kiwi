package ch.bfh.bti7535.w2017.kiwi.filter;

import ch.bfh.bti7535.w2017.kiwi.utils.Utils;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 4.01.18.
 */
public class NegationFilter {

    public static Stream<String> filter(Stream<String> stream) {
        List<String> wordlist = stream.collect(Collectors.toList());
        List<String> filtered = Lists.newArrayList();
        wordlist.forEach(w -> {
            if(w.equals("don't")){
                filtered.add("NOT_do");
            }
            else if(w.equals("can't")){
                filtered.add("NOT_can");
            }
            else if(w.equals("won't")){
                filtered.add("NOT_will");
            }
            else if(w.equals("wasn't")){
                filtered.add("NOT_was");
            }
            else if(w.equals("hadn't")){
                filtered.add("NOT_has");
            }
            else{
                filtered.add(w);
            }
        });
        return filtered.stream();
    }
}
