package imagisoft.rommie;

import android.util.Log;
import imagisoft.edepa.ScheduleEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;


public class EventsViewAdapterOngoing
        extends EventsViewAdapter implements ChildEventListener {

    public EventsViewAdapterOngoing(EventsView view) {
        super(view);
        setupReference();
    }

    public void setupReference(){
        view.activity.getOngoingReference().addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.size();
        events.add(index, event);
        notifyItemInserted(index);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.indexOf(event);
        events.set(index, event);
        notifyItemChanged(index);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.indexOf(event);
        events.remove(index);
        notifyItemRemoved(index);
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
