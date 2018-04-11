package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.util.Log;
import java.util.ArrayList;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;

import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class SchedulePagerAdapter extends SchedulePager {

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public SchedulePagerAdapter(MainViewFragment schedulePager) {

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

            // Se extraen todos los eventos de firebase
            ArrayList<ScheduleBlock> scheduleEvents = new ArrayList<>();
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                scheduleEvents.add(postSnapshot.getValue(ScheduleEvent.class));

            orderEvents(scheduleEvents);
            eventsByDay = getEventsByDay();
            dates.addAll(eventsByDay.keySet());
            pages = new Fragment[dates.size()];
            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(schedulePager.getTag(), databaseError.toString());
        }

    }

}