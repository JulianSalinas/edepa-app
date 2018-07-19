package imagisoft.misc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

public class RegexUtil {

    private static Pattern SPLIT_PATTERN = Pattern.compile("\\s+");
    private static Pattern FIRST_NAME_PATTERN = Pattern.compile("([\\w\\.@]+)");

    public static String findFirstName(String completeName){
        Matcher m = FIRST_NAME_PATTERN.matcher(completeName);
        return m.find() ? m.group(0) : null;
    }

    private static Pattern createPatternFromQuery(String query){
        Matcher m = SPLIT_PATTERN.matcher(query);
        return Pattern.compile(m.replaceAll("|"), Pattern.CASE_INSENSITIVE);
    }

    public static ArrayList<MatchResult> findAll(String query, String text){

        ArrayList<MatchResult> results = new ArrayList<>();
        Pattern p = RegexUtil.createPatternFromQuery(query);
        Matcher m = p.matcher(text);

        while(m.find()) results.add(m.toMatchResult());
        return results;
    }

}