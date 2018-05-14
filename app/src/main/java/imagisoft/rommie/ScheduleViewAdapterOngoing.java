package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import imagisoft.edepa.ScheduleEvent;

public class ScheduleViewAdapterOngoing extends ScheduleViewAdapter{

    public ScheduleViewAdapterOngoing(MainActivityFragment scheduleView) {

        super(scheduleView);

        scheduleView.activity.getOngoingReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
                events.add(0, event);
                notifyItemInserted(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
                events.set(0, event);
                notifyItemChanged(0);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
                int index = events.indexOf(event);
                events.remove(index);
                notifyItemRemoved(0);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("SVAdapterOnline", databaseError.getDetails());
            }

        });
    }



}
