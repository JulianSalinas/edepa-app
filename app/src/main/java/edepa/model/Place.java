package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Esta clase se utiliza tanto para los hoteles como
 * para los restaurantes
 */
public class Place implements Parcelable {

    private String key;

    /**
     * Página web para hacer reservaciones
     */
    private String web;

    /**
     * Teléfono para reservaciones
     */
    private String phone;

    /**
     * Nombre del hotel, hospedaje, etc
     */
    private String name;

    /**
     * Ubicación, kilometros desde el congreso
     * o una descripción breve de
     */
    private String description;

    /**
     * Marca un punto específico del mapa
     */
    private String location;

    private String imageUrl;

    private String facebookUrl;

    public Place() {}

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place = (Place) o;
        return Objects.equals(getKey(), place.getKey());
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
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeString(this.imageUrl);
        dest.writeString(this.facebookUrl);
    }

    protected Place(Parcel in) {
        this.key = in.readString();
        this.web = in.readString();
        this.phone = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.location = in.readString();
        this.imageUrl = in.readString();
        this.facebookUrl = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}

