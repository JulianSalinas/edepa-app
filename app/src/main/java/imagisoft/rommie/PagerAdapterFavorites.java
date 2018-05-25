package imagisoft.rommie;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleEvent;


public class PagerAdapterFavorites extends PagerAdapter implements ChildEventListener {

    /**
     * Para agreagar los eventos que solicita la clase padre
     */
    protected FavoriteList favoriteList;

    public PagerAdapterFavorites(PagerFragment fragment) {
        super(fragment);
        favoriteList = FavoriteList.getInstance();
        fragment.activity.getScheduleReference().addChildEventListener(this);
    }

    @Override
    protected EventsFragment createScheduleView(long date) {
        return EventsFragmentFavorites.newInstance(date);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null && favoriteList.getSortedEvents().contains(event)) addPageIfNotExists(event);
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