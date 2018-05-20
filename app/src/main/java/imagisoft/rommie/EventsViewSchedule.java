package imagisoft.rommie;

import android.os.Bundle;


public class EventsViewSchedule extends EventsViewWithDate {

    public static EventsViewSchedule newInstance(String date) {
        Bundle args = new Bundle();
        args.putString("date", date);
        EventsViewSchedule fragment = new EventsViewSchedule();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupAdapter() {
        if(eventsViewAdapter == null)
            eventsViewAdapter = new EventsViewAdapterSchedule(this);
    }

}