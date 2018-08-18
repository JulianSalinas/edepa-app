package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Congress implements Parcelable {

    private long end;
    private long start;

    private float xCoord;
    private float yCoord;

    private String name;
    private String location;
    private String description;
    private String locationTag;

    public Congress() { }

    private Congress(Builder builder) {
        setEnd(builder.end);
        setStart(builder.start);
        setxCoord(builder.xCoord);
        setyCoord(builder.yCoord);
        setName(builder.name);
        setLocation(builder.location);
        setDescription(builder.description);
        setLocationTag(builder.locationTag);
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

    public float getxCoord() {
        return xCoord;
    }

    public void setxCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }


    public static final class Builder {
        private long end;
        private long start;
        private float xCoord;
        private float yCoord;
        private String name;
        private String location;
        private String description;
        private String locationTag;

        public Builder() {
        }

        public Builder end(long end) {
            this.end = end;
            return this;
        }

        public Builder start(long start) {
            this.start = start;
            return this;
        }

        public Builder xCoord(float xCoord) {
            this.xCoord = xCoord;
            return this;
        }

        public Builder yCoord(float yCoord) {
            this.yCoord = yCoord;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder locationTag(String locationTag) {
            this.locationTag = locationTag;
            return this;
        }

        public Congress build() {
            return new Congress(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.end);
        dest.writeLong(this.start);
        dest.writeFloat(this.xCoord);
        dest.writeFloat(this.yCoord);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.locationTag);
    }

    protected Congress(Parcel in) {
        this.end = in.readLong();
        this.start = in.readLong();
        this.xCoord = in.readFloat();
        this.yCoord = in.readFloat();
        this.name = in.readString();
        this.location = in.readString();
        this.description = in.readString();
        this.locationTag = in.readString();
    }

    public static final Parcelable.Creator<Congress> CREATOR = new Parcelable.Creator<Congress>() {
        @Override
        public Congress createFromParcel(Parcel source) {
            return new Congress(source);
        }

        @Override
        public Congress[] newArray(int size) {
            return new Congress[size];
        }
    };

}
