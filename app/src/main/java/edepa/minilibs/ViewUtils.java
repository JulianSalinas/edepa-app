package edepa.minilibs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;

import edepa.modelview.R;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

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
