package edepa.cloud;

import edepa.model.Event;
import edepa.model.EventType;

import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;

/**
 * Clase que sirve para poblar de eventos los objetos
 * que implementan {@link Callbacks}, antes de usar
 * esta clase resulta conveniente usar antes {@link CloudFavorites}
 */
public class CloudEvents extends CloudChild {

    /**
     * Interfaz que permite recibir los eventos actualizados
     * de la BD
     */
    protected Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addEvent(Event event);
        void changeEvent(Event event);
        void removeEvent(Event event);
    }

    /**
     * Retorna un consulta por defecto, Esta obtiene
     * todos los eventos ordenados por fecha de inicio
     * @return Query con los eventos ordenados por hora de inicio
     */
    public static Query getEventsQueryByStart(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("start");
    }

    /**
     * Realiza la misma consulta que {@link #getEventsQueryByStart()}
     * con la diferencia de que este ordena los eventos por día y no
     * por la hora exacta a la que comienza cada uno
     * @return Query con los eventos ordenados por día
     */
    public static Query getEventsQueryByDate(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("date");
    }

    /**
     * Realiza la misma consulta que {@link #getEventsQueryByStart()}
     * con la diferencia de que este ordena los eventos para uno
     * día en específico
     * @param date: Fecha de los eventos por recuperar
     * @return Query con los eventos ordenados por día
     */
    public static Query getEventsQueryUsingDate(long date){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("date")
                .equalTo(date);
    }

    public static Query getEventsQueryUsingType(EventType type){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("eventype")
                .equalTo(type.toString());
    }

    /**
     * Obtiene un consulta para un evento en específico
     * @param eventKey: PushId del evento en BD
     * @return Query con el evento que corresponde la key
     */
    public static Query getSingleEventQuery(String eventKey) {
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .child(eventKey);
    }

    /**
     * Se conecta a la BD para recibir los eventos ordenados
     * por fecha por medio {@link #getEventsQueryByStart()}
     * @see #disconnect()
     */
    public void connect(){
        getEventsQueryByStart().addChildEventListener(this);
    }

    public void connectByDate(){
        getEventsQueryByDate().addChildEventListener(this);
    }

    /**
     * Se desconecta de la BD y se dejan de recibir eventos
     * @see #connect()
     */
    public void disconnect(){
        getEventsQueryByStart().removeEventListener(this);
    }

    public void disconnectByDate(){
        getEventsQueryByDate().removeEventListener(this);
    }

    /**
     * Realiza lo mismo que {@link #connect()} con la diferencia
     * de que puede utilizar un consulta personalizada.
     * @param query: Consulta personalizada
     * @see #connect(Query)
     */
    public void connect(Query query){
        query.addChildEventListener(this);
    }

    /**
     * Se desconecta de la BD y se dejan de recibir eventos
     * de una consulta personalizada
     * @param query: Consulta personalizada
     * @see #connect(Query)
     */
    public void disconnect(Query query){
        query.removeEventListener(this);
    }

    /**
     * Se recibe un nuevo evento de la BD
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Event event = dataSnapshot.getValue(Event.class);
        if (event != null) {
//            event.setKey(dataSnapshot.getKey());
//            if (event.getPeople() != null){
//                for(String personKey : event.getPeople().keySet()){
//                    Cloud.getInstance().getReference(Cloud.PEOPLE)
//                            .child(personKey).child("events").child(event.getKey()).setValue(true);
//                }
//            }
            event.setKey(dataSnapshot.getKey());
            callbacks.addEvent(event);
        }
    }

    /**
     * Se recibe un evento modificado de la BD
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Event event = dataSnapshot.getValue(Event.class);
        if (event != null) {
            event.setKey(dataSnapshot.getKey());
            callbacks.changeEvent(event);
        }
    }

    /**
     * Se recibe un evento que ha sido removido de la BD
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Event event = dataSnapshot.getValue(Event.class);
        if (event != null) {
            event.setKey(dataSnapshot.getKey());
            callbacks.removeEvent(event);
        }
    }

}
