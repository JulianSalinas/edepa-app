package edepa.cloud;

import edepa.minilibs.RegexSearcher;
import edepa.model.Message;
import edepa.model.Notice;
import edepa.model.Preferences;
import edepa.services.UpdateImageService;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.policy.TimeWindow;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class CloudChat extends CloudChild {

    /**
     * Interfaz para recibir mensajes del chat
     */
    private Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addMessage(Message message);
        void changeMessage(Message message);
        void removeMessage(Message message);
    }

    /**
     * Obtiene un objeto para realizar la consulta
     * de los últimos 500 mensajes en el chat
     * @return Query
     */
    public static Query getChatQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.CHAT)
                .orderByChild("time")
                .limitToLast(500);
    }

    /**
     * Obtiene una referencia a la base de datos de donde se encuentra
     * la preview de la noticia
     * @param message: Mensaje de la que se necesita la preview
     * @return Referencia a la BD donde se encuentra la preview
     */
    public static DatabaseReference getPreviewReference(Message message){
        return Cloud.getInstance()
                .getReference(Cloud.CHAT)
                .child(message.getKey())
                .child("preview");
    }

    /**
     * Agrega un mensaje nuevo a la BD
     * @param message: Mensaje por agregar
     * @return Key del mensaje agregado
     */
    public static String addMessage(Message message){

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("time", message.getTime());
        payload.put("content", message.getContent());
        payload.put("userid", message.getUserid());
        payload.put("username", message.getUsername());
        payload.put("preview", message.getPreview());

        String messageKey = Cloud.getInstance()
                .getReference(Cloud.CHAT).push().getKey();

        if(messageKey != null) {
            Cloud.getInstance().getReference(Cloud.CHAT)
                    .child(messageKey).setValue(payload);
        }

        return messageKey;

    }

    /**
     * Remueve un mensaje existente de la BD
     * @param message: Mensaje por remover
     */
    public static void removeMessage(Message message){
        Cloud.getInstance()
                .getReference(Cloud.CHAT)
                .child(message.getKey())
                .removeValue();
    }

    /**
     * Se sincroniza con la BD para obtener la
     * lista de los mensajes con base a {@link #getChatQuery()}
     * @see #disconnect()
     */
    public void connect(){
        getChatQuery().addChildEventListener(this);
    }

    /**
     * Se remueve la sincronización de los mensajes
     * utilizando {@link #getChatQuery()}
     * @see #connect()
     */
    public void disconnect(){
        getChatQuery().removeEventListener(this);
    }


    /**
     * Se recibe un nuevo mensaje de la BD.
     * Se coloca el atributo {@link Message#fromCurrentUser}
     * que es necesario para poder determinar de que lado
     * colocar en el mensaje en la interfaz
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message message = dataSnapshot.getValue(Message.class);
        if (message != null){
            message.setKey(dataSnapshot.getKey());
            String sender = message.getUserid();
            String receiver = Cloud.getInstance().getUserId();
            message.setFromCurrentUser(sender.equals(receiver));
            callbacks.addMessage(message);
        }
    }

    /**
     * Se recibe un mensaje modificado de la BD
     * TODO: Este método no se utiliza porque la app no permite
     * TODO: modificar mensajes pero puede ser útil a futuro
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Message message = dataSnapshot.getValue(Message.class);
        if (message != null){
            message.setKey(dataSnapshot.getKey());
            String sender = message.getUserid();
            String receiver = Cloud.getInstance().getUserId();
            message.setFromCurrentUser(sender.equals(receiver));
            callbacks.changeMessage(message);
        }
    }

    /**
     * Se recibe un mensaje que ha sido removido en la BD
     * En este caso no es necesario colocar de quién es el mensaje
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);
        if (message != null){
            message.setKey(dataSnapshot.getKey());
            callbacks.removeMessage(message);
        }
    }

}
