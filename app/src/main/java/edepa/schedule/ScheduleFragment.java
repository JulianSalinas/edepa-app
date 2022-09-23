package edepa.schedule;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;
import edepa.custom.CustomFragment;
import edepa.model.Event;
import edepa.modelview.R;


public abstract class ScheduleFragment extends CustomFragment
        implements CloudEvents.Callbacks, CloudFavorites.Callbacks {

    /**
     * Se colocan los eventos de manera visual
     * Los eventos los obtiene de {@link #scheduleAdapter}
     */
    @BindView(R.id.events_recycler)
    RecyclerView eventsRecycler;

    /**
     * Solo eventos de esta fecha deben estar en
     * este fragmento. Si no existe la fecha; se colocan
     * todos los eventos sin importar el día
     */
    protected long date;

    public long getDate(){
        return date;
    }

    /**
     * Lista que contiene los key de todos los
     * eventos que el usuario ha marcado como favoritos
     * Es poblada con {@link CloudFavorites}
     */
    protected List<String> favorites;

    /**
     * Lista que contiene todos los eventos
     * del cronograma
     * Es poblada con {@link CloudEvents}
     */
    protected List<Event> events;

    /**
     * El {@link IPageListener} se da cuenta de los
     * cambios que son hechos en el fragmento
     */
    protected IPageListener pageListener;

    /**
     * Adaptador para {@link #eventsRecycler}
     * y así colocar los eventos de forma visual
     */
    protected ScheduleAdapter scheduleAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.schedule_screen;
    }

    /**
     * {@inheritDoc}
     * Se obtienen la fecha que es pasada como argumento
     * y se instancia el adaptador si es necesario
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se obtiene la fecha que deben de tener los
        // eventos presentes en el fragmento
        Bundle args = getArguments();
        if (args != null && args.containsKey("date"))
            date = args.getLong("date");

        if(scheduleAdapter == null) {
            events = new ArrayList<>();
            favorites = new ArrayList<>();
            scheduleAdapter = instantiateAdapter();
        }

    }

    /**
     * {@inheritDoc}
     * Se configura el {@link #eventsRecycler}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventsRecycler.setHasFixedSize(true);
        eventsRecycler.setAdapter(scheduleAdapter);
        eventsRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * Se añade un nuevo mensaje en la lista
     * @param event Evento por agregar
     */
    @Override
    public void addEvent(Event event){
        int index = events.indexOf(event);
        if(index == -1){
            String key = event.getKey();
            index = findIndexToAddEvent(event);
            events.add(index, event);
            setFavoriteEvent(key, favorites.contains(key));
            scheduleAdapter.notifyItemInserted(index);
        }
    }

    /**
     * Busca en que posición debe agregarse un nuevo
     * evento. Invocada por {@link #addEvent(Event)}
     * @param event Evento por agregar
     * @return Posición donde se debe agregar el evento
     * TODO: Usar algoritmo más eficiente
     */
    private int findIndexToAddEvent(Event event){
        int index = 0;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getStart() <= event.getStart()) index += 1;
            else break;
        }   return index;
    }

    /**
     * Se utiliza cuando la propiedad de algún evento
     * ha sido cambiada
     * @param event Mensaje por cambiar
     */
    @Override
    public void changeEvent(Event event){
        int index = events.indexOf(event);
        if (index != -1) {

            // Solo se actualiza la vista si el cambio
            // fue cualquier cosa excepto la cantidad de favoritos
            Event before = events.get(index);
            boolean favoritesChanged = event.getFavorites() != before.getFavorites();

            String key = event.getKey();
            events.set(index, event);
            setFavoriteEvent(key, favorites.contains(key));

            if(!favoritesChanged)
                scheduleAdapter.notifyItemChanged(index);
        }
    }

    /**
     * Se remueve un evento. Es utilizada cuando el
     * evento es borrado de la BD o cuando el evento
     * cambia de fecha
     */
    @Override
    public void removeEvent(Event event){
        int index = events.indexOf(event);
        if (index != -1) {
            events.remove(index);
            scheduleAdapter.notifyItemRemoved(index);
        }
    }

    /**
     * Agrega el key del evento que usuario ha marcado
     * como favorito. Además busca el evento real y lo
     * modifica como favorito
     * @param eventKey Key del evento marcado como fav
     */
    @Override
    public void addFavorite(String eventKey) {
        if(!favorites.contains(eventKey)) {
            favorites.add(eventKey);
            setFavoriteEvent(eventKey, true);
        }
    }

    /**
     * Remueve el key del evento que usuario ha desmarcado
     * como favorito. Además busca el evento real y lo actualiza
     * @param eventKey Key del evento marcado como fav
     */
    @Override
    public void removeFavorite(String eventKey) {
        if(favorites.contains(eventKey)) {
            favorites.remove(eventKey);
            setFavoriteEvent(eventKey, false);
            scheduleAdapter.notifyItemChanged(getFavoriteIndex(eventKey));
        }
    }

    /**
     * Usada internamente por los métodos {@link #addFavorite(String)}
     * y {@link #removeFavorite(String)} para setear el nuevo
     * valor del evento que ha sido marcado o desmarcado
     * @param eventKey: Key del evento marcado o desmarcado como fav
     * @param isFavorite: True si el evento se encuentra entre
     *                    los favoritos del usuario
     */
    private void setFavoriteEvent(String eventKey, boolean isFavorite){
        int index = getFavoriteIndex(eventKey);
        if(index != -1) events.get(index).setFavorite(isFavorite);
    }

    /**
     * A partir del key de un evento encuentra la posición del
     * evento como tal. Es invocada internamente
     * por el metodo {@link #setFavoriteEvent(String, boolean)}
     * @param eventKey: Key del evento buscado
     * @return Posición del evento en {@link #events}
     */
    protected int getFavoriteIndex(String eventKey){
        Event temp = new Event.Builder().key(eventKey).build();
        return events.indexOf(temp);
    }

    /**
     * Evita a las subclases tener que hacer la revisión
     * de nullpointer en el método {@link #onCreate(Bundle)}
     */
    protected abstract ScheduleAdapter instantiateAdapter();

}