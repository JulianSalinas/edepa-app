package imagisoft.rommie;

import android.support.v4.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.edepa.ScheduleEvent;


public class PagerAdapterSchedule
        extends PagerAdapter implements ChildEventListener {

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public PagerAdapterSchedule(PagerFragment fragment) {
        super(fragment);

        fragment.activity
                .getScheduleReference()
                .orderByChild("start")
                .addChildEventListener(this);
        
    }

    @Override
    protected EventsView createScheduleView(String date) {
        return EventsViewSchedule.newInstance(date);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) addPageIfNotExists(event);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) removePageIfLast(event);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}