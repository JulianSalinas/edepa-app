package imagisoft.edepa;

import android.os.Parcel;
import android.os.Parcelable;

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userid);
        dest.writeString(this.content);
        dest.writeString(this.username);
    }

    protected Message(Parcel in) {
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
