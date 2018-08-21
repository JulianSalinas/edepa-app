package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Notice implements Parcelable {

    protected String key;

    private long time;
    private int viewed;

    private String title;
    private String content;
    private String imageUrl;

    private Preview preview;

    public Notice() { }

    private Notice(Builder builder) {
        setKey(builder.key);
        setTime(builder.time);
        setViewed(builder.viewed);
        setTitle(builder.title);
        setContent(builder.content);
        setImageUrl(builder.imageUrl);
        setPreview(builder.preview);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notice)) return false;
        Notice notice = (Notice) o;
        return Objects.equals(getKey(), notice.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }


    public static final class Builder {
        private String key;
        private long time;
        private int viewed;
        private String title;
        private String content;
        private String imageUrl;
        private Preview preview;

        public Builder() {
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder time(long time) {
            this.time = time;
            return this;
        }

        public Builder viewed(int viewed) {
            this.viewed = viewed;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder preview(Preview preview) {
            this.preview = preview;
            return this;
        }

        public Notice build() {
            return new Notice(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeLong(this.time);
        dest.writeInt(this.viewed);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.imageUrl);
        dest.writeParcelable(this.preview, flags);
    }

    protected Notice(Parcel in) {
        this.key = in.readString();
        this.time = in.readLong();
        this.viewed = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.imageUrl = in.readString();
        this.preview = in.readParcelable(Preview.class.getClassLoader());
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel source) {
            return new Notice(source);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

}
