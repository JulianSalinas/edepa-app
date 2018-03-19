package imagisoft.edepa;

import java.util.ArrayList;
import java.util.Observable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

/**
 * Cuando cambia algo en esta clase se debe cambiar en la interfaz
 */
public class UTestController extends Observable {

    /**
     * Conexión con Firebase
     */
    final DatabaseReference root;
    final DatabaseReference scheduleSection;
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
        downloadSchedule();

    }

    /**
     * Carga la información del cronograma en firebase
     */
    private void uploadSchedule(){
        this.schedule = UTestGenerator.createSchedule();
        scheduleSection.setValue(schedule.getEvents());
    }

    /**
     * Agregar un listener que permite descargar los datos la primera vez
     * y actualizarla en caso de que cambie en el servidor
     */
    private void downloadSchedule(){

        scheduleSection.addValueEventListener(new ValueEventListener() {

            /**
             * Descarga los evento sy crear la clase cronograma a partir
             * de los mismos
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<ScheduleEvent>> typeIndicator =
                        new GenericTypeIndicator<ArrayList<ScheduleEvent>>(){};
                schedule = new Schedule(dataSnapshot.getValue(typeIndicator));
                notifyObservers();
            }

            /**
             * Notifica a los observadores que ocurrio un error
             * al cargar el cronograma
             */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                notifyObservers(databaseError);
            }

        });
    }

    /**
     * Notifica a todos los observadores
     * El boton de favoritos del menu lateral responde con un evento
     * cuando se da la notificacion
     */
    public void testObserverPattern(){
         setChanged();
         notifyObservers("Probando Observer");
    }

}
