package edepa.event;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.firebase.database.ValueEventListener;

import edepa.info.MinimapFragment;
import edepa.model.Event;
import edepa.cloud.CloudEvents;
import edepa.custom.CustomFragment;


public abstract class EventHostFragment
        extends CustomFragment implements ValueEventListener {

    public static final String SAVED_EVENT_KEY = "event_state";

    protected Event event;

    public Event getEvent() {
        return event;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null && args.containsKey(SAVED_EVENT_KEY))
            event = args.getParcelable(SAVED_EVENT_KEY);
    }

    /**
     * Guarda el evento que fue pasado como argumento
     * @param outState: Bundle donde se guarda el evento
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_EVENT_KEY, event);
    }

    /**
     * Se carga el evento que había antes de girar la pantalla
     * @param savedInstanceState: Bundle donde se carga el evento
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            event = savedInstanceState.getParcelable(SAVED_EVENT_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
//        CloudEvents
//                .getSingleEventQuery(notice.getKey())
//                .addValueEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        CloudEvents
//                .getSingleEventQuery(notice.getKey())
//                .removeEventListener(this);
    }

    public void openMinimap(){
        Fragment miniMap = new MinimapFragment();
        setFragmentOnScreen(miniMap, "MINIMAP");
    }

    public void openCalendar(){
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEnd())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStart())
                .putExtra(CalendarContract.Events.TITLE, event.getTitle())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocation())
                .putExtra(CalendarContract.Events.CALENDAR_COLOR, event.getEventype().getColorResource())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        getNavigationActivity().startActivity(intent);
    }

    public void updateEvent(Event newValue){
        if (newValue != null) {
            event.setEnd(newValue.getEnd());
            event.setDate(newValue.getDate());
            event.setStart(newValue.getStart());
            event.setTitle(newValue.getTitle());
            event.setLocation(newValue.getLocation());
            event.setEventype(newValue.getEventype());
            event.setBriefSpanish(newValue.getBriefSpanish());
            event.setBriefEnglish(newValue.getBriefEnglish());
            event.setFavorites(newValue.getFavorites());
        }
    }

}
