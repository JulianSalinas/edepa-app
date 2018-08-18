package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Message implements Parcelable {

    protected String key;

    private long time;
    private boolean delivered;
    private boolean fromCurrentUser;

    private String userid;
    private String content;
    private String username;

    public Message() {}

    private Message(Builder builder) {
        setKey(builder.key);
        setTime(builder.time);
        setDelivered(builder.delivered);
        setFromCurrentUser(builder.fromCurrentUser);
        setUserid(builder.userid);
        setContent(builder.content);
        setUsername(builder.username);
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

    public boolean isFromCurrentUser() {
        return fromCurrentUser;
    }

    public void setFromCurrentUser(boolean fromCurrentUser) {
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
        private boolean fromCurrentUser;
        private String userid;
        private String content;
        private String username;

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

        public Builder fromCurrentUser(boolean fromCurrentUser) {
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
        dest.writeByte(this.fromCurrentUser ? (byte) 1 : (byte) 0);
        dest.writeString(this.userid);
        dest.writeString(this.content);
        dest.writeString(this.username);
    }

    protected Message(Parcel in) {
        this.key = in.readString();
        this.time = in.readLong();
        this.delivered = in.readByte() != 0;
        this.fromCurrentUser = in.readByte() != 0;
        this.userid = in.readString();
        this.content = in.readString();
        this.username = in.readString();
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
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

