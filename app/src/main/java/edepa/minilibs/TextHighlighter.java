package edepa.minilibs;

import android.text.Spannable;
import android.graphics.Typeface;
import android.text.style.StyleSpan;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.List;
import java.util.regex.MatchResult;

/**
 * Depende de {@link RegexSearcher}
 */
public class TextHighlighter {

    public static CharSequence
    highlightText(String query, String text, int color) {
        List<MatchResult> matches = RegexSearcher.autoSearch(query, text);
        if (matches.size() <= 0) return text;
        else return setSpannables(text, color, matches);
    }

    private static CharSequence
    setSpannables(String text, int color, List<MatchResult> matches){
        Spannable highlighted = new SpannableString(text);
        for (MatchResult match : matches){
            highlighted.setSpan(new ForegroundColorSpan(color),
            match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlighted.setSpan(new StyleSpan(Typeface.BOLD),
            match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }   return highlighted;
    }

}
