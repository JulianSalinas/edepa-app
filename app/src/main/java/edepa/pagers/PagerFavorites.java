package edepa.pagers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.firebase.database.Query;

import java.util.List;
import java.util.ArrayList;

import edepa.cloud.Cloud;
import edepa.custom.EmptyFavorites;
import edepa.custom.EmptyOngoing;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.cloud.CloudChild;
import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;
import edepa.events.EventsFavorites;


public class PagerFavorites extends PagerFragment implements CloudFavorites.Callbacks {

    /**
     * Lista que contiene los key de todos los
     * eventos que el usuario ha marcado como favoritos
     * Es poblada con {@link CloudFavorites}
     */
    private List<String> favorites;

    /**
     * Lista que contiene todos los eventos
     * del cronograma
     * Es poblada con {@link CloudEvents}
     */
    protected List<Event> events;

    /**
     * Carga todos los eventos del cronograma de
     * manera asincrónica. Ésta carga se debe realizar
     * depués de obtener la lista de favoritos
     */
    private CloudEvents cloudEvents;

    /**
     * Carga todos los key de todos los eventos que
     * el usuario ha marcado como favoritos
     */
    private CloudFavorites cloudFavorites;

    /**
     * {@inheritDoc}
     * Es invocado el método {@link #onCreate(Bundle)}
     * @return PagerAdapter
     */
    @Override
    protected FragmentPagerAdapter instantiateAdapter() {
        return new PagerAdapter(this, dates) {
        protected Fragment instantiateEventsFragment() {
            return new EventsFavorites();
        }};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void inflateEmptyView() {
        String tag = "EMPTY_FAVORITES";
        Fragment frag = new EmptyFavorites();
        FragmentManager manager = getChildFragmentManager();
        manager .beginTransaction()
                .add(R.id.events_empty_view, frag, tag)
                .commit();
    }

    /**
     * {@inheritDoc}
     * Se conecta la base de datos para comenzar a
     * obtener los eventos y poder agregar la páginas
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        events = new ArrayList<>();
        favorites = new ArrayList<>();
        cloudEvents = new CloudEvents();
        cloudEvents.setCallbacks(this);
        cloudFavorites = new CloudFavorites();
        cloudFavorites.setCallbacks(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cloudFavorites.connect();
        cloudEvents.connect();
    }

    /**
     * {@inheritDoc}
     * Se desconecta de la base de datos
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cloudEvents.disconnect();
        cloudFavorites.disconnect();
    }

    /**
     * {@inheritDoc}
     * Si no existe una página para el nuevo evento, se
     * agrega una nueva
     */
    @Override
    public void addEvent(Event event) {
        if (event.getDate() != 0) {
            if (favorites.contains(event.getKey()))
                addPageIfNotExists(event);
            if (!events.contains(event)) events.add(event);
        }
    }

    /**
     * {@inheritDoc}
     * Un evento ha cambiado de fecha y se necesita actualizar
     * las páginas actuales
     */
    @Override
    public void changeEvent(Event event) {
        if (event.getDate() != 0) {
            if (favorites.contains(event.getKey()))
                addPageIfNotExists(event);
            int index = events.indexOf(event);
            if (index != -1) events.set(index, event);
        }
    }

    /**
     * {@inheritDoc}
     * @param event
     */
    @Override
    public void removeEvent(Event event) {
        // No interesa
    }

    /**
     * Al agregarse un favorito se tiene que agregar
     * una nueva página
     * @param eventKey Key del evento marcadp como favorito
     */
    @Override
    public void addFavorite(String eventKey) {
        if(!favorites.contains(eventKey)){
            favorites.add(eventKey);
            Event tmp = new Event.Builder().key(eventKey).build();
            int index = events.indexOf(tmp);
            if(index != -1) addPageIfNotExists(events.get(index));
        }
    }

    /**
     * La página es la que indica cuando borrarse, de momento
     * solo se borra de favoritos la key
     * @param eventKey Key del evento desmarcadp como favorito
     */
    @Override
    public void removeFavorite(String eventKey) {
        if(favorites.contains(eventKey)){
            favorites.remove(eventKey);
        }
    }

}
