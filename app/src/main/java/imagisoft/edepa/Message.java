package imagisoft.edepa;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Clase usada para enviar y recibir mensajes del chat
 * Tambien se usa para la sección de noticias porque tiene los mismos datos
 */
public class Message extends Timestamp implements Parcelable {

    /**
     * El id es para identificar si el mensaje corresponde al usuario
     * que esta usando el app, y así, posteriomente acomodar los mensajes a la izq o der
     */
    private String userid;

    /**
     * Atributos fundamentales en un mensaje
     */
    private String content;
    private String username;

    /**
     * Getters y Setters de los atributos del mensaje
     */
    public String getUserid() {
        return userid;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public Message(){
        super();
    }

    /**
     * Constructor utilizado desde el chat para enviar mensajes
     */
    public Message(String userid, String username, String content, Long time) {
        super(time);
        this.userid = userid;
        this.content = content;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        if (!super.equals(o)) return false;
        Message message = (Message) o;
        return Objects.equals(userid, message.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.userid);
        dest.writeString(this.content);
        dest.writeString(this.username);
        dest.writeValue(this.time);
    }

    protected Message(Parcel in) {
        super(in);
        this.userid = in.readString();
        this.content = in.readString();
        this.username = in.readString();
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
