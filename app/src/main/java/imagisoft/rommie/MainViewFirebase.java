package imagisoft.rommie;

import java.util.ArrayList;
import imagisoft.edepa.Schedule;
import imagisoft.edepa.ScheduleEvent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.GenericTypeIndicator;

public abstract class MainViewFirebase extends MainView {

    /**
     * Conexión con Firebase
     */
    final FirebaseDatabase database;

    /**
     * Referencia a cada una de las secciones de la BD
     */
    final DatabaseReference root;
    final DatabaseReference scheduleReference;

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */
    public DatabaseReference getScheduleReference() {
        return scheduleReference;
    }

    /**
     * Cronograma que permanece en memoria para realizar las
     * consultas necesarias
     */
    private Schedule schedule;

    /**
     * Función para que otros fragmentos puedan obtener el cronograma
     */
    public Schedule getSchedule(){
        return schedule;
    }

    /**
     * En el constructor se crean las referencias a la BD
     */
    public MainViewFirebase(){

        // Por si ocurre un error de conexión que no lance un nullpointer
        schedule = new Schedule();

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);

        // Creando referencias a la base da datos
        this.root = database.getReference("edepa5");
        this.scheduleReference = root.child("schedule");

        // Listener para obtener datos del cronograma desde firebase
        ValueEventListener listener = new ScheduleValueEventListener();
        scheduleReference.addValueEventListener(listener);

    }

    class ScheduleValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            // Se encarga de castear un arreglo de firebase a ArrayList
            GenericTypeIndicator<ArrayList<ScheduleEvent>> typeIndicator;
            typeIndicator = new GenericTypeIndicator<ArrayList<ScheduleEvent>>(){};
            schedule = new Schedule(dataSnapshot.getValue(typeIndicator));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // TODO: Colocar algo por si pasa un error
        }

    }

}
