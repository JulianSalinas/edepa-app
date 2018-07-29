package imagisoft.modelview.schedule;

import butterknife.BindView;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import imagisoft.misc.DateConverter;
import imagisoft.modelview.R;
import imagisoft.modelview.views.RecyclerAdapter;
import imagisoft.modelview.views.SmoothLayout;
import imagisoft.modelview.views.RecyclerFragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;


public abstract class EventsFragment
        extends RecyclerFragment implements IEventsSubject {

    /**
     * Se colocan los eventos de manera visual
     */
    @BindView(R.id.events_view)
    RecyclerView eventsRV;

    /**
     * Adaptador para {@link #eventsRV}
     */
    private EventsAdapter eventsVA;

    protected long date;

    public long getDate(){
        return date;
    }

    protected String tagF;

    public String getTagF(){
        return tagF;
    }

    private Bundle pausedState;

    protected IEventsListener pagerListener;

    @Override
    protected RecyclerView getRecyclerView() {
        return eventsRV;
    }

    @Override
    protected RecyclerAdapter getViewAdapter() {
        return eventsVA;
    }

    @Override
    public void setListener(IEventsListener pagerListener){
        this.pagerListener = pagerListener;
    }

    public IEventsListener getListener(){
        return this.pagerListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {

            if (args.containsKey("date"))
                date = args.getLong("date");

            if (args.containsKey("tag"))
                tagF = args.getString("tag");

            Log.i(toString(), "date: " + DateConverter.extractDate(date));
            Log.i(toString(), "tag: " + tagF);

        }

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

    /**
     * Se prepara el adaptador para recibir
     * nuevos eventos
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
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
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
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


    public static class Schedule extends EventsFragment{

        @Override
        protected EventsAdapter getAdapter() {
            return new AdapterSchedule(this);
        }

    }

    public static class Favorites extends EventsFragment{

        @Override
        protected EventsAdapter getAdapter() {
            return new AdapterFavorites(this);
        }

    }

    public static class Ongoing extends EventsFragment{

        @Override
        public void setupActivityView() {
            super.setupActivityView();
            setActiveTabbedMode(true);
        }

        @Override
        protected EventsAdapter getAdapter() {
            return new AdapterOngoing(this);
        }

    }

}