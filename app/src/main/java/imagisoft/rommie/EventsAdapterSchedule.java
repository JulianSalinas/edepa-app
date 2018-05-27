package imagisoft.rommie;

import android.util.Log;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

import java.util.Collections;


public class EventsAdapterSchedule
        extends EventsAdapter implements ChildEventListener {

    public EventsAdapterSchedule(EventsFragment view) {
        super(view);
        long date = ((EventsFragmentSchedule) view).getDate();

        view.activity
                .getScheduleReference()
                .orderByChild("start")
                .startAt(date)
                .endAt(DateConverter.atEndOFDay(date))
                .addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {

            int index = Collections.binarySearch(events, event);

            if (index >= 0){
                events.add(index, event);
                notifyItemInserted(index);
            }

            else {
                events.add(-index - 1, event);
                notifyItemInserted(-index - 1);
            }

        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            int index = events.indexOf(event);
            events.set(index, event);
            notifyItemChanged(index);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            int index = events.indexOf(event);
            events.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i("EventsAdapterSchedule::", s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("EventsAdapterSchedule::", databaseError.getDetails());
    }

}
