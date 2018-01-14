package ch.bfh.bti7535.w2017.kiwi.baseline.sentiwordnet;

import ch.bfh.bti7535.w2017.kiwi.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SentiWordNetBaseline {

    /**
     * String that stores the text to guess its polarity.
     */
    String text;

    /**
     * SentiWordNet object to query the polarity of a word.
     */
    public SentiWordNetBaselineCode sentiwordnet = new SentiWordNetBaselineCode(getClass().getClassLoader()
            .getResource("sentiWords/SentiWordNet_3.0.0_20130122.txt").getPath());

    public File[] negFiles = new File(App.class.getClassLoader()
            .getResource("txt_sentoken/neg")
            .getPath()).listFiles();
    public File[] posFiles = new File(App.class.getClassLoader()
            .getResource("txt_sentoken/pos")
            .getPath()).listFiles();
    public SentiWordNetBaseline() throws IOException {
    }

    /**
     * This method loads the text to be classified.
     * @param fileName The name of the file that stores the text.
     */
    public void load(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            text = "";
            while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }
            // System.out.println("===== Loaded text data: " + fileName + " =====");
            reader.close();
            // System.out.println(text);
        }
        catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    public void evaluate(boolean weighted) {
        sentiwordnet.weighted = weighted;

        // negative Files
        int countNegCorrectAllPosY = 0, countNegWrongAllPosY = 0;
        int countNegCorrectAllPosN = 0, countNegWrongAllPosN = 0;
        int countNegCorrectAdjY = 0, countNegWrongAdjY = 0;
        int countNegCorrectAdjN = 0, countNegWrongAdjN = 0;

        for (File file : negFiles) {
            this.load(App.class.getClassLoader()
                    .getResource("txt_sentoken/neg/" + file.getName())
                    .getPath());
            if (this.classifyAllPOSY() == "no"){ countNegCorrectAllPosY++; }
            else { countNegWrongAllPosY++; }

            if (this.classifyAllPOSN() == "no"){ countNegCorrectAllPosN++; }
            else { countNegWrongAllPosN++; }

            if (this.classifyADJY() == "no"){ countNegCorrectAdjY++; }
            else { countNegWrongAdjY++; }

            if (this.classifyADJN() == "no"){ countNegCorrectAdjN++; }
            else { countNegWrongAdjN++; }
        }

        // positive Files
        int countPosCorrectAllPosY = 0, countPosWrongAllPosY = 0;
        int countPosCorrectAllPosN = 0, countPosWrongAllPosN = 0;
        int countPosCorrectAdjY = 0, countPosWrongAllAdjY = 0;
        int countPosCorrectAdjN = 0, countPosWrongAllAdjN = 0;
        for (File file : posFiles) {
            this.load(App.class.getClassLoader()
                    .getResource("txt_sentoken/pos/" + file.getName())
                    .getPath());
            if (this.classifyAllPOSY() == "yes"){ countPosCorrectAllPosY++; }
            else { countPosWrongAllPosY++; }

            if (this.classifyAllPOSN() == "yes"){ countPosCorrectAllPosN++; }
            else { countPosWrongAllPosN++; }

            if (this.classifyADJY() == "yes"){ countPosCorrectAdjY++; }
            else { countPosWrongAllAdjY++; }

            if (this.classifyADJN() == "yes"){ countPosCorrectAdjN++; }
            else { countPosWrongAllAdjN++; }
        }
        System.out.println("All words, if value = 0 -> rate positive: ");
        log(countNegCorrectAllPosY, countNegWrongAllPosY, countPosCorrectAllPosY, countPosWrongAllPosY);

        System.out.println("All words, if value = 0 -> rate negative: ");
        log(countNegCorrectAllPosN, countNegWrongAllPosN, countPosCorrectAllPosN, countPosWrongAllPosN);

        System.out.println("Just the adjectives, if value = 0 -> rate negative: ");
        log(countNegCorrectAdjY, countNegWrongAdjY, countPosCorrectAdjY, countPosWrongAllAdjY);

        System.out.println("Just the adjectives, if value = 0 -> rate negative: ");
        log(countNegCorrectAdjN, countNegWrongAdjN, countPosCorrectAdjN, countPosWrongAllAdjN);
    }

    /**
     * print on console
     */
    public void log( int negCorrect,  int negWrong, int posCorrect, int posWrong) {
        System.out.println("Total Negative Correct: " + negCorrect);
        System.out.println("Total Negative Wrong: " + negWrong);

        System.out.println("Total Positive Correct: " + posCorrect);
        System.out.println("Total Positive Wrong: " + posWrong);

        System.out.println("Correct: " + (double) Math.round((negCorrect + posCorrect) / 2000.0 * 100) / 100);
        System.out.println("Error: " + (double) Math.round((negWrong + posWrong) / 2000.0 * 100) / 100);
        System.out.println();
    }

    /**
     * This method performs the classification of the text.
     * Algorithm: Use all POS, say "yes" in case of 0.
     * @return An string with "no" (negative) or "yes" (positive).
     */
    public String classifyAllPOSY() {

        int count = 0;
        try {
            String delimiters = "\\W";
            String[] tokens = text.split(delimiters);
            String feeling = "";
            for (int i = 0; i < tokens.length; ++i) {
                // Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
                if (!tokens[i].equals("")) {
                    // Search as adjetive
                    feeling = sentiwordnet.extractFeeling(tokens[i],"a");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                    // Search as noun
                    feeling = sentiwordnet.extractFeeling(tokens[i],"n");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                    // Search as adverb
                    feeling = sentiwordnet.extractFeeling(tokens[i],"r");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                    // Search as verb
                    feeling = sentiwordnet.extractFeeling(tokens[i],"v");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                }
            }
            // System.out.println(count);
        }
        catch (Exception e) {
            System.out.println("Problem found when classifying the text ");
        }
//        System.out.println(count);
        // Returns "yes" in case of 0
        if (count >= 0)
            return "yes";
        else return "no";
    }

    /**
     * This method performs the classification of the text.
     * Algorithm: Use all POS, say "no" in case of 0.
     * @return An string with "no" (negative) or "yes" (positive).
     */
    public String classifyAllPOSN() {

        int count = 0;
        try {
            String delimiters = "\\W";
            String[] tokens = text.split(delimiters);
            String feeling = "";
            for (int i = 0; i < tokens.length; ++i) {
                // Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
                if (!tokens[i].equals("")) {
                    // Search as adjetive
                    feeling = sentiwordnet.extractFeeling(tokens[i],"a");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                    // Search as noun
                    feeling = sentiwordnet.extractFeeling(tokens[i],"n");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                    // Search as adverb
                    feeling = sentiwordnet.extractFeeling(tokens[i],"r");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                    // Search as verb
                    feeling = sentiwordnet.extractFeeling(tokens[i],"v");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                }
            }
            // System.out.println(count);
        }
        catch (Exception e) {
            System.out.println("Problem found when classifying the text");
        }
//        System.out.println(count);
        // Returns "no" in case of 0
        if (count > 0)
            return "yes";
        else return "no";
    }

    /**
     * This method performs the classification of the text.
     * Algorithm: Use only ADJ, say "yes" in case of 0.
     * @return An string with "no" (negative) or "yes" (positive).
     */
    public String classifyADJY() {

        int count = 0;
        try {
            String delimiters = "\\W";
            String[] tokens = text.split(delimiters);
            String feeling = "";
            for (int i = 0; i < tokens.length; ++i) {
                // Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
                if (!tokens[i].equals("")) {
                    // Search as adjetive
                    feeling = sentiwordnet.extractFeeling(tokens[i],"a");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                }
            }
            // System.out.println(count);
        }
        catch (Exception e) {
            System.out.println("Problem found when classifying the text");
        }
//        System.out.println(count);
        // Returns "yes" in case of 0
        if (count >= 0)
            return "yes";
        else return "no";
    }

    /**
     * This method performs the classification of the text.
     * Algorithm: Use only ADJ, say "no" in case of 0.
     * @return An string with "no" (negative) or "yes" (positive).
     */
    public String classifyADJN() {

        int count = 0;
        try {
            String delimiters = "\\W";
            String[] tokens = text.split(delimiters);
            String feeling = "";
            for (int i = 0; i < tokens.length; ++i) {
                // Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
                if (!tokens[i].equals("")) {
                    // Search as adjetive
                    feeling = sentiwordnet.extractFeeling(tokens[i],"a");
                    if ((feeling != null) && (!feeling.equals(""))) {
                        switch (feeling) {
                            case "strong_positive"	: count += 2;
                                break;
                            case "positive"			: count += 1;
                                break;
                            case "negative"			: count -= 1;
                                break;
                            case "strong_negative"	: count -= 2;
                                break;
                        }
                        // System.out.println(tokens[i]+"#"+feeling+"#"+count);
                    }
                }
            }
            // System.out.println(count);
        }
        catch (Exception e) {
            System.out.println("Problem found when classifying the text");
        }
//        System.out.println(count);
        // Returns "no" in case of 0
        if (count > 0)
            return "yes";
        else return "no";
    }

}