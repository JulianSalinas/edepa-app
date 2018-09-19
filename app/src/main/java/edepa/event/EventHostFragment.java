package edepa.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.firebase.database.ValueEventListener;

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
        CloudEvents
                .getSingleEventQuery(event.getKey())
                .addValueEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        CloudEvents
                .getSingleEventQuery(event.getKey())
                .removeEventListener(this);
    }

    public abstract void updateEvent(Event event);

}
