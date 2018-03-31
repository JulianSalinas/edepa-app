package imagisoft.rommie;

import java.util.ArrayList;

import imagisoft.edepa.Congress;
import imagisoft.edepa.Message;
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
    final DatabaseReference congressReference;
    final DatabaseReference chatReference;

    /**
     * Funciones que sirven para obtener las referencias desde
     * los fragmentos y así colocar los listeners
     */
    public DatabaseReference getScheduleReference() {
        return scheduleReference;
    }

    public DatabaseReference getCongressReference() { return congressReference; }

    /**
     * Cronograma que permanece en memoria para realizar las
     * consultas necesarias
     */
    private Schedule schedule;
    private Congress congressInformation;
    private ArrayList<Message> chatRoom;

    /**
     * Función para que otros fragmentos puedan obtener el cronograma
     */
    public Schedule getSchedule(){
        return schedule;
    }

    public Congress getCongressInformation() {
        return congressInformation;
    }

    public DatabaseReference getChatReference() {
        return chatReference;
    }

    /**
     * En el constructor se crean las referencias a la BD
     */
    public MainViewFirebase(){

        // Por si ocurre un error de conexión que no lance un nullpointer
        schedule = new Schedule();
        congressInformation = new Congress();

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);

        // Creando referencias a la base da datos
        this.root = database.getReference("edepa5");
        this.scheduleReference = root.child("schedule");
        this.congressReference = root.child("congress");
        this.chatReference =root.child("chat");

        // Listener para obtener datos del cronograma desde firebase
        ValueEventListener listener = new ScheduleValueEventListener();
        scheduleReference.addValueEventListener(listener);

        ValueEventListener listener2 = new CongressValueEventListener();
        congressReference.addValueEventListener(listener2);
    }

    class CongressValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            GenericTypeIndicator<Congress> typeIndicator;
            typeIndicator = new GenericTypeIndicator<Congress>(){};
            congressInformation = dataSnapshot.getValue(typeIndicator);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // TODO: Colocar algo por si pasa un error
        }
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
