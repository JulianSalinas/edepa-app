package imagisoft.modelview;

import butterknife.BindView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public abstract class EventsFragment extends MainActivityFragment {

    /**
     * Se colocan los eventos de manera visual
     */
    @BindView(R.id.events_view)
    RecyclerView eventsRV;

    /**
     * Adaptador para eventsRV
     */
    protected EventsAdapter eventsVA;

    @Override
    public void setupResource() {
        this.resource = R.layout.schedule_view;
    }

    @Override
    public void setupActivityView() {
        setupAdapter();
        setupEventsView();
    }

    public void setupEventsView(){
        eventsRV.setHasFixedSize(true);
        eventsRV.setAdapter(eventsVA);
        eventsRV.setItemAnimator(new DefaultItemAnimator());
        eventsRV.setLayoutManager(new SmoothLayout(activity));
    }

    protected abstract void setupAdapter();

}