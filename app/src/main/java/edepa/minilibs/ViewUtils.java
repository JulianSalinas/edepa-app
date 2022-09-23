package edepa.minilibs;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.content.Context;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import edepa.modelview.R;

public class ViewUtils {

    public static void setupDividedRecyclerView(
            Context context, RecyclerView view, RecyclerView.Adapter adapter){
        view.setAdapter(adapter);
        view.setLayoutManager(new SmoothLayout(context));
        view.addItemDecoration(getDecoration(context));
    }

    public static DividerItemDecoration getDecoration(Context context){
        if (context != null) {
            DividerItemDecoration decoration = new DividerItemDecoration(context, VERTICAL);
            decoration.setDrawable(context.getResources().getDrawable(R.drawable.util_decorator));
            return decoration;
        }
        return null;
    }


}
