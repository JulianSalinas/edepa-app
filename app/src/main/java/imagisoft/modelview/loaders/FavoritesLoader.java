package imagisoft.modelview.loaders;

import java.util.List;
import com.google.firebase.database.DataSnapshot;
import imagisoft.modelview.interfaces.IEventsSubject;

/**
 * Clase que se encarga de leer la lista del favoritos
 * del usuario. Esta solo debe ser usada justo después
 * de {@link EventsLoader} de lo contrario no encontrara
 * cuales eventos son los que tiene que actualizar con favs
 */
public class FavoritesLoader extends BaseLoader {

    /**
     * Es la instancia que contiene los eventos,
     * y por tanto, cuales son favoritos
     */
    private IEventsSubject eventsSubject;

    /**
     * Constructor
     * Al pasar el adaptador como párametros se obtienen
     * los eventos que hay en el mismo
     * @param eventsSubject Objeto que implementa {@link IEventsSubject}
     */
    public FavoritesLoader(IEventsSubject eventsSubject) {
        this.eventsSubject = eventsSubject;
    }

    /**
     * {@inheritDoc}
     * Cada favorito en la lista de favoritos de un usuario
     * de la BD solo contiene el ID del evento. Al recibir este
     * ID, se busca el evento con dicho ID y se actualiza
     * @see #onChildRemoved(DataSnapshot)
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String eventKey = dataSnapshot.getKey();
        if(eventKey != null) eventsSubject.addFavorite(eventKey);
    }

    /**
     * {@inheritDoc}
     * Cuando el usuario remueve un favorito, los que se elimina
     * es ID del evento de su lista de favoritos, al hacerlo se
     * ejecuta esta función que busca el evento con dicho ID
     * y se encarga de notificar al adaptador
     * @see #onChildAdded(DataSnapshot, String)
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String eventKey = dataSnapshot.getKey();
        if(eventKey != null) eventsSubject.removeFavorite(eventKey);
    }

}
