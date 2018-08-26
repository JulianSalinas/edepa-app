package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Person implements Parcelable {

    protected String key;

    /**
     * El título podría ser solo el país y tal vez la institución
     * Ej: Julian Salinas
     *     Instituto Tecnológico de Costa Rica
     */
    private String completeName;
    private String personalTitle;

    private String about;
    private List<Event> eventsList;

    /**
     * A diferencia de {@link #eventsList} este solo
     * almacena las keys de los eventos
     */
    private HashMap<String, Boolean> events;

    public Person() { }

    private Person(Builder builder) {
        setKey(builder.key);
        setCompleteName(builder.completeName);
        setPersonalTitle(builder.personalTitle);
        setAbout(builder.about);
        setEventsList(builder.eventsList);
        setEvents(builder.events);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getPersonalTitle() {
        return personalTitle;
    }

    public void setPersonalTitle(String personalTitle) {
        this.personalTitle = personalTitle;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public HashMap<String, Boolean> getEvents() {
        return events;
    }

    public void setEvents(HashMap<String, Boolean> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getKey(), person.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }


    public static final class Builder {
        private String key;
        private String completeName;
        private String personalTitle;
        private String about;
        private List<Event> eventsList;
        private HashMap<String, Boolean> events;

        public Builder() {
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder completeName(String completeName) {
            this.completeName = completeName;
            return this;
        }

        public Builder personalTitle(String personalTitle) {
            this.personalTitle = personalTitle;
            return this;
        }

        public Builder about(String about) {
            this.about = about;
            return this;
        }

        public Builder eventsList(List<Event> eventsList) {
            this.eventsList = eventsList;
            return this;
        }

        public Builder events(HashMap<String, Boolean> events) {
            this.events = events;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.completeName);
        dest.writeString(this.personalTitle);
        dest.writeString(this.about);
        dest.writeTypedList(this.eventsList);
        dest.writeSerializable(this.events);
    }

    protected Person(Parcel in) {
        this.key = in.readString();
        this.completeName = in.readString();
        this.personalTitle = in.readString();
        this.about = in.readString();
        this.eventsList = in.createTypedArrayList(Event.CREATOR);
        this.events = (HashMap<String, Boolean>) in.readSerializable();
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

}


