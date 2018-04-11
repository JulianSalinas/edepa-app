package imagisoft.rommie;

import imagisoft.edepa.Message;
import imagisoft.edepa.UTestGenerator;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public abstract class MainViewFirebase extends MainView {

    /**
     * Conexión con Firebase
     */
    final FirebaseStorage storage;
    final FirebaseDatabase database;


    /**
     * Referencia a cada una de las secciones de la BD
     */
    final DatabaseReference root;
    final DatabaseReference newsReference;
    final DatabaseReference chatReference;
    final DatabaseReference scheduleReference;
    final DatabaseReference congressReference;

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */

    public FirebaseStorage getStorage() {
        return storage;
    }

    public DatabaseReference getNewsReference() {
        return newsReference;
    }

    public DatabaseReference getChatReference() {
        return chatReference;
    }

    public DatabaseReference getScheduleReference() {
        return scheduleReference;
    }

    public DatabaseReference getCongressReference() {
        return congressReference;
    }

    /**
     * En el constructor se crean las referencias a la BD
     */
    public MainViewFirebase(){

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);

        // Para almacenar imagenes
        this.storage = FirebaseStorage.getInstance();

        // Creando referencias a la base da datos
        this.root = database.getReference("edepa5");
        this.chatReference = root.child("chat");
        this.newsReference = root.child("news");
        this.scheduleReference = root.child("schedule");
        this.congressReference = root.child("congress");

        // Linea que se debe descomentar cuando se necesita subir la información de prueba
        // this.scheduleReference.setValue(UTestGenerator.createSchedule());
        // ArrayList<Message> msgs = UTestGenerator.createNews();
        // for (Message msg : msgs)
        //    newsReference.push().setValue(msg);

    }

}
