package edepa.events;

import android.os.Bundle;

import com.google.firebase.database.Query;

import edepa.cloud.CloudEvents;
import edepa.model.EventType;

public class EventsScheduleByType extends EventsSchedule {

    private EventType eventype;

    public static final String TYPE_KEY = "eventype";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(TYPE_KEY)){
            String type = args.getString(TYPE_KEY);
            eventype = EventType.valueOf(type);
        }
    }

    public Query getEventsQuery(){
        return CloudEvents.getEventsQueryUsingType(eventype);
    }

}
