package edepa.minilibs;

import android.text.Spanned;
import android.text.Spannable;
import android.graphics.Typeface;
import android.text.style.StyleSpan;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

/**
 * Depende de {@link RegexSearcher}
 */
public class TextHighlighter {

    public static CharSequence highlightText(String query, String text, int color) {
        List<MatchResult> matches = RegexSearcher.autoSearch(query, text);
        if (matches.size() <= 0) return text;
        else return highlightText(text, color, matches);
    }

    private static CharSequence highlightText(String text, int color, List<MatchResult> matches){
        Spannable highlighted = new SpannableString(text);
        for (MatchResult match : matches){
            highlighted.setSpan(new ForegroundColorSpan(color),
            match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlighted.setSpan(new StyleSpan(Typeface.BOLD),
            match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }   return highlighted;
    }

    public static SpannableStringBuilder decodeSpannables(String text){
        return decodeSpannables(text, RegexSearcher.DECORATED_TEXT);
    }

    private static SpannableStringBuilder decodeSpannables(String text, Pattern pattern) {

        StringBuffer sb = new StringBuffer();
        SpannableStringBuilder spannable = new SpannableStringBuilder();
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            sb.setLength(0);
            String group = matcher.group();
            String spanText = group.substring(1, group.length() - 1);
            matcher.appendReplacement(sb, spanText);
            spannable.append(sb.toString());
            int start = spannable.length() - spanText.length();
            spannable.setSpan(
                    new StyleSpan(RegexSearcher.isBoldText(group) ?
                    Typeface.BOLD : Typeface.ITALIC), start,
                    spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        sb.setLength(0);
        matcher.appendTail(sb);
        spannable.append(sb.toString());
        return spannable;
    }

}
