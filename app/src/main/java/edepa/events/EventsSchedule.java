package edepa.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edepa.model.Cloud;
import edepa.loaders.BaseLoader;
import edepa.loaders.EventsLoader;
import edepa.loaders.FavoritesLoader;
import edepa.interfaces.IPageListener;
import edepa.pagers.PagerFragment;

/**
 * Contiene todos los eventos del cronograma, incluidos
 * los favoritos y los no favoritos
 */
public class EventsSchedule extends EventsFragment {

    private int eventsAmount = 0;

    /**
     * Carga todos los eventos del cronograma de
     * manera asincrónica. Ésta carga se debe realizar
     * depués de obtener la lista de favoritos
     */
    private BaseLoader eventsLoader;

    /**
     * Carga todos los key de todos los eventos que
     * el usuario ha marcado como favoritos
     */
    protected BaseLoader favoritesLoader;

    /**
     * Es donde se cargan los eventos y se colocan
     * como favoritos. Además avisa al páginador (si lo hay)
     * cuando alguno de los eventos es ingresado o
     * si el último evento ha sido eliminado
     * @see AdapterSchedule
     */
    protected AdapterSchedule eventsAdapter;

    /**
     * Instancia el adaptador necesario por la superclase
     * {@link EventsFragment}. Deja una referencia en
     * este fragmento para evitar realizar el cast
     * @return AdapterSchedule
     */
    @Override
    protected AdapterSchedule instantiateAdapter() {
        eventsAdapter = new AdapterSchedule();
        return eventsAdapter;
    }

    /**
     * {@inheritDoc}
     * Al crearse el fragmeno, se conecta el listener
     * para comenzar a recibir los eventos de la BD
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se obtiene el listener que en este caso
        // es un PagerFragment que implementa IPageListener
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof IPageListener)
            pageListener = (IPageListener) fragment;

        // Deben instanciarse antes de conectar
        eventsLoader = new EventsLoader(this);
        favoritesLoader = new FavoritesLoader(this);

        // Se obtiene la cantidad de eventos para
        // saber cuando termina la carga de datos inicial
        getEventsQuery()
        .addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventsAmount = (int) dataSnapshot.getChildrenCount();
            eventsAdapter.connectListeners();
            Log.i(toString(), "Retriving events amount");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(toString(), databaseError.getMessage());
        }});

    }

    /**
     * {@inheritDoc}
     * Al destruirse el fragmento se borra el listener
     * para evitar tener un duplicado
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsAdapter.disconnectListeners();
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de eventos para un fecha
     * en específico (que es pasada al fragmento como arg)
     * @return Query
     */
    public Query getEventsQuery(){
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
    public Query getFavoritesQuery(){
        Cloud cloud = Cloud.getInstance();
        String uid = cloud.getAuth().getUid();
        assert uid != null;
        return cloud.getReference(Cloud.FAVORITES).child(uid);
    }

    /**
     * Clase que modifica la lista de eventos y de favoritos
     * del fragmento {@link EventsSchedule}
     */
    public class AdapterSchedule extends EventsAdapter {

        /**
         * Contructor de {@link AdapterSchedule}
         */
        public AdapterSchedule() {
            super(EventsSchedule.this);
            this.events = EventsSchedule.this.events;
            registerAdapterDataObserver(addObserver);
            registerAdapterDataObserver(removeObserver);
        }

        /**
         * Pone a la escucha los listener encargados de
         * agregar eventos y colocar los favoritos. Es
         * invocada en {@link #onCreate(Bundle)}
         * @see #disconnectListeners()
         */
        public void connectListeners(){
            getFavoritesQuery().addChildEventListener(favoritesLoader);
            getEventsQuery().addChildEventListener(eventsLoader);
        }

        /**
         * Remueve los listeners encargados de agregar
         * los eventos y de colocar los favoritos. Es
         * invocada en {@link #onDestroy()}
         */
        public void disconnectListeners(){
            getFavoritesQuery().removeEventListener(favoritesLoader);
            getEventsQuery().removeEventListener(eventsLoader);
        }

        /**
         * Cada vez que se remueve un evento se revisa si quedan
         * más eventos, de lo contrario se avisa al fragmento
         * padre {@link PagerFragment}
         * que debe remover este fragmento
         */
        class DataObserver extends RecyclerView.AdapterDataObserver{}
        DataObserver removeObserver = new DataObserver() {
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if(events.size() <= 0 && pageListener != null)
                pageListener.onPageRemoved(getDate());
        }};

        /**
         * Cada vez que se agrega un evento se avisa al fragmento
         * padre {@link PagerFragment}
         * para que coloque la página que ha sido modificada
         */
        DataObserver addObserver = new DataObserver() {
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if(pageListener != null && eventsAmount-- <= 0)
                pageListener.onPageChanged(getDate());
        }};

    }

}
