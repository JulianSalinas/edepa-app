package imagisoft.modelview.schedule;

import butterknife.BindView;

import android.util.Log;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.ActivityFragment;
import imagisoft.modelview.views.SmoothLayout;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public abstract class EventsFragment extends ActivityFragment implements IEventsSubject {

    /**
     * Se colocan los eventos de manera visual
     */
    @BindView(R.id.events_view)
    RecyclerView eventsRV;

    /**
     * Adaptador para {@link #eventsRV}
     */
    private EventsAdapter eventsVA;

    public abstract long getDate();

    protected IEventsListener pagerListener;

    @Override
    public void setListener(IEventsListener pagerListener){
        this.pagerListener = pagerListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.schedule_view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupActivityView() {
        setToolbarText(R.string.app_name);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public abstract void bindArguments();

    /**
     * Se prepara el adaptador para recibir
     * nuevos eventos
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupAdapter(){
        if(eventsVA == null){
            eventsVA = getAdapter();
            Log.i(toString(), "setupAdapter()");
        }
    }

    /**
     * Se prepara el recycler view para poder
     * colocar en pantalla los eventos
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void setupRecyclerView(){
        if(eventsRV.getAdapter() == null) {
            eventsRV.setHasFixedSize(true);
            eventsRV.setAdapter(eventsVA);
            eventsRV.setItemAnimator(new DefaultItemAnimator());
            eventsRV.setLayoutManager(new SmoothLayout(activity));
            Log.i(toString(), "setupRecyclerView()");
        }
    }

    protected abstract EventsAdapter getAdapter();

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}