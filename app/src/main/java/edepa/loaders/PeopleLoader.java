package edepa.loaders;

import com.google.firebase.database.DataSnapshot;

import edepa.model.Person;
import edepa.interfaces.IPeopleSubject;

public class PeopleLoader extends BaseLoader {

    /**
     * Adaptador que contiene los eventos del objeto
     * que se pretende poblar
     */
    protected IPeopleSubject subject;

    /**
     * Contructor
     * @param subject Objeto que implementa {@link IPeopleSubject}
     */
    public PeopleLoader (IPeopleSubject subject) {
        this.subject = subject;
    }

    /**
     * TODO: Documentar
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null) {
            person.setKey(dataSnapshot.getKey());
            subject.addPerson(person);
        }
    }

    /**
     * TODO: Documentar
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null) {
            person.setKey(dataSnapshot.getKey());
            subject.changePerson(person);
        }
    }

    /**
     * TODO: Documentar
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null) {
            person.setKey(dataSnapshot.getKey());
            subject.removePerson(person);
        }
    }

}
