package imagisoft.modelview;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public abstract class ActivityFirebase extends ActivityClassic {

    /**
     * Conexión con Firebase
     */
    final FirebaseAuth auth;
    final FirebaseStorage storage;
    final FirebaseDatabase database;


    /**
     * Referencia a cada una de las secciones de la BD
     */
    final DatabaseReference root;
    final DatabaseReference newsReference;
    final DatabaseReference chatReference;
    final DatabaseReference configReference;
    final DatabaseReference ongoingReference;
    final DatabaseReference scheduleReference;
    final DatabaseReference congressReference;
    final DatabaseReference favoritesReference;

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

    public DatabaseReference getConfigReference() {
        return configReference;
    }

    public DatabaseReference getOngoingReference() {
        return ongoingReference;
    }

    public DatabaseReference getScheduleReference() {
        return scheduleReference;
    }

    public DatabaseReference getCongressReference() {
        return congressReference;
    }

    public DatabaseReference getFavoritesReference() {
        return favoritesReference;
    }

    /**
     * En el constructor se crean las referencias a la BD
     */
    public ActivityFirebase(){

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();

        // No se puede mover arriba de this.database
        this.auth = FirebaseAuth.getInstance();

        // Para almacenar imagenes
        this.storage = FirebaseStorage.getInstance();

        // Creando referencias a la base da datos
        this.root = database.getReference("edepa5");

        this.chatReference = root.child("chat");
        this.newsReference = root.child("news");
        this.configReference = root.child("config");
        this.ongoingReference = root.child("ongoing");
        this.scheduleReference = root.child("schedule");
        this.congressReference = root.child("congress");
        this.favoritesReference = root.child("favorites");

    }

}
