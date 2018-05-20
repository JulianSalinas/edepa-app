package imagisoft.rommie;

import android.util.Log;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;


public class EventsViewAdapterSchedule
        extends EventsViewAdapter implements ChildEventListener {

    private String date;
    private Long dateStart;
    private Long dateEnd;

    public EventsViewAdapterSchedule(EventsView view) {
        super(view);
        this.date = ((EventsViewSchedule) view).getDate();
        this.dateStart = DateConverter.stringToLong(date + " 12:00 am");
        this.dateEnd = DateConverter.stringToLong(date + " 12:00 pm");

        view.activity
                .getScheduleReference()
                .orderByChild("start")
                .startAt(dateStart)
                .endAt(dateEnd)
                .addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null &&
                DateConverter.extractDate(event.getStart()).equals(date)) {
                int index = events.size();
                events.add(index, event);
                notifyItemInserted(index);
                Log.i(view.getTag(), "eventAdded");
                view.showStatusMessage("Un evento ha sido agregado");
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null &&
                DateConverter.extractDate(event.getStart()).equals(date)) {
                int index = events.indexOf(event);
                events.set(index, event);
                notifyItemChanged(index);
                Log.i(view.getTag(), "eventChanged");
                view.showStatusMessage("Un evento ha sido actualizado");
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null &&
                DateConverter.extractDate(event.getStart()).equals(date)) {
                int index = events.indexOf(event);
                events.remove(index);
                notifyItemRemoved(index);
                Log.i(view.getTag(), "eventRemoved");
                view.showStatusMessage("Un evento ha sido removido");
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(view.getTag(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(view.getTag(), databaseError.getDetails());
    }

}
