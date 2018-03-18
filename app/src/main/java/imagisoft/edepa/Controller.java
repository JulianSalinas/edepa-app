package imagisoft.edepa;

import java.util.ArrayList;

public class Controller {

    /**
     * Atributos globales de la aplicación.
     * Es distinto para cada usuario
     */
    private int userid;
    private String username;
    private Congress congress;
    private ArrayList<Message> chatRoom;
    private ArrayList<Message> newsRoom;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Congress getCongress() {
        return congress;
    }

    public void setCongress(Congress congress) {
        this.congress = congress;
    }

    public ArrayList<Message> getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ArrayList<Message> chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ArrayList<Message> getNewsRoom() {
        return newsRoom;
    }

    public void setNewsRoom(ArrayList<Message> newsRoom) {
        this.newsRoom = newsRoom;
    }

    /**
     * Implementación del singleton
     */
    private static final Controller ourInstance = new Controller();

    /**
     * Llamar este método en vez de instanciar r
     * @return Controller
     */
    public static Controller getInstance() {
        return ourInstance;
    }

    /**
     * Se debe instanciar al iniciar la aplicación.
     */
    private Controller() {

        // Usuario de pruebas
        setUserid(123);
        setUsername("jsalinas");

        // Código cochino
        try {
            // Congreso de prueba
            congress = new Congress(
                    "EDEPA",
                    UDateConverter.stringToLong("12/12/18 12:00 am"),
                    UDateConverter.stringToLong("15/12/18 12:00 am")
            );

            chatRoom = new ArrayList<>();
            newsRoom = new ArrayList<>();

            // Mensajes de prueba
            chatRoom.add(new Message(
                    123, "jsalinas", "Lorem ipsum dolor sit amet",
                    UDateConverter.stringToLong("12/12/18 11:00 am")
            ));

            chatRoom.add(new Message(
                    321, "bdinarte", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    123, "jsalinas", "Integer urna purus, commodo vel leo ut, fringilla elementum ante. ",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    321, "bdinarte", "Nulla ut libero ac odio bibendum convallis. ",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    123, "jsalinas", "Ut eu leo bibendum, interdum arcu eu, pretium orci.",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    321, "bdinarte", "Lorem",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    123, "jsalinas", "Bibendum",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    321, "bdinarte", "Nulla ut libero ac odio bibendum convallis.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit. ",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            chatRoom.add(new Message(
                    123, "jsalinas", "Ut eu leo bibendum, interdum arcu eu, pretium orci.Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));






            // Noticias de prueba
            newsRoom.add(new Message(
                    123, "jsalinas", "Lorem ipsum dolor sit amet",
                    UDateConverter.stringToLong("12/12/18 11:00 am")
            ));

            newsRoom.add(new Message(
                    321, "bdinarte", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    123, "jsalinas", "Integer urna purus, commodo vel leo ut, fringilla elementum ante. ",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    321, "bdinarte", "Nulla ut libero ac odio bibendum convallis. ",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    123, "jsalinas", "Ut eu leo bibendum, interdum arcu eu, pretium orci.",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    321, "bdinarte", "Lorem",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    123, "jsalinas", "Bibendum",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    321, "bdinarte", "Nulla ut libero ac odio bibendum convallis.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit. ",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

            newsRoom.add(new Message(
                    123, "jsalinas", "Ut eu leo bibendum, interdum arcu eu, pretium orci.Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    UDateConverter.stringToLong("12/12/18 11:05 am")
            ));

        }
        catch (Exception e){
            // Solo para atajar el error
        }

    }

//    /**
//     * TODO: Aqui se debe enviar el mensaje con firebase
//     * @param msgContent contenido del mensaje
//     */
//    public void sendMessage(String msgContent){
//        Long datetime = Calendar.getInstance().getTimeInMillis();
//        Message msg = new Message(getUserid(), getUsername(), msgContent, datetime);
//        //Enviar aquí
//    }

}
