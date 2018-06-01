package imagisoft.modelview;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Collections;

import imagisoft.model.FavoriteList;
import imagisoft.model.FavoriteListener;
import imagisoft.model.ScheduleEvent;
import imagisoft.misc.DateConverter;


public class EventsAdapterFavorites
        extends EventsAdapter implements ChildEventListener, FavoriteListener {

    private long date;
    private FavoriteList favoriteList;

    public EventsAdapterFavorites(EventsFragment view) {

        super(view);
        this.date = ((EventsFragmentFavorites) view).getDate();
        this.favoriteList = FavoriteList.getInstance();

        view.activity
                .getScheduleReference()
                .orderByChild("start")
                .startAt(date)
                .endAt(DateConverter.atEndOFDay(date))
                .addChildEventListener(this);

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null && events.contains(event)) {
            event.setId(dataSnapshot.getKey());
            int index = events.indexOf(event);
            events.set(index, event);
            notifyItemChanged(index);
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null && favoriteList.contains(event)) {

            event.setId(dataSnapshot.getKey());
            int index = events.indexOf(event);

            if(index == -1){

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

            fragment.showStatusMessage("Un evento en tus favoritos ha sido actualizado");
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null && favoriteList.contains(event)) {
            event.setId(dataSnapshot.getKey());
            int index = events.indexOf(event);
            events.remove(index);
            notifyItemRemoved(index);
            fragment.showStatusMessage("Un evento en tus favoritos ya no está disponible");
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

    @Override
    public void onFavoriteAdded(ScheduleEvent event) {

        long eventDate = DateConverter.atStartOfDay(event.getStart());

        if(!events.contains(event) && eventDate == date) {

//            int index = Collections.binarySearch(events, event);
//
//            if (index >= 0) {
//                events.add(index, event);
//                notifyItemInserted(index);
//            }
//            else {
//                events.add(-index - 1, event);
//                notifyItemInserted(-index - 1);
//            }
            events.add(0, event);
            notifyItemInserted(0);
            Log.i("EventsAdapterFav::", "onFavoriteAdded::" + event.getId());

        }

    }

    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {
        if(events.contains(event)) {
            int index = events.indexOf(event);
            events.remove(index);
            notifyItemRemoved(index);
        }
    }

}
