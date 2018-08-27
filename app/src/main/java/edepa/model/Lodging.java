package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Lodging implements Parcelable {

    private String key;

    /**
     * Página web para hacer reservaciones
     */
    private String web;

    /**
     * Nombre del hotel, hospedaje, etc
     */
    private String name;

    /**
     * Ubicación, kilometros desde el congreso
     * o una descripción breve de
     */
    private String location;

    public Lodging() {}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lodging)) return false;
        Lodging lodging = (Lodging) o;
        return Objects.equals(getKey(), lodging.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.web);
        dest.writeString(this.name);
        dest.writeString(this.location);
    }

    protected Lodging(Parcel in) {
        this.key = in.readString();
        this.web = in.readString();
        this.name = in.readString();
        this.location = in.readString();
    }

    public static final Parcelable.Creator<Lodging> CREATOR = new Parcelable.Creator<Lodging>() {
        @Override
        public Lodging createFromParcel(Parcel source) {
            return new Lodging(source);
        }

        @Override
        public Lodging[] newArray(int size) {
            return new Lodging[size];
        }
    };

}

