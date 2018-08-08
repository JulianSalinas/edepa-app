package imagisoft.modelview.schedule.pagers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.firebase.database.Query;

import java.util.List;
import java.util.ArrayList;

import imagisoft.model.Cloud;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.R;
import imagisoft.modelview.loaders.BaseLoader;
import imagisoft.modelview.loaders.EventsLoader;
import imagisoft.modelview.loaders.FavoritesLoader;
import imagisoft.modelview.interfaces.IEventsSubject;
import imagisoft.modelview.schedule.events.EventsFavorites;


public class PagerFavorites extends PagerFragment implements IEventsSubject {

    /**
     * Lista que contiene los key de todos los
     * eventos que el usuario ha marcado como favoritos
     * Es poblada con {@link FavoritesLoader}
     */
    private List<String> favorites;

    /**
     * Lista que contiene todos los eventos
     * del cronograma
     * Es poblada con {@link EventsLoader}
     */
    protected List<ScheduleEvent> events;

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
    private BaseLoader favoritesLoader;

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
     * {@inheritDoc}
     * Si no existe una página para el nuevo evento, se
     * agrega una nueva
     */
    @Override
    public void addEvent(ScheduleEvent event) {
        if (event.getDate() != null) {
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
    public void changeEvent(ScheduleEvent event) {
        if (event.getDate() != null) {
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
    public void removeEvent(ScheduleEvent event) {
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
            ScheduleEvent tmp = new ScheduleEvent();
            tmp.setKey(eventKey);
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

    /**
     * {@inheritDoc}
     * Es invocado el método {@link #onCreate(Bundle)}
     * @return PagerAdapter
     */
    @Override
    protected PagerAdapter instantiateAdapter() {
        return new PagerAdapter(this) {
        protected Fragment instantiateEventsFragment() {
            return new EventsFavorites();
        }};
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventsEmptyView.setText(getString(R.string.text_without_favorites));
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
        eventsLoader = new EventsLoader(this);
        favoritesLoader = new FavoritesLoader(this);
        getFavoritesQuery().addChildEventListener(favoritesLoader);
        getEventsQuery().addChildEventListener(eventsLoader);
    }

    /**
     * {@inheritDoc}
     * Se desconecta de la base de datos
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getEventsQuery().removeEventListener(eventsLoader);
        getFavoritesQuery().removeEventListener(favoritesLoader);
    }

}
