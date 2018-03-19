package imagisoft.edepa;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Observable;

/**
 * Cuando cambia algo en esta clase se debe cambiar en la interfaz
 */
public class UTestController extends Observable {

    /**
     * Conexión con Firebase
     */
    final DatabaseReference root;
    final FirebaseDatabase database;


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
     * Cronograma del congreso, esta clase se encarga de filtrar
     * los tipos de eventos y dividirlas en bloques
     */
    private Schedule schedule;

    public UTestController() {
        this.database = FirebaseDatabase.getInstance();
        this.root = database.getReference("edepa5");
        this.schedule = UTestGenerator.createSchedule();

        // Subir información de prueba a firebase
        // uploadSchedule();

    }

    public void uploadSchedule(){
        for(ScheduleEvent event : schedule.getEvents())
            uploadScheduleEvent(event);
    }

    public void uploadScheduleEvent(ScheduleEvent event){
        DatabaseReference section = root.child("cronograma");
        DatabaseReference eventSection = section.child(String.valueOf(event.getId()));
        eventSection.setValue(event);
    }

    public void testObserverPattern(){
         setChanged();
         notifyObservers("Probando Observer");
    }

}
