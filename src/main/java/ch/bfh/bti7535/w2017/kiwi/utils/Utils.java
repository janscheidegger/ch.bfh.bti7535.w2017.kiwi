package ch.bfh.bti7535.w2017.kiwi.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nicolasschmid on 01.01.18.
 */
public class Utils {

    private static String findResource(String name){
        URL url = Utils.class.getClassLoader().getResource(name);
        return url.getPath();
    }

    public static File senTokenBaseDir() {
        return new File(findResource("txt_sentoken"));
    }

    public static Set<String> readNegativeWords(){
        return readWords(findResource("mkulakowski2/negative-words.txt"));
    }

    public static Set<String> readPositiveWords(){
        return readWords(findResource("mkulakowski2/positive-words.txt"));
    }

    private static Set<String> readWords(String filename){
        try {
            return IOUtils.readLines(new FileInputStream(new File(filename)), Charset.forName("UTF-8"))
                    .stream().filter(l -> { return !l.startsWith(";") && l.length() > 0; }).collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Sets.newHashSet();
    }

    public static List<String> positiveReviews() {
        return loadReviews(findResource("txt_sentoken/pos"));
    }

    public static List<String> negativeReviews() {
        return loadReviews(findResource("txt_sentoken/neg"));
    }

    private static List<String> loadReviews(String directory){
        File dir = new File(directory);
        File[] files = dir.listFiles();
        return Stream.of(files).map(f -> {
            try {
                return FileUtils.readFileToString(f, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.toList());
    }

    public static String toPlainString(List<String> list){
        StringBuffer sb = new StringBuffer();
        list.forEach(l -> {
            sb.append(l);
        });

        return sb.toString();
    }
}
