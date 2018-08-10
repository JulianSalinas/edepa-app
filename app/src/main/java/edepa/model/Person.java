package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;


public class Person implements Parcelable, Comparable<Person> {

    /**
     * Nombre donde se incluyen también apellidos de la persona
     */
    private String completeName;

    /**
     * El título podría ser solo el país y tal vez la institución
     * Ej: Julian Salinas
     *     Instituto Tecnológico de Costa Rica
     */
    private String personalTitle;

    private String personKey;

    /**
     * Getters y Setters de los atributos del expositor
     */
    public String getCompleteName() {
        return completeName;
    }

    public String getPersonalTitle() {
        return personalTitle;
    }

    public void setKey(String personKey) {
        this.personKey = personKey;
    }

    public String getPersonKey() {
        return personKey;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public Person() {

    }

    /**
     * Constructor usado únicamente para generar pruebas
     */
    public Person(String completeName, String personalTitle) {
        this.completeName = completeName;
        this.personalTitle = personalTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(completeName, person.completeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personKey);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.completeName);
        dest.writeString(this.personalTitle);
        dest.writeString(this.personKey);
    }

    protected Person(Parcel in) {
        this.completeName = in.readString();
        this.personalTitle = in.readString();
        this.personKey = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int compareTo(Person person) {
        return this.getCompleteName().compareTo(person.getCompleteName());
    }

}

