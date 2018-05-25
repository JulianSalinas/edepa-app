package imagisoft.rommie;

import android.os.Bundle;

public class EventsFragmentFavorites extends EventsFragmentWithDate {

    public static EventsFragmentFavorites newInstance(long date) {
        Bundle args = new Bundle();
        EventsFragmentFavorites fragment = new EventsFragmentFavorites();
        args.putLong("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupAdapter() {
        if(eventsVA == null)
            eventsVA = new EventsAdapterFavorites(this);
    }

}
