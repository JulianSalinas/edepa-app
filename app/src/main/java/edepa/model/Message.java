package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Message implements Parcelable {

    protected String key;

    private long time;
    private boolean delivered;
    private Boolean fromCurrentUser;

    private String userid;
    private String content;
    private String username;
    private String imageUrl;

    private Preview preview;

    public Message() {}

    private Message(Builder builder) {
        setKey(builder.key);
        setTime(builder.time);
        setDelivered(builder.delivered);
        setFromCurrentUser(builder.fromCurrentUser);
        setUserid(builder.userid);
        setContent(builder.content);
        setUsername(builder.username);
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

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getFromCurrentUser() {
        return fromCurrentUser == null ? false : fromCurrentUser;
    }

    public void setFromCurrentUser(Boolean fromCurrentUser) {
        this.fromCurrentUser = fromCurrentUser;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(getKey(), message.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    public static final class Builder {
        private String key;
        private long time;
        private boolean delivered;
        private Boolean fromCurrentUser;
        private String userid;
        private String content;
        private String username;
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

        public Builder delivered(boolean delivered) {
            this.delivered = delivered;
            return this;
        }

        public Builder fromCurrentUser(Boolean fromCurrentUser) {
            this.fromCurrentUser = fromCurrentUser;
            return this;
        }

        public Builder userid(String userid) {
            this.userid = userid;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
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

        public Message build() {
            return new Message(this);
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
        dest.writeByte(this.delivered ? (byte) 1 : (byte) 0);
        dest.writeValue(this.fromCurrentUser);
        dest.writeString(this.userid);
        dest.writeString(this.content);
        dest.writeString(this.username);
        dest.writeString(this.imageUrl);
        dest.writeParcelable(this.preview, flags);
    }

    protected Message(Parcel in) {
        this.key = in.readString();
        this.time = in.readLong();
        this.delivered = in.readByte() != 0;
        this.fromCurrentUser = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.userid = in.readString();
        this.content = in.readString();
        this.username = in.readString();
        this.imageUrl = in.readString();
        this.preview = in.readParcelable(Preview.class.getClassLoader());
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

}

