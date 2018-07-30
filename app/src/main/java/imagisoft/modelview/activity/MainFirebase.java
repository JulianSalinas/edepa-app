package imagisoft.modelview.activity;

import android.util.Log;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import imagisoft.model.Preferences;


public abstract class MainFirebase
        extends AppCompatActivity implements ValueEventListener{

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
    public MainFirebase(){

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

    /**
     * Actualiza la actividad una vez que se escriben
     * las preferencias disponibles en Firebase
     * como per ejemplo la disponibilidad de cada
     * una de las secciones
     */
    public abstract void update();

    /**
     * Obtienen todas las preferencias desde el dataSnapshot
     * y las escribe con {@link #setAvailable(String, DataSnapshot)}
     * @param dataSnapshot: Contiene las preferencias según la BD
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        setAvailable(Preferences.INFO_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.NEWS_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.CHAT_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.PALETTE_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.PEOPLE_AVAILABLE_KEY, dataSnapshot);
        update();
        Log.i(toString(), "onDataChange(DataSnapshot)");
    }

    /**
     * Escribe en las preferencias si una sección de la
     * aplicación debe estar disponible
     * @param key: Key de la clase Preferences
     * @param value: La sección debe o no estar disponible
     */
    public void setAvailable(String key, Boolean value){
        key = key.toLowerCase();
        Preferences.getInstance().setPreference(this, key, value);
        Log.i(toString(), "setAvailable("+ key + ", " + value.toString() + ")");
    }

    /**
     * Utilizada por la funcion {@link #onDataChange(DataSnapshot)}
     * @param key: Key de la clase Preferences
     * @param dataSnapshot: Donde se extrae en valor que se debe colocar
     *                      en las preferencias
     */
    public void setAvailable(String key, DataSnapshot dataSnapshot){
        key = key.toLowerCase();
        Boolean value = dataSnapshot.child(key).getValue(Boolean.class);
        Preferences.getInstance().setPreference(this, key, value);
        Log.i(toString(), "setAvailable("+ key + ", " + value.toString() + ")");
    }

    /**
     * Determina con base a las preferencias si una sección de
     * la aplicación debe estar disponible
     * @param key: Key de la clase Preferences
     * @return True si la sección debe estar disponible
     */
    public boolean isAvailable(String key){
        key = key.toLowerCase();
        return Preferences
                .getInstance()
                .getBooleanPreference(this, key);
    }

    /**
     * Ha ocurrido un error al leer las preferencias desde Firebase
     * @param databaseError: Contiene el mensaje con el error
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
