package imagisoft.modelview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import java.util.ArrayList;
import imagisoft.misc.DateConverter;
import imagisoft.misc.SearchNormalizer;
import imagisoft.model.ScheduleEvent;


class EventsAdapterSearch extends EventsAdapterOngoing
        implements MaterialSearchView.OnQueryTextListener {

    /**
     * Para el Logcat
     */
    private String TAG = "EventsAdapterSearch::";

    /**
     * Almacena una lista de todos los eventos de la BD
     */
    private List<ScheduleEvent> eventsHolder;

    /**
     * Almacena los resultados de la última búsqueda realizada
     */
    private List<ScheduleEvent> searchResults;

    /**
     * Se inicializa las listas necesarias
     * @param view: Fragmento que hace uso del buscador
     */
    public EventsAdapterSearch(EventsFragment view) {
        super(view);
        eventsHolder = new ArrayList<>();
        searchResults = new ArrayList<>();
    }

    /**
     * Hace la consulta a la base de datos
     */
    @Override
    public void setupReference(){
//        reference = fragment.activity
//                .getScheduleReference()
//                .orderByChild("start").getRef();
    }

    /**
     * Setea el listener si no lo está. Esto para poblar
     * la lista de eventos
     * @param recyclerView: del fragmento padre
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Log.i(TAG, "onAttachedToRecyclerView");
        super.onAttachedToRecyclerView(recyclerView);
        setEventListener();
    }

    /**
     * Remueve el listener cuando el buscador se deja de usar,
     * es decir cuando se gira la pantalla o se cierra la aplicación
     * @param recyclerView: del fragmento padre
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Log.i(TAG, "onDetachedFromRecyclerView");
        super.onDetachedFromRecyclerView(recyclerView);
        removeEventListener();
    }

    /**
     *  Obtiene el tipo de vista dependiendo si evento
     *  debe tener el encabezado indicando la fecha
     *  @param position: Posición de la vista en el adaptador
     *  @return Tipo de vista del evento
     */
    @Override
    public int getItemViewType(int position) {
        if(position == 0) return WITH_SEPARATOR;
        else return getItemViewTypeAux(position);
    }

    /**
     * Ejecuta el else de la función getItemViewType
     * @param position: Posición de la vista en el adaptador
     * @return Tipo de vista del evento
     */
    private int getItemViewTypeAux(int position){
        ScheduleEvent item = events.get(position);
        ScheduleEvent upItem = events.get(position - 1);
        String itemStart = getDateAsString(item.getStart());
        String upItemStart = getDateAsString(upItem.getStart());
        return itemStart.equals(upItemStart) ? SINGLE: WITH_SEPARATOR;
    }

    /**
     * Obtiene con base a la fecha de un evento la fecha con el
     * formato dd/mm/aa
     * @param datetime: Fecha en formato millis
     * @return Fecha con formato dd/mm/aa
     */
    @Override
    protected String getDateAsString(long datetime){
        return DateConverter.extractDate(datetime);
    }

    /**
     * Se ejecuta cuando el usuario ingresa una letra en el buscador
     * o cuando presiona buscar al terminar de escribir
     * @param query: Texto que se colca en el buscador
     * @return true si la búsqueda se realiza exitosamente
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        clearResults();
        searchResults(query);
        updateResults();
        return true;
    }

    /**
     * Ejecuta la función onQueryTextsumit cuando
     * el usuario agrega una letra a la consulta.
     * @param newText: Texto actualizado
     * @return true si la búsqueda se realiza exitosamente
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return onQueryTextSubmit(newText);
    }

    /**
     * Por cada evento se verifica si su toString
     * coincide con la búsqueda, si lo hace lo agrega
     * a la lista de resultados
     * @param query: Consutla ingresada
     */
    public void searchResults(String query){
        Log.i(TAG, "searchResults::" + query);
        query = SearchNormalizer.normalize(query);
        for (ScheduleEvent event: eventsHolder){
            String text = SearchNormalizer.normalize(event.toString());
            if(text.contains(query)) searchResults.add(event);
        }
    }

    /**
     * Después de realizar la búsqueda notifica los
     * cambios a la interfaz de usuario
     */
    public void updateResults(){
        Log.i(TAG, "updateResults");
        events = new ArrayList<>(searchResults);
        notifyDataSetChanged();
    }

    /**
     * Limpia los resultados de la búsqueda anterior
     */
    public void clearResults(){
        Log.i(TAG, "clearResults");
        searchResults = new ArrayList<>();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            eventsHolder.add(event);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null){
            event.setId(dataSnapshot.getKey());
            int index = eventsHolder.indexOf(event);
            eventsHolder.set(index, event);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            int index = eventsHolder.indexOf(event);
            eventsHolder.remove(index);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG, s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(TAG, databaseError.getDetails());
    }

}
