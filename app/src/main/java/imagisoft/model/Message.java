package imagisoft.model;

import java.util.Objects;

/**
 * Clase usada para enviar y recibir mensajes del chat
 */
public class Message {

    private String key;
    private Long time;
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

    public Long getTime() {
        return time;
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

    }

    /**
     * Constructor utilizado desde el chat para enviar mensajes
     * No utiliza la key porque este lo asigna Firebase
     */
    public Message(String userid, String username, String content, Long time) {
        this.time = time;
        this.userid = userid;
        this.content = content;
        this.username = username;
        this.delivered = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(key, message.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}

