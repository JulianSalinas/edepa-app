package imagisoft.modelview;


public class EventsFragmentOngoing extends EventsFragment {

    public static EventsFragmentOngoing newInstance() {
        return new EventsFragmentOngoing();
    }

    protected void setupAdapter() {
        if(eventsVA == null)
            eventsVA = new EventsAdapterOngoing(this);
    }

}
