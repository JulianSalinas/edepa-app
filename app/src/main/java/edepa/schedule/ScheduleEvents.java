package edepa.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;

/**
 * Contiene todos los eventos del cronograma, incluidos
 * los favoritos y los no favoritos
 */
public class ScheduleEvents extends ScheduleFragment {

    protected int eventsAmount = 0;

    /**
     * Carga todos los eventos del cronograma de
     * manera asincrónica. Ésta carga se debe realizar
     * depués de obtener la lista de favoritos
     */
    protected CloudEvents cloudEvents;

    /**
     * Carga todos los key de todos los eventos que
     * el usuario ha marcado como favoritos
     */
    protected CloudFavorites cloudFavorites;

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
     * {@link ScheduleFragment}. Deja una referencia en
     * este fragmento para evitar realizar el cast
     * @return AdapterSchedule
     */
    @Override
    protected ScheduleAdapter instantiateAdapter() {
        eventsAdapter = new AdapterSchedule();
        return eventsAdapter;
    }

    /**
     * {@inheritDoc}
     * Al crearse el fragmeno, se conecta el adminPermissionListener
     * para comenzar a recibir los eventos de la BD
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se obtiene el IPageListener que en este caso
        // es un PagerFragment que implementa IPageListener
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof IPageListener)
            pageListener = (IPageListener) fragment;

        // Deben instanciarse antes de conectar
        cloudEvents = new CloudEvents();
        cloudFavorites = new CloudFavorites();

        cloudEvents.setCallbacks(this);
        cloudFavorites.setCallbacks(this);

        // Se obtiene la cantidad de eventos para
        // saber cuando termina la carga de datos inicial
        getEventsQuery()
        .addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventsAmount = (int) dataSnapshot.getChildrenCount();
            connectListeners();
            Log.i(toString(), "Retriving events amount");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(toString(), databaseError.getMessage());
        }});

    }

    /**
     * {@inheritDoc}
     * Al destruirse el fragmento se borra el adminPermissionListener
     * para evitar tener un duplicado
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectListeners();
    }

    /**
     * Pone a la escucha los adminPermissionListener encargados de
     * agregar eventos y colocar los favoritos. Es
     * invocada en {@link #onCreate(Bundle)}
     * @see #disconnectListeners()
     */
    public void connectListeners(){
        cloudFavorites.connect();
        cloudEvents.connect(getEventsQuery());
    }

    /**
     * Remueve los listeners encargados de agregar
     * los eventos y de colocar los favoritos. Es
     * invocada en {@link #onDestroy()}
     */
    public void disconnectListeners(){
        cloudFavorites.disconnect();
        cloudEvents.disconnect(getEventsQuery());
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de eventos para un fecha
     * en específico (que es pasada al fragmento como arg)
     * @return Query
     */
    public Query getEventsQuery(){
        return CloudEvents.getEventsQueryUsingDate(getDate());
    }

    /**
     * Cada vez que se remueve un evento se revisa si quedan
     * más eventos, de lo contrario se avisa a IPageListener
     * que debe remover este fragmento
     */
    public RecyclerView.AdapterDataObserver getDataObserver() {
        return new RecyclerView.AdapterDataObserver() {

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if(events.size() <= 0 && pageListener != null)
                    pageListener.onPageRemoved(getDate());
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(pageListener != null && eventsAmount-- <= 0)
                    pageListener.onPageChanged(getDate());
            }

        };
    }

    /**
     * Clase que modifica la lista de eventos y de favoritos
     * del fragmento {@link ScheduleEvents}
     */
    public class AdapterSchedule extends ScheduleAdapter {

        public AdapterSchedule() {
            super(ScheduleEvents.this.events);
            registerAdapterDataObserver(getDataObserver());
        }

    }

}
