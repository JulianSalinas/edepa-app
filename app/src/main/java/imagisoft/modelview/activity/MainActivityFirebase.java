package imagisoft.modelview.activity;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


public abstract class MainActivityFirebase extends AppCompatActivity {

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

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */
    public FirebaseStorage getStorage() {
        return storage;
    }

    public DatabaseReference getNewsReference() {
        return root.child("news");
    }

    public DatabaseReference getChatReference() {
        return root.child("chat");
    }

    public DatabaseReference getConfigReference() {
        return root.child("config");
    }

    public DatabaseReference getOngoingReference() {
        return root.child("ongoing");
    }

    public DatabaseReference getScheduleReference() {
        return root.child("schedule");
    }

    public DatabaseReference getCongressReference() {
        return root.child("congress");
    }

    public DatabaseReference getFavoritesReference() {
        return root.child("favorites");
    }

    /**
     * En el constructor se crean las referencias a la BD
     */
    public MainActivityFirebase(){

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();

        // No se puede mover arriba de this.database
        this.auth = FirebaseAuth.getInstance();

        // Para almacenar imagenes
        this.storage = FirebaseStorage.getInstance();

        // Creando referencias a la base da datos
        this.root = database.getReference("edepa5");

    }

}
