package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.FavoriteListener;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;


public class PagerAdapterFavorites
        extends PagerAdapter implements FavoriteListener{

    /**
     * Para agreagar los eventos que solicita la clase padre
     */
    protected FavoriteList favoriteList;

    public PagerAdapterFavorites(PagerFragment fragment) {
        super(fragment);
        favoriteList = FavoriteList.getInstance();
    }

    @Override
    protected EventsFragment createScheduleView(long date) {
        return EventsFragmentFavorites.newInstance(date);
    }

    @Override
    public void onFavoriteAdded(ScheduleEvent event) {
        if (event != null) addPageIfNotExists(event);
    }

    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {
        if (event != null) removePageIfLast(event);
    }

}