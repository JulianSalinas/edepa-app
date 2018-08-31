package edepa.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import edepa.app.ActivityNavig;
import edepa.info.MinimapFragment;

public class EventHolderSeeMap extends RecyclerView.ViewHolder {

    private Context context;

    public EventHolderSeeMap(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> openMinimap());
    }

    public void bind(Context context){
        this.context = context;
    }

    public void openMinimap(){
        Fragment miniMap = new MinimapFragment();
        if (context instanceof ActivityNavig) {
            ActivityNavig activity = (ActivityNavig) context;
            activity.setFragmentOnScreen(miniMap, "MINIMAP");
        }
    }

}
