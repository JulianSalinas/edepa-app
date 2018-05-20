package imagisoft.rommie;

import android.os.Bundle;

import java.util.List;
import java.util.ArrayList;

import imagisoft.edepa.ScheduleEvent;

public class EventsViewFavorites extends EventsViewWithDate {

    public static EventsViewFavorites newInstance(String date) {
        Bundle args = new Bundle();
        args.putString("date", date);
        EventsViewFavorites fragment = new EventsViewFavorites();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupAdapter() {
        if(eventsViewAdapter == null)
            eventsViewAdapter = new EventsViewAdapterFavorites(this);
    }

}
