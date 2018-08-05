package imagisoft.modelview.schedule.events;

import android.content.Context;

import com.google.firebase.database.Query;

import imagisoft.model.Cloud;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.loaders.FavoritesLoader;

public class EventsFavorites extends EventsFragment {

    private FavoritesLoader favoritesLoader;

    @Override
    protected Query getFavoritesQuery() {
        Cloud cloud = Cloud.getInstance();
        String uid = cloud.getAuth().getUid();
        assert uid != null;
        return cloud.getReference(Cloud.FAVORITES).child(uid);
    }

    @Override
    public Query getScheduleQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("start")
                .equalTo(getDate());
    }

    @Override
    protected EventsAdapter instantiateAdapter() {
        return new AdapterFavorites(getContext());
    }

    public class AdapterFavorites extends EventsAdapter {

        /**
         * Constructor
         */
        public AdapterFavorites(Context context) {
            super(context);
        }

        @Override
        public void addEvent(ScheduleEvent event) {
            super.addEvent(event);
        }

        @Override
        public void addFavorite(String eventKey) {

        }

        @Override
        public void removeFavorite(String eventKey) {

        }

    }

}
