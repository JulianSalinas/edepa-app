package edepa.cloud;

import edepa.model.Person;

import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;


public class CloudPeople extends CloudChild {

    /**
     * Interfaz que permite recibir información de las
     * personas que participan en el Congreso
     */
    protected Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addPerson(Person person);
        void changePerson(Person person);
        void removePerson(Person person);
    }

    /**
     * Consulta que contiene todas los personas que
     * participan en el congreso ordenados por nombre
     * @return Query con la información de las personas
     */
    public static Query getPeopleQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.PEOPLE)
                .orderByChild("completeName");
    }

    /**
     * se conecta con la Bd para comenzar a recibir
     * información de las personas
     * @see #disconnect()
     */
    public void connect(){
        getPeopleQuery().addChildEventListener(this);
    }

    /**
     * Se deconecta de la BD y se deja de recibir la información
     * de las personas que participan en le congreso
     */
    public void disconnect(){
        getPeopleQuery().removeEventListener(this);
    }

    /**
     * Se ha agregado una nueva persona a la BD
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null) {
            person.setKey(dataSnapshot.getKey());
            callbacks.addPerson(person);
        }
    }

    /**
     * Se ha modificado la información de una persona en la BD
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null) {
            person.setKey(dataSnapshot.getKey());
            callbacks.changePerson(person);
        }
    }

    /**
     * Se removido la información de un persona en la BD
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null) {
            person.setKey(dataSnapshot.getKey());
            callbacks.removePerson(person);
        }
    }

}
