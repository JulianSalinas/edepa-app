package imagisoft.modelview.schedule;

import android.util.Log;
import java.util.Collections;

import imagisoft.misc.DateConverter;
import imagisoft.model.Cloud;
import imagisoft.model.ScheduleEvent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class AdapterSchedule extends EventsAdapter implements ChildEventListener {

    public AdapterSchedule(IEventsSubject subject) {
        super((EventsFragment) subject);
        Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("startRunnable")
                .startAt(subject.getDate())
                .endAt(DateConverter.atEndOFDay(subject.getDate()))
                .addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {

            event.setId(dataSnapshot.getKey());
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

            event.setId(dataSnapshot.getKey());
            int index = events.indexOf(event);

            if(index == -1 ){

                index = Collections.binarySearch(events, event);

                if (index >= 0){
                    events.add(index, event);
                    notifyItemInserted(index);
                }

                else {
                    events.add(-index - 1, event);
                    notifyItemInserted(-index - 1);
                }

            }

            else if(DateConverter.atStartOfDay(events.get(index).getStart())
                    .equals(DateConverter.atStartOfDay(event.getStart()))){
                events.set(index, event);
                notifyItemChanged(index);
            }
            else {
                events.remove(index);
                notifyItemRemoved(index);
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);

        if (event != null) {
            event.setId(dataSnapshot.getKey());
            int index = events.indexOf(event);
            events.remove(index);
            notifyItemRemoved(index);

            if (fragment.getListener() != null){
                fragment.getListener().removeDate(fragment.getDate());
            }

        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i("AdapterSchedule::", s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("AdapterSchedule::", databaseError.getDetails());
    }

}

