package imagisoft.modelview;

import android.util.Log;
import imagisoft.model.ScheduleEvent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;


public class EventsAdapterOngoing
        extends EventsAdapter implements ChildEventListener {

    public EventsAdapterOngoing(EventsFragment view) {
        super(view);
        setupReference();
    }

    public void setupReference(){
        fragment.activity
                .getOngoingReference()
                .addChildEventListener(this);
    }

    @Override
    protected String getDateAsString(long start){
        return fragment.activity
                .getResources()
                .getString(R.string.text_ongoing);
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
        Log.i(fragment.getTag(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(fragment.getTag(), databaseError.getDetails());
    }

}
