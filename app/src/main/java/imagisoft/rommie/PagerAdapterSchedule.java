package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import imagisoft.edepa.ScheduleEvent;


public class PagerAdapterSchedule extends PagerAdapter {

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public PagerAdapterSchedule(MainActivityFragment schedulePager) {

        super(schedulePager);

        // Se deben ordenar los eventos por fecha de inicio
        Query query = this.schedulePager
                .getFirebase()
                .getScheduleReference()
                .orderByChild("start");

        query.addValueEventListener(new SchedulePagerAdapterValueEventListener());

    }

    /**
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class SchedulePagerAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            List<ScheduleEvent> newEvents = new ArrayList<>();

            // Se extraen todos los eventos de firebase
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                newEvents.add(postSnapshot.getValue(ScheduleEvent.class));

            events = newEvents;
            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(schedulePager.getTag(), databaseError.toString());
        }

    }

}