package imagisoft.modelview;

import android.util.Log;

import imagisoft.model.ScheduleEvent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;


public class EventsAdapterOngoing
        extends EventsAdapter implements ChildEventListener {

    protected boolean isListenerSet;
    protected DatabaseReference reference;

    public EventsAdapterOngoing(EventsFragment view) {
        super(view);
        setupReference();
        setEventListener();
    }

    public void setupReference(){

        long currentTime = System.currentTimeMillis();

//        if(reference == null)
//            reference = fragment.activity.getScheduleReference()
//                    .orderByChild("start")
//                    .startAt(DateConverter.atStartOfDay(currentTime))
//                    .endAt(DateConverter.atEndOFDay(currentTime))
//                    .getRef();
    }

    public void removeEventListener(){
        if(reference != null && isListenerSet) {
            reference.removeEventListener(this);
            isListenerSet = false;
        }
    }

    public void setEventListener(){
        if(reference != null && !isListenerSet) {
            reference.addChildEventListener(this);
            isListenerSet = true;
        }
    }

    @Override
    protected String getDateAsString(long start){
        return fragment.getActivityCustom()
                .getResources()
                .getString(R.string.text_ongoing);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null){

            long currentTime = System.currentTimeMillis();

            if (event.getStart() <= currentTime && currentTime < event.getEnd()){
                event.setId(dataSnapshot.getKey());
                int index = events.size();
                events.add(index, event);
                notifyItemInserted(index);
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);

        if(event != null) {

            long currentTime = System.currentTimeMillis();

            if (event.getStart() <= currentTime && currentTime < event.getEnd()) {
                event.setId(dataSnapshot.getKey());
                int index = events.indexOf(event);
                events.set(index, event);
                notifyItemChanged(index);
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            int index = events.indexOf(event);
            events.remove(index);
            notifyItemRemoved(index);
        }
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
