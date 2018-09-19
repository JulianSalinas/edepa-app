package edepa.event;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.Query;
import edepa.cloud.Cloud;
import edepa.comments.CommentsFragment;
import edepa.model.Event;
import edepa.modelview.R;

import static edepa.event.EventHostFragment.SAVED_EVENT_KEY;


public class EventComments extends CommentsFragment {

    protected Event event;

    public Event getEvent() {
        return event;
    }

    @Override
    public int getResource() {
        return R.layout.comments_screen;
    }

    /**
     * Se obtiene una nueva instancia del fragmento
     * @return EventFragment
     */
    public static EventComments newInstance(Event event) {
        EventComments fragment = new EventComments();
        Bundle args = new Bundle();
        args.putParcelable(SAVED_EVENT_KEY, event);
        fragment.setArguments(args);
        return fragment;
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
    public Query getCommentsQuery() {
        return Cloud.getInstance()
                .getReference("schedule_comments")
                .child(event.getKey())
                .orderByChild("time")
                .limitToLast(200);
    }

}