package imagisoft.modelview;

import android.os.Bundle;

import imagisoft.model.FavoriteList;
import imagisoft.model.FavoriteListener;

public class EventsFragmentFavorites extends EventsFragmentWithDate {

    public static EventsFragmentFavorites newInstance(long date) {
        Bundle args = new Bundle();
        EventsFragmentFavorites fragment = new EventsFragmentFavorites();
        args.putLong("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavoriteList.getInstance()
                .addListener((FavoriteListener) eventsVA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FavoriteList.getInstance()
                .removeListener((FavoriteListener) eventsVA);
    }

    @Override
    protected void setupAdapter() {
        if(eventsVA == null)
            eventsVA = new EventsAdapterFavorites(this);
    }

}
