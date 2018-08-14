package edepa.events;

import android.util.Log;
import edepa.model.Cloud;
import edepa.model.ScheduleEvent;
import com.google.firebase.database.DatabaseReference;

/**
 * Encargada de realizar las operaciones relacionadas
 * con los eventos favoritos
 */
public class FavoriteHandler {

    /**
     * Se obtiene la referencia a los favoritos del usuario
     * @param userid: Id del usuario que marca el favorito
     * @param eventKey: Key del evento que se va a (des) marcar como favorito
     * @return Query
     */
    public static DatabaseReference
    getFavoriteReference(String userid, String eventKey){
        return Cloud.getInstance()
                .getReference(Cloud.FAVORITES)
                .child(userid).child(eventKey);
    }

    /**
     * Actualiza si un evento es favorito en la lista
     * de un usuario
     * @param event: Evento (des) marcado como favorito
     */
    public static void updateFavorite(ScheduleEvent event){
        if(!event.isFavorite()) markAsFavorite(event);
        else unmarkAsFavorite(event);
    }

    /**
     * Actualiza la base de datos agregando la clave
     * de un evento que el usuario a marcado dentro
     * de sus favoritos
     * @param event: Evento marcado como favorito
     * @see #unmarkAsFavorite(ScheduleEvent)
     */
    public static void markAsFavorite(ScheduleEvent event){
        String uid = Cloud.getInstance().getAuth().getUid();
        if (uid != null)
            getFavoriteReference(uid, event.getKey()).setValue(event.getDate());
        else {
            Log.i(FavoriteHandler.class.toString(),
            String.format("Error marking favorite for " +
                          "event %s: User is null", event));
        }
    }

    /**
     * Actualiza la base de datos borrando la clave
     * del evento que el usuario ha desmarcado de entre
     * su lista de favoritos
     * @param event: Evento desmarcado como favorito
     * @see #markAsFavorite(ScheduleEvent)
     */
    public static void unmarkAsFavorite(ScheduleEvent event){
        String uid = Cloud.getInstance().getAuth().getUid();
        if (uid != null)
            getFavoriteReference(uid, event.getKey()).removeValue();
        else {
            Log.i(FavoriteHandler.class.toString(),
            String.format("Error unmarking favorite for " +
                          "event %s: User is null", event));
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return getClass().getSimpleName();
    }

}
