package imagisoft.rommie;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public abstract class MainViewFirebase extends MainView {

    /**
     * Conexión con Firebase
     */
    final FirebaseDatabase database;

    /**
     * Referencia a cada una de las secciones de la BD
     */
    final DatabaseReference root;
    final DatabaseReference scheduleReference;
    final DatabaseReference congressReference;
    final DatabaseReference chatReference;

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */
    public DatabaseReference getScheduleReference() {
        return scheduleReference;
    }

    public DatabaseReference getCongressReference() {
        return congressReference;
    }

    public DatabaseReference getChatReference() {
        return chatReference;
    }

    /**
     * En el constructor se crean las referencias a la BD
     */
    public MainViewFirebase(){

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);

        // Creando referencias a la base da datos
        this.root = database.getReference("edepa5");
        this.scheduleReference = root.child("schedule");
        this.congressReference = root.child("congress");
        this.chatReference =root.child("chat");

    }

}
