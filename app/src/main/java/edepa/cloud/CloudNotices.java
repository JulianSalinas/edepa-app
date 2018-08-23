package edepa.cloud;

import edepa.model.Notice;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;


public class CloudNotices extends CloudChild {

    /**
     * Interfaz que permite recibir las noticias
     * actualizadas de la BD
     */
    private Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addNotice(Notice notice);
        void changeNotice(Notice notice);
        void removeNotice(Notice notice);
    }

    /**
     * Obtiene una consulta que contiene las
     * últimas 50 noticias ordenadas por fecha de publicación
     * @return Query con las últimas noticias
     */
    public static Query getNoticesQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .orderByChild("time")
                .limitToLast(50);
    }

    /**
     * Obtiene una referencia a la base de datos de donde se encuentra
     * la preview de la noticia
     * @param notice: Noticia de la que se necesita la preview
     * @return Referencia a la BD donde se encuentra la preview
     */
    public static DatabaseReference getPreviewReference(Notice notice){
        return Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .child(notice.getKey())
                .child("preview");
    }

    /**
     * Agrega una nueva noticia a la BD
     * @param notice Noticia por agregar
     */
    public static void addNotice(Notice notice){
        Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .push().setValue(notice);
    }

    /**
     * Remueve una notica de la BD
     * @param notice Noticia por remover
     */
    public static void removeNotice(Notice notice){
        Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .child(notice.getKey())
                .removeValue();
    }

    /**
     * Se conecta con la BD para comenzar a recibir
     * las noticias
     * @see #disconnect()
     */
    public void connect(){
        getNoticesQuery().addChildEventListener(this);
    }

    /**
     * Se desconecta de la BD para dejar de recibir
     * las noticias
     * @see #connect()
     */
    public void disconnect(){
        getNoticesQuery().removeEventListener(this);
    }

    /**
     * Se ha agregado una nueva noticia a la BD
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Notice notice = dataSnapshot.getValue(Notice.class);
        if(notice != null) {
            notice.setKey(dataSnapshot.getKey());
            callbacks.addNotice(notice);
        }
    }

    /**
     * Se ha actualizado una noticia en la BD
     * TODO: Aunque la aplicación ni la web permiten modificar noticias,
     * TODO: puede considerarse implementarse a futuro
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Notice notice = dataSnapshot.getValue(Notice.class);
        if(notice != null) {
            notice.setKey(dataSnapshot.getKey());
            callbacks.changeNotice(notice);
        }
    }

    /**
     * Se recibe un noticia que ha sido eliminada de la BD
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Notice notice = dataSnapshot.getValue(Notice.class);
        if(notice != null) {
            notice.setKey(dataSnapshot.getKey());
            callbacks.removeNotice(notice);
        }
    }

}
