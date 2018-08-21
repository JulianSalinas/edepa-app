package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Preview implements Parcelable {

    private String url;
    private String header;
    private String imageUrl;
    private String description;

    public Preview() { }

    private Preview(Builder builder) {
        setHeader(builder.header);
        setImageUrl(builder.imageUrl);
        setDescription(builder.description);
        setUrl(builder.url);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static final class Builder {
        private String header;
        private String imageUrl;
        private String description;
        private String url;

        public Builder() {
        }

        public Builder header(String header) {
            this.header = header;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
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
        dest.writeString(this.header);
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
        dest.writeString(this.url);
    }

    protected Preview(Parcel in) {
        this.header = in.readString();
        this.imageUrl = in.readString();
        this.description = in.readString();
        this.url = in.readString();
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

