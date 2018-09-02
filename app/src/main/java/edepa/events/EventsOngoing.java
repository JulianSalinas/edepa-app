package edepa.events;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import edepa.cloud.CloudEvents;
import edepa.custom.EmptyOngoing;
import edepa.minilibs.TimeConverter;

import edepa.modelview.R;
import edepa.model.Event;
import edepa.model.Preferences;
import edepa.cloud.CloudFavorites;

import static edepa.model.Preferences.UPDATE_DELAY;


public class EventsOngoing
        extends EventsFragment implements CloudFavorites.Callbacks {

    /**
     * View que se coloca cuando no hay eventos
     */
    @BindView(R.id.events_empty_view)
    View eventsEmptyView;

    /**
     * Indica si {@link #runnable} debe seguir
     * ejecutandosé
     */
    private Boolean started;

    /**
     * Es el encargado iniciar y detener la ejecución
     * de {@link #runnable}
     */
    private Handler handler;

    /**
     * Ejecuta un thread que solicita los eventos
     * a la base de datos y filtran solo los que
     * se encuentran en curso (ongoing)
     */
    private Runnable runnable;

    /**
     * Indica cada cuánto se debe realizar una
     * actualización en la función {@link #startDelayed()}
     * Este valor es obtenido de las preferencias
     * de lo contrario usa 1 minuto por defecto
     */
    private Integer updateDelay;

    /**
     * Adaptador que contiene los eventos que
     * están en curso
     */
    protected OngoingAdapter eventsAdapter;

    private CloudFavorites cloudFavorites;

    @Override
    public long getDate() {
        long time = System.currentTimeMillis();
        return TimeConverter.atStartOfDay(time);
    }

    /**
     * {@inheritDoc}
     * @return Retorna el adaptador que está
     * usando este fragmento
     */
    @Override
    protected OngoingAdapter instantiateAdapter() {
        eventsAdapter = new OngoingAdapter();
        return eventsAdapter;
    }

    protected void inflateEmptyView(){
        String tag = "EMPTY_ONGOING";
        Fragment frag = new EmptyOngoing();
        FragmentManager manager = getChildFragmentManager();
        manager .beginTransaction()
                .replace(R.id.events_empty_view, frag, tag)
                .commit();
    }

    /**
     * {@inheritDoc}
     * Inicializa las variables que se necesitan para
     * ejecutar las actualizaciones del contenido
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        runnable = this::loop;
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     * Inicia por primera vez la ejecución de {@link #runnable}
     * y configura el texto que se debe mostrar en caso
     * de que no haya ningún evento en curso
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setUpdateDelay();
        startRunnable();
        super.onActivityCreated(savedInstanceState);
        inflateEmptyView();
    }

    /**
     * Al destruir la vista no es necesario que el fragmento
     * se siga actualizando porque el usuario no estará
     * viendo la pantalla
     */
    @Override
    public void onDestroyView() {
        stopRunnable();
        super.onDestroyView();
    }

    /**
     * Coloca el valor de {@link #updateDelay} que indica
     * cada cuánto se debe realizar una actualización
     * Si el valor no se encuentra en las preferencias
     * se escribe 1 minuto por defecto
     */
    public void setUpdateDelay(){

        updateDelay = 60000;

        // La preferencia se guarda como string en el fragmento
        // diseñado para tal fin
        String delay = Preferences.getStringPreference(activity, UPDATE_DELAY);

        // Es casi imposible que suceda la excepción puesto que
        // no se le da la posibilidad al usuario de ingresarlo mal
        // Pero los desarrolladores podría ncambiar algo a futuro
        try {
            updateDelay = Integer.valueOf(delay);
            Log.i(toString(), String.format(
                    "setUpdateDelay -> updateDelay %d", updateDelay));
        }
        catch (NumberFormatException e){
            Log.e(toString(), "setUpdateDelay -> " + e.getMessage());
        }

    }

    /**
     * Es la función ejecutada por {@link #runnable}
     * Es invocada cada 60000 milisegundos, es decir,
     * una vez cada minuto
     */
    public void loop(){

        if (cloudFavorites == null) {
            cloudFavorites = new CloudFavorites();
            cloudFavorites.setCallbacks(this);
            cloudFavorites.connect();
        }

        Query query = CloudEvents.getEventsQueryUsingDate(getDate());
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(eventsAdapter);

    }

    /**
     * Ejecuta la función loop sin nigún tipo
     * de retraso. Es usada en {@link #onActivityCreated(Bundle)}
     * pues se necesita mostrar la información inmediatamente
     */
    public void startRunnable(){
        started = true;
        handler.post(runnable);
    }

    /**
     * Es ejecutada al terminar una actualización
     * para que tiempo después se realice otra vez
     * Esta actualización se realiza en el {@link #eventsAdapter}
     */
    private void startDelayed(){
        started = true;
        handler.postDelayed(runnable, updateDelay);
    }

    /**
     * Detiene el {@link #runnable} que se está encargando
     * de actualizar
     */
    public void stopRunnable(){
        started = false;
        handler.removeCallbacks(runnable);
    }

    /**
     * Obtiene el filtro que indica si un evento
     * está o no en curso
     * @param event Evento del cronograma
     * @return True si el evento está en curso
     */
    public boolean getTimeFilter(Event event){
        long currentTime = System.currentTimeMillis();
        boolean filter = event.getStart() <= currentTime;
        Log.i("ongoing",
                String.format("Filter applied %s <= %s <= %s",
                TimeConverter.longToString(event.getStart()),
                TimeConverter.longToString(currentTime),
                TimeConverter.longToString(event.getEnd())));
        return filter && currentTime <= event.getEnd();
    }

    /**
     * Actualiza la interfaz colocando los eventos en
     * pantalla o por contrario, sino existen eventos
     * colocar un texto que lo indique
     * @param isEmpty: True si no hay eventos en curso
     */
    public void updateInterface(boolean isEmpty){
        eventsRecycler.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        eventsEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        String s = TimeConverter.longToString(System.currentTimeMillis());
        Log.i(toString(), String.format("Update complete at %s", s));
        // showStatusMessage("Events updated");
    }

    /**
     * Adaptador que contiene los eventos que se
     * encuentran en curso. Recupera todos los eventos
     * de la base de datos y aplica la función
     * {@link #getTimeFilter(Event)} para tal fin
     */
    public class OngoingAdapter
            extends EventsAdapter implements ValueEventListener {

        /**
         * {@inheritDoc}
         */
        public OngoingAdapter() {
            super(EventsOngoing.this.events);
        }

        /**
         * {@inheritDoc}
         * En una actualización se borran todos los eventos
         * y se recuperan de la base de datos aquellos que
         * cumplen con la función {@link #getTimeFilter(Event)}
         */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            clearEvents();
            for(DataSnapshot snapshot: dataSnapshot.getChildren())
                retrieveEvent(snapshot);
            notifyUpdate();
            if(started) startDelayed();
        }

        /**
         * Convierte el dataSnapshot en un evento y clasifica
         * si el evento se encuentra en curso. si lo esta procede
         * a agregarlo a la lista de eventos
         * @param dataSnapshot: Obtenido del dataSnapshot
         * de {@link #onDataChange(DataSnapshot)}
         */
        private void retrieveEvent(DataSnapshot dataSnapshot){
            Event event = dataSnapshot.getValue(Event.class);
            if (event != null ){
                event.setKey(dataSnapshot.getKey());
                Log.i("ongoing -> Retriving ", event.getKey());
                 if(getTimeFilter(event)) addEvent(event);
            }
        }

        /**
         * Borra todos los eventos actualmente existentes
         * Es utilizada antes de realizar una actualización
         */
        private void clearEvents(){
            int size = events.size();
            if(size > 0) events.clear();
        }

        /**
         * Notifica que la recuperación de los datos
         * ha finalizado e invoca al fragment para actualizar
         * la interfaz
         */
        private void notifyUpdate(){
            notifyDataSetChanged();
            boolean isEmpty = events.size() <= 0;
            updateInterface(isEmpty);
        }

        /**
         * {@inheritDoc}
         * Ha habido un error en la actualización por
         * los que se procede a mostrar el error en pantalla
         * @param databaseError: Contiene la información del
         *                       error por lo que se registra
         */
        @Override
        public void onCancelled(DatabaseError databaseError) {
            events.clear();
            notifyUpdate();
            Log.e("OngoingAdapter", databaseError.getMessage());
        }

    }

}
