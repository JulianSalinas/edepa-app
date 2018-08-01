package imagisoft.modelview.schedule;

import butterknife.BindView;

import android.os.Bundle;
import android.util.Log;

import imagisoft.misc.DateConverter;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;
import imagisoft.modelview.views.SmoothLayout;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.widget.TextView;


public abstract class EventsFragment
        extends MainFragment implements IEventsSubject {

    /**
     * Se colocan los eventos de manera visual
     */
    @BindView(R.id.events_view)
    RecyclerView eventsRV;

    @BindView(R.id.events_empty_view)
    TextView eventsEmpty;

    /**
     * Adaptador para {@link #eventsRV}
     */
    private EventsAdapter eventsVA;

    /**
     * Solo eventos de esta fecha deben estar en
     * este fragmento
     */
    protected long date;

    public long getDate(){
        return date;
    }

    /**
     * El listener se da cuanta de los cambios que son hechos
     * en el fragmento
     */
    protected IEventsListener listener;

    @Override
    public IEventsListener getListener(){
        return this.listener;
    }

    @Override
    public void setListener(IEventsListener listener){
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.schedule_view;
    }

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("date")) {
            date = args.getLong("date");
            Log.i(toString(), "date: " + DateConverter.extractDate(date));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Se prepara el adaptador para recibir
     * nuevos eventos
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void setupAdapter(){
        if(eventsVA == null){
            eventsVA = instantiateAdapter();
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

    protected abstract EventsAdapter instantiateAdapter();


    public static class Schedule extends EventsFragment{

        @Override
        protected EventsAdapter instantiateAdapter() {
            return new AdapterSchedule(this);
        }

    }

    public static class Favorites extends EventsFragment{

        @Override
        protected EventsAdapter instantiateAdapter() {
            return new AdapterFavorites(this);
        }

    }

}