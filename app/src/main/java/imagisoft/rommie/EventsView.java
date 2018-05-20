package imagisoft.rommie;

import android.os.Bundle;
import butterknife.BindView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public abstract class EventsView extends MainActivityFragment {

    /**
     * Se colocan los eventos de manera visual
     */
    @BindView(R.id.events_view)
    RecyclerView eventsView;

    /**
     * Adaptador para eventsView
     */
    protected EventsViewAdapter eventsViewAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.schedule_view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupAdapter();
        setupEventsView();
    }

    public void setupEventsView(){
        eventsView.setHasFixedSize(true);
        eventsView.setAdapter(eventsViewAdapter);
        eventsView.setItemAnimator(new DefaultItemAnimator());
        eventsView.setLayoutManager(new SmoothLayout(this.getActivity()));
    }

    protected abstract void setupAdapter();

}