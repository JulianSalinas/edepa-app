package edepa.minilibs;

import android.graphics.Color;
import android.content.Context;
import android.content.res.Resources;

import edepa.modelview.R;
import java.util.List;
import java.util.ArrayList;


public class ColorGenerator {

    private Context context;
    private List<Integer> colors;

    protected int getResource(){
        return R.array.material_colors;
    }

    public ColorGenerator(Context context){
        this.context = context;
        this.colors = new ArrayList<>();
        this.loadColorsFromResource();
    }

    private void loadColorsFromResource() {
        Resources r = context.getResources();
        for (String hex_color : r.getStringArray(getResource()))
            colors.add(Color.parseColor(hex_color));
    }

    public int getColor(Object key) {
        int value = Math.abs(key.hashCode());
        return colors.size() > 0 ? colors.get(value % colors.size()) : 0;
    }

}