package imagisoft.modelview.schedule;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Collections;

import imagisoft.misc.DateConverter;
import imagisoft.model.ScheduleEvent;


public class EventsSchedule extends EventsFragment {

    private long date;

    @Override
    public long getDate() {
        return date;
    }

    @Override
    protected EventsAdapter getAdapter() {
        return new AdapterSchedule(this);
    }

    @Override
    public void bindArguments(){
        Bundle args = getArguments();
        if (args != null && args.containsKey("date"))
            this.date = args.getLong("date");
        Log.i(toString(), "My date is " + DateConverter.extractDate(date));
    }

    public class AdapterSchedule
            extends EventsAdapter implements ChildEventListener {

        public AdapterSchedule(EventsFragment view) {
            super(view);
            activity.getScheduleReference()
                    .orderByChild("start")
                    .startAt(date)
                    .endAt(DateConverter.atEndOFDay(date))
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

                if (pagerListener != null){
                    pagerListener.removeDate(getDate());
                }

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

}
