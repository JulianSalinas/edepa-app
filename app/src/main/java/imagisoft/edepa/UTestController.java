package imagisoft.edepa;

import java.util.Hashtable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Cuando cambia algo en esta clase se debe cambiar en la interfaz
 */
public class UTestController {

     /**
     * Conexión con Firebase
     */
    final FirebaseDatabase database;
    final DatabaseReference root;
    final DatabaseReference scheduleSection;

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getRoot() {
        return root;
    }

    public DatabaseReference getScheduleSection() {
        return scheduleSection;
    }
    /**
     * Implementación del singleton
     */
    private static final UTestController ourInstance = new UTestController();

    /**
     * Llamar este método en vez de instanciar r
     * @return Controller
     */
    public static UTestController getInstance() {
        return ourInstance;
    }

    /**
     * Constructor del controlador, no se debe usar.
     * Usar getInstance en vez de eso
     */
    public UTestController() {

        // Creando referencias a la base da datos
        this.database = FirebaseDatabase.getInstance();
        this.root = database.getReference("edepa5");
        this.scheduleSection = root.child("cronograma");

        // Subir información de prueba a firebase
        // uploadSchedule();

        // Descargar los datos necesarios aquí
        // downloadSchedule();

    }

    /**
     * Carga la información del cronograma en firebase
     */
    private void uploadSchedule(){
        Schedule schedule = UTestGenerator.createSchedule();
        scheduleSection.setValue(schedule.getEvents());
    }

}
