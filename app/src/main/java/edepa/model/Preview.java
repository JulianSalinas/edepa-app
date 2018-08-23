package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Preview implements Parcelable {

    private String url;
    private String header;
    private String thumbnail;
    private String description;

    public Preview() { }

    private Preview(Builder builder) {
        setUrl(builder.url);
        setHeader(builder.header);
        setThumbnail(builder.thumbnail);
        setDescription(builder.description);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Preview)) return false;
        Preview preview = (Preview) o;
        return Objects.equals(getUrl(), preview.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl());
    }


    public static final class Builder {

        private String url;
        private String header;
        private String thumbnail;
        private String description;

        public Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder header(String header) {
            this.header = header;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Preview build() {
            return new Preview(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.header);
        dest.writeString(this.thumbnail);
        dest.writeString(this.description);
    }

    protected Preview(Parcel in) {
        this.url = in.readString();
        this.header = in.readString();
        this.thumbnail = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Preview> CREATOR = new Creator<Preview>() {
        @Override
        public Preview createFromParcel(Parcel source) {
            return new Preview(source);
        }

        @Override
        public Preview[] newArray(int size) {
            return new Preview[size];
        }
    };

}

