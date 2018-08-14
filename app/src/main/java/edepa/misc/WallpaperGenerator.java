package edepa.misc;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import edepa.model.Searcher;
import edepa.modelview.R;

public class WallpaperGenerator {

    private Context context;
    private List<Drawable> drawables;

    public WallpaperGenerator(Context context) {
        this.context = context;
        this.drawables = new ArrayList<>();
    }

    public Drawable getWallpaper(String query){
       return context.getResources().getDrawable(parseText(query));
    }

    private int parseText(String text){

        text = Searcher.normalize(text);

        String query = "bibli figuere ferrer";
        if(Searcher.autoSearch(query, text).size() > 0)
            return R.drawable.tec_biblioteca;

        query = "(centro)?(\\s|\\w|d)*artes?";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_centro_artes;

        query = "((edifi?c?i?o?)|(audito?r?i?o))\\s+a(4|5)";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_edificio_a5;

        query = "((edifi?c?i?o?)|(audito?r?i?o))\\s+c1";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_edificio_c1;

        query = "((edifi?c?i?o?)|(audito?r?i?o))\\s+d";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_edificio_d;

        query = "((edifi?c?i?o?)|(audito?r?i?o))\\s+f2";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_edificio_f2;

        query = "((labo?r?a?t?o?r?i?o)|(laimi?))\\s*1?";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_laimi_1;

        query = "((labo?r?a?t?o?r?i?o)|(laimi?))\\s*2?";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_laimi_2;

        query = "(((labo?r?a?t?o?r?i?o)|(laimi?))\\s+b3\\s*10?)|((tierr\\w+)|(medi\\w+))";
        if(Searcher.customSearch(query, text).size() > 0)
            return R.drawable.tec_tierra_media;

        return R.color.cool_orange_light;

    }

}
