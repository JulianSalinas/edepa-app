package imagisoft.rommie;


public class EventsViewOngoing extends EventsView{

    public static EventsViewOngoing newInstance() {
        return new EventsViewOngoing();
    }

    protected void setupAdapter() {
        if(eventsViewAdapter == null)
            eventsViewAdapter = new EventsViewAdapterOngoing(this);
    }

}
