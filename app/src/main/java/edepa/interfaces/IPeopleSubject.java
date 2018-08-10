package edepa.interfaces;

import edepa.model.Person;

public interface IPeopleSubject {

    void addPerson(Person person);
    void changePerson(Person person);
    void removePerson(Person person);

}
