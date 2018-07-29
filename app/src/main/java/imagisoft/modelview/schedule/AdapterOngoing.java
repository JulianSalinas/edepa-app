package imagisoft.modelview.schedule;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.model.ScheduleEvent;

public class AdapterOngoing extends EventsAdapter implements ChildEventListener {


    public AdapterOngoing(EventsFragment view) {
        super(view);
        view
                .getActivityCustom()
                .getScheduleReference()
                .addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null){
            event.setId(dataSnapshot.getKey());
            addEvent(event);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            changeEvent(event);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            removeEvent(event);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(fragment.getTag(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(fragment.getTag(), databaseError.getMessage());
    }

}
