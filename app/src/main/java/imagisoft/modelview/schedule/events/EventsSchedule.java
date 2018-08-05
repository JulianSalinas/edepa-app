package imagisoft.modelview.schedule.events;

import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.Query;

import imagisoft.model.Cloud;
import imagisoft.modelview.loaders.BaseLoader;
import imagisoft.modelview.loaders.EventsLoader;
import imagisoft.modelview.loaders.FavoritesLoader;

/**
 * Contiene todos los eventos del cronograma, incluidos
 * los favoritos y los no favoritos
 */
public class EventsSchedule extends EventsFragment {

    /**
     * Carga todos los eventos del cronograma de
     * manera asincrónica. Ésta carga se debe realizar
     * depués de obtener la lista de favoritos
     * Se instancia en {@link AdapterSchedule(Context)}
     */
    private BaseLoader eventsLoader;

    /**
     * Carga todos los key de los eventos favoritos
     * que el usuario ha marcado
     * Se instancia en {@link AdapterSchedule(Context)}
     */
    private BaseLoader favoritesLoader;

    /**
     * {@inheritDoc}
     */
    private AdapterSchedule eventsAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    protected AdapterSchedule instantiateAdapter() {
        eventsAdapter = new AdapterSchedule(getContext());
        return eventsAdapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsAdapter.connectListeners();
        setRetainInstance(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsAdapter.disconnectListeners();
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de eventos
     * @return Query
     */
    @Override
    public Query getScheduleQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("date")
                .equalTo(getDate());
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de favoritos del usuario
     * @return Query
     */
    @Override
    public Query getFavoritesQuery(){
        Cloud cloud = Cloud.getInstance();
        String uid = cloud.getAuth().getUid();
        assert uid != null;
        return cloud.getReference(Cloud.FAVORITES).child(uid);
    }

    public class AdapterSchedule extends EventsAdapter {

        private Query scheduleQuery;

        private Query favoritesQuery;

        public AdapterSchedule(Context context) {
            super(context);
            scheduleQuery = getScheduleQuery();
            favoritesQuery = getFavoritesQuery();
            eventsLoader = new EventsLoader(this);
            favoritesLoader = new FavoritesLoader(this);
            registerAdapterDataObserver(addObserver);
            registerAdapterDataObserver(removeObserver);
        }

        public void connectListeners(){
            scheduleQuery.addChildEventListener(eventsLoader);
            favoritesQuery.addChildEventListener(favoritesLoader);
        }

        public void disconnectListeners(){
            favoritesQuery.removeEventListener(favoritesLoader);
            scheduleQuery.removeEventListener(eventsLoader);
        }

        class DataObserver extends RecyclerView.AdapterDataObserver{}
        DataObserver removeObserver = new DataObserver() {
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if(events.size() <= 0) getPageListener().onPageRemoved(getDate());
        }};

        DataObserver addObserver = new DataObserver() {
        public void onItemRangeInserted(int positionStart, int itemCount) {
            getPageListener().onPageChanged(getDate());
        }};

    }

}
