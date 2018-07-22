package imagisoft.modelview;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.misc.DateConverter;
import imagisoft.model.ScheduleEvent;


public class PagerAdapterSchedule
        extends PagerAdapter implements ChildEventListener {

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public PagerAdapterSchedule(PagerFragment fragment) {
        super(fragment);

        fragment.getActivityCustom()
                .getScheduleReference()
                .orderByChild("start")
                .addChildEventListener(this);
    }

    @Override
    protected EventsFragment createScheduleView(long date) {
        return EventsFragmentSchedule.newInstance(date);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            event.setId(dataSnapshot.getKey());
            addPageIfNotExists(event);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null){

            event.setId(dataSnapshot.getKey());
            int index = alreadyPagedEvents.indexOf(event);
            ScheduleEvent original = alreadyPagedEvents.get(index);

            if (!DateConverter.atStartOfDay(original.getStart()).equals(DateConverter.atStartOfDay(event.getStart()))){
                removePageIfLast(original);
                addPageIfNotExists(event);
            }

            Log.i("PagerAdapterSchedule::", "onChildAdded::" + event.getTitle());

        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            event.setId(dataSnapshot.getKey());
            removePageIfLast(event);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}