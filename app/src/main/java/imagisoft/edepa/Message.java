package imagisoft.edepa;

/**
 * Clase usada para enviar y recibir mensajes del chat
 * Tambien se usa para la sección de noticias porque tiene los mismos datos
 */
public class Message {

    /**
     * El id es para identificar si el mensaje corresponde al usuario
     * que esta usando el app, y así, posteriomente acomodar los mensajes a la izq o der
     */
    private String userid;

    /**
     * Atributos fundamentales en un mensaje
     */
    private Long time;
    private String content;
    private String username;

    /**
     * Getters y Setters de los atributos del mensaje
     */
    public String getUserid() {
        return userid;
    }

    public Long getTime() {
        return time;
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

    }

    /**
     * Constructor utilizado desde el chat para enviar mensajes
     */
    public Message(String userid, String username, String content, Long time) {

        this.time = time;
        this.userid = userid;
        this.content = content;
        this.username = username;

    }

}
