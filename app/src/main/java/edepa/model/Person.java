package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    public Person() { }

    private Person(Builder builder) {
        setKey(builder.key);
        setCompleteName(builder.completeName);
        setPersonalTitle(builder.personalTitle);
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
    }

    protected Person(Parcel in) {
        this.key = in.readString();
        this.completeName = in.readString();
        this.personalTitle = in.readString();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
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

