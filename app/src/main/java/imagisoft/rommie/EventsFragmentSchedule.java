package imagisoft.rommie;

import android.os.Bundle;


public class EventsFragmentSchedule extends EventsFragmentWithDate {

    public static EventsFragmentSchedule newInstance(long date) {
        Bundle args = new Bundle();
        EventsFragmentSchedule fragment = new EventsFragmentSchedule();
        args.putLong("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupAdapter() {
        if(eventsVA == null)
            eventsVA = new EventsAdapterSchedule(this);
    }

}