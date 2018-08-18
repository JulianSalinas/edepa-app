package edepa.cloud;

import edepa.model.Event;

import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Clase que se encarga de leer la lista del favoritos
 * del usuario. Esta solo debe ser usada justo después
 * de {@link CloudEvents} de lo contrario no podrá encotrar
 * cuales eventos son los que tiene que actualizar con favs
 */
public class CloudFavorites extends CloudChild {

    /**
     * Interfaz que permite actualizar los eventos favoritos
     * del usuario que está utilizando la app
     */
    private Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addFavorite(String eventKey);
        void removeFavorite(String eventKey);
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de favoritos del usuario
     * @return Query con las keys de los eventos favoritos
     */
    public static Query getFavoritesQuery(){
        Cloud cloud = Cloud.getInstance();
        String uid = cloud.getUserId();
        assert uid != null;
        return cloud.getReference(Cloud.FAVORITES).child(uid);
    }

    /**
     * Se obtiene un consulta para determinar si un evento
     * está en la lista de favoritos de un usuario
     * @param eventKey: Key del evento
     * @return DatabaseReference que si existe quiere decir que es un favorito
     */
    public static DatabaseReference getSingleFavoriteQuery(String eventKey){
        Cloud cloud = Cloud.getInstance();
        String userid = cloud.getUserId();
        assert userid != null;
        return Cloud.getInstance()
                .getReference(Cloud.FAVORITES)
                .child(userid).child(eventKey);
    }

    /**
     * Actualiza si un evento es favorito en la lista
     * de un usuario
     * @param event: Evento (des) marcado como favorito
     */
    public static void updateFavorite(Event event){
        if(!event.isFavorite()) markAsFavorite(event);
        else unmarkAsFavorite(event);
    }

    /**
     * Actualiza la base de datos agregando la clave de un
     * evento que el usuario a marcado dentro de sus favoritos
     * @param event: Evento marcado como favorito
     * @see #unmarkAsFavorite(Event)
     */
    public static void markAsFavorite(Event event){
        getSingleFavoriteQuery(event.getKey()).setValue(event.getDate());
    }

    /**
     * Actualiza la base de datos borrando la clave
     * del evento que el usuario ha desmarcado de entre
     * su lista de favoritos
     * @param event: Evento desmarcado como favorito
     * @see #markAsFavorite(Event)
     */
    public static void unmarkAsFavorite(Event event){
        getSingleFavoriteQuery(event.getKey()).removeValue();
    }

    /**
     * Se conecta a la BD para empezar a recibir la lista
     * de favoritos  del usuario
     * @see #disconnect()
     */
    public void connect(){
        getFavoritesQuery().addChildEventListener(this);
    }

    /**
     * Se desconecta de la Bd y se deja de sincronizar
     * la lista de favoritos del usurio
     * @see #connect()
     */
    public void disconnect(){
        getFavoritesQuery().removeEventListener(this);
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
        if(eventKey != null) callbacks.addFavorite(eventKey);
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
        if(eventKey != null) callbacks.removeFavorite(eventKey);
    }

}
