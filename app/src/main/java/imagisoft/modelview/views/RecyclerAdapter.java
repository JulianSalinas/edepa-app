package imagisoft.modelview.views;

import android.support.v7.widget.RecyclerView;

public abstract class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
