package imagisoft.modelview.views;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import imagisoft.modelview.activity.ActivityFragment;

public abstract class RecyclerFragment extends ActivityFragment{

    protected Bundle pausedState;

    protected abstract RecyclerView getRecyclerView();

    protected abstract RecyclerAdapter getViewAdapter();

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void savePausedInstanceState(){
        pausedState = new Bundle();
        Parcelable scrollPosition = getRecyclerView()
                .getLayoutManager().onSaveInstanceState();
        pausedState.putParcelable("scrollPosition", scrollPosition);
        Log.i(toString(), "savePausedInstanceState(Bundle)");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onPausedViewStateRestored(){
        if(pausedState != null) {
            Parcelable scrollPosition = pausedState
                    .getParcelable("scrollPosition");
            getRecyclerView()
                    .getLayoutManager()
                    .onRestoreInstanceState(scrollPosition);
            Log.i(toString(), "onPausedViewStateRestored(Bundle)");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int position = ((LinearLayoutManager) getRecyclerView()
                .getLayoutManager()).findFirstVisibleItemPosition();
        View startView = getRecyclerView().getChildAt(0);

        int top = (startView == null) ? 0 :
                (startView.getTop() - getRecyclerView().getPaddingTop());

        outState.putInt("position", position);
        outState.putInt("top", top);

        Log.i(toString(), "onSaveInstanceState(Bundle)");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {

            int position = savedInstanceState.getInt("position");
            int top = savedInstanceState.getInt("top");

            getViewAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                public void onItemRangeInserted(int positionStart, int itemCount) {

                    ((LinearLayoutManager)
                            getRecyclerView().getLayoutManager()).scrollToPositionWithOffset(position, top);
                }
            });
        }
        Log.i(toString(), "onViewStateRestored(Bundle)");
    }

}
