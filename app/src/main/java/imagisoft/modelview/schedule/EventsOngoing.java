package imagisoft.modelview.schedule;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import imagisoft.misc.DateCalculator;
import imagisoft.misc.DateConverter;
import imagisoft.model.ScheduleEvent;

public class EventsOngoing extends EventsFragment {

    /**
     * Solo eventos de esta fecha pueden estar
     * en este fragmento
     */
    protected long date;

    @Override
    public long getDate() {
        long currentTime = System.currentTimeMillis();
        return DateConverter.atStartOfDay(currentTime);
    }

    @Override
    public void bindArguments() {
        // Requerido
    }

    @Override
    protected EventsAdapter getAdapter() {
        return new AdapterOngoing(this);
    }

    public DatabaseReference getReference(){
        return activity.getScheduleReference();
    }

    class AdapterOngoing extends EventsAdapter implements ChildEventListener {

        public AdapterOngoing(EventsFragment view) {
            super(view);
            getReference().addChildEventListener(this);
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

}
