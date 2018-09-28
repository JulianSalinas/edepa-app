package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Objects;


public class Event implements Parcelable {

    protected String key;

    private String title;
    private String location;
    private EventType eventype;

    protected long date;
    protected long end;
    protected long start;

    protected int favorites;
    protected boolean isFavorite;

    private String fileUrl;
    private String imageUrl;
    private String briefEnglish;
    private String briefSpanish;
    private HashMap<String, Boolean> people;

    public Event(){ }

    private Event(Builder builder) {
        setKey(builder.key);
        setTitle(builder.title);
        setLocation(builder.location);
        setEventype(builder.eventype);
        setDate(builder.date);
        setEnd(builder.end);
        setStart(builder.start);
        setFavorites(builder.favorites);
        setFavorite(builder.isFavorite);
        setFileUrl(builder.fileUrl);
        setImageUrl(builder.imageUrl);
        setBriefEnglish(builder.briefEnglish);
        setBriefSpanish(builder.briefSpanish);
        setPeople(builder.people);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventType getEventype() {
        return eventype;
    }

    public void setEventype(EventType eventype) {
        this.eventype = eventype;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBriefEnglish() {
        return briefEnglish;
    }

    public void setBriefEnglish(String briefEnglish) {
        this.briefEnglish = briefEnglish;
    }

    public String getBriefSpanish() {
        return briefSpanish;
    }

    public void setBriefSpanish(String briefSpanish) {
        this.briefSpanish = briefSpanish;
    }

    public HashMap<String, Boolean> getPeople() {
        return people;
    }

    public void setPeople(HashMap<String, Boolean> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(getKey(), event.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    public static final class Builder {
        private String key;
        private String title;
        private String location;
        private EventType eventype;
        private long date;
        private long end;
        private long start;
        private int favorites;
        private boolean isFavorite;
        private String fileUrl;
        private String imageUrl;
        private String briefEnglish;
        private String briefSpanish;
        private HashMap<String, Boolean> people;

        public Builder() {
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder eventype(EventType eventype) {
            this.eventype = eventype;
            return this;
        }

        public Builder date(long date) {
            this.date = date;
            return this;
        }

        public Builder end(long end) {
            this.end = end;
            return this;
        }

        public Builder start(long start) {
            this.start = start;
            return this;
        }

        public Builder favorites(int favorites) {
            this.favorites = favorites;
            return this;
        }

        public Builder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }

        public Builder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder briefEnglish(String briefEnglish) {
            this.briefEnglish = briefEnglish;
            return this;
        }

        public Builder briefSpanish(String briefSpanish) {
            this.briefSpanish = briefSpanish;
            return this;
        }

        public Builder people(HashMap<String, Boolean> people) {
            this.people = people;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.location);
        dest.writeInt(this.eventype == null ? -1 : this.eventype.ordinal());
        dest.writeLong(this.date);
        dest.writeLong(this.end);
        dest.writeLong(this.start);
        dest.writeInt(this.favorites);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.fileUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.briefEnglish);
        dest.writeString(this.briefSpanish);
        dest.writeSerializable(this.people);
        dest.writeString(this.key);
    }

    protected Event(Parcel in) {
        this.title = in.readString();
        this.location = in.readString();
        int tmpEventype = in.readInt();
        this.eventype = tmpEventype == -1 ? null : EventType.values()[tmpEventype];
        this.date = in.readLong();
        this.end = in.readLong();
        this.start = in.readLong();
        this.favorites = in.readInt();
        this.isFavorite = in.readByte() != 0;
        this.fileUrl = in.readString();
        this.imageUrl = in.readString();
        this.briefEnglish = in.readString();
        this.briefSpanish = in.readString();
        this.people = (HashMap<String, Boolean>) in.readSerializable();
        this.key = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}
