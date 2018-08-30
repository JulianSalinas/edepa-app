package edepa.cloud;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * De esta clase se pueden obtener todas las
 * referencias que se necesiten de Firebase
 */
public class Cloud {

    private final FirebaseAuth auth;
    private final DatabaseReference root;
    private final FirebaseStorage storage;
    private final FirebaseDatabase database;

    /**
     * Variables usadas para obtener un referencia un nodo
     * específico de la BD con la función {@link #getReference(String)}
     */
    public static final String NEWS = "news";
    public static final String CHAT = "chat";

    public static final String PEOPLE = "people";
    public static final String ADMINS = "admins";
    public static final String CONFIG = "config";

    public static final String SCHEDULE = "schedule";
    public static final String CONGRESS = "congress";
    public static final String FAVORITES = "favorites";

    /**
     * Patrón singleton
     */
    private static final Cloud ourInstance = new Cloud();

    public static Cloud getInstance() {
        return ourInstance;
    }

    /**
     * Constructor. Inicializa los atributos para
     * conectar con Firebase
     */
    private Cloud() {

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();

        // No se puede mover arriba de this.database
        this.auth = FirebaseAuth.getInstance();

        // Para almacenar imagenes
        this.storage = FirebaseStorage.getInstance();

        // TODO Se debe cambiar la raíz cuando se termina la app
        this.root = database.getReference("edepa5");

    }

    /**
     * Obtiene el id del usuario que está usando la app
     * @return Id del usuario
     */
    public String getUserId(){
        return auth.getUid();
    }

    /**
     * Obtiene una referencia de la base de datos para colocar
     * un callbacks en dicha referencia
     * @param section: Alguna de las constantes declaradas en #Cloud
     * @return Referencia hacia una sección de la base de datos 
     */
    public DatabaseReference getReference(String section){
        return root.child(section.toLowerCase());
    }

}
