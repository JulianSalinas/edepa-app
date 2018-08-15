package edepa.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * De esta clase se pueden obtener todas las referencias
 * que se necesiten de Firebase
 */
public class Cloud {

    public static final String PEOPLE = "people";
    public static final String ADMINS = "admins";

    /**
     * Principales caracteristicas de Firebase
     */
    final FirebaseAuth auth;
    final FirebaseStorage storage;
    final FirebaseDatabase database;
    final DatabaseReference root;

    /**
     * Variables usadas con la función
     * {@link #getReference(String)}
     */
    public static final String NEWS = "news";
    public static final String CHAT = "chat";
    public static final String CONFIG = "config";
    public static final String ONGOING = "ongoing";
    public static final String SCHEDULE = "schedule";
    public static final String CONGRESS = "congress";
    public static final String FAVORITES = "favorites";

    /**
     * Instancia única con el fin de evitar
     * crear listeners repetidos
     */
    private static final Cloud ourInstance = new Cloud();

    public static Cloud getInstance() {
        return ourInstance;
    }

    /**
     * Constructor de la clase
     */
    private Cloud() {

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();

        // No se puede mover arriba de this.database
        this.auth = FirebaseAuth.getInstance();

        // Para almacenar imagenes
        this.storage = FirebaseStorage.getInstance();

        // TODO Puede cambiar a futuro
        this.root = database.getReference("edepa5");

    }

    public FirebaseAuth getAuth(){
        return auth;
    }

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */
    public FirebaseStorage getStorage() {
        return storage;
    }

    /**
     * Obtiene una referencia de la base de datos para colocar
     * un subject en dicha referencia
     * @param section: Alguna de las constantes declaradas en #Cloud
     * @return Referencia hacia una sección de la base de datos 
     */
    public DatabaseReference getReference(String section){
        return root.child(section.toLowerCase());
    }

}
