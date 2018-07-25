package imagisoft.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Clase usada para enviar y recibir mensajes del chat
 */
public class Message extends Timestamp implements Parcelable {

    private String key;
    private String userid;
    private String content;
    private String username;
    private boolean delivered;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getUserid() {
        return userid;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public Message(){
        super();
    }

    /**
     * Constructor utilizado desde el chat para enviar mensajes
     * No utiliza la key porque este lo asigna Firebase
     */
    public Message(String userid, String username, String content, Long time) {
        super(time);
        this.userid = userid;
        this.content = content;
        this.username = username;
        this.delivered = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message = (Message) o;
        return Objects.equals(key, message.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.key);
        dest.writeString(this.userid);
        dest.writeString(this.content);
        dest.writeString(this.username);
        dest.writeByte(this.delivered ? (byte) 1 : (byte) 0);
        dest.writeValue(this.time);
    }

    protected Message(Parcel in) {
        super(in);
        this.key = in.readString();
        this.userid = in.readString();
        this.content = in.readString();
        this.username = in.readString();
        this.delivered = in.readByte() != 0;
        this.time = (Long) in.readValue(Long.class.getClassLoader());
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
