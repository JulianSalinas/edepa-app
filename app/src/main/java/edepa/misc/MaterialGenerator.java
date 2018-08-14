package edepa.misc;

import android.graphics.Color;
import android.content.Context;
import android.content.res.Resources;

import edepa.modelview.R;
import java.util.List;
import java.util.ArrayList;


public class MaterialGenerator {

    private Context context;
    private List<Integer> colors;

    public MaterialGenerator(Context context){
        this.context = context;
        this.colors = new ArrayList<>();
        this.loadColorsFromResource();
    }

    private void loadColorsFromResource() {
        Resources res = context.getResources();
        String hex_colors[] = res.getStringArray(R.array.material_colors);
        for (String hex_color : hex_colors)
            colors.add(Color.parseColor(hex_color));
    }

    public int getColor(Object key) {
        int value = Math.abs(key.hashCode());
        return colors.size() > 0 ? colors.get(value % colors.size()) : 0;
    }

}