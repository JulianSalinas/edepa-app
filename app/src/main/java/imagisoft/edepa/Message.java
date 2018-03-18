package imagisoft.edepa;

/**
 * Clase usada para enviar y recibir mensajes del chat
 * Tambien se usa para la sección de noticias porque tiene los mismos datos
 */
public class Message {

    /**
     * El id es para identificar si el mensaje corresponde al usuario
     * que esta usando el app, y así, acomoddar los mensajes a la izq o der
     */
    private int userid;

    private Long time;
    private String username;
    private String content;

    public int getUserid() {
        return userid;
    }

    public Long getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Message(int userid, String username, String content, Long time) {
        this.userid = userid;
        this.username = username;
        this.content = content;
        this.time = time;
    }

}
