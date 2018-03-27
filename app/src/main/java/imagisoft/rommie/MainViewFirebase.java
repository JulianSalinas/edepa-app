package imagisoft.rommie;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class MainViewFirebase extends MainView {

    /**
     * Conexión con Firebase
     */
    final FirebaseDatabase database;

    /**
     * Referencia a cada una de las secciones de la BD
     */
    final DatabaseReference root;
    final DatabaseReference scheduleSection;

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */
    public DatabaseReference getScheduleSection() {
        return scheduleSection;
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
        this.scheduleSection = root.child("schedule");

    }

    /**
     * Se inician todos los componenetes principales de la aplicación
     */
    @Override
    protected void onCreate (Bundle bundle) {
        super.onCreate(bundle);
    }

}
