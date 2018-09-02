package edepa.events;


import android.os.Bundle;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import edepa.cloud.CloudEvents;
import edepa.model.Event;
import edepa.model.EventType;

public class EventsFavoritesByType extends EventsFavorites implements IEventsByType {

    public static final String TYPE_KEY = "type";

    protected EventType eventype;

    public EventType getEventype() {
        return eventype;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(TYPE_KEY)){
            String type = args.getString(TYPE_KEY);
            eventype = EventType.valueOf(type);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de eventos para un fecha
     * en específico (que es pasada al fragmento como arg)
     * @return Query
     */
    public Query getEventsQuery(){
        return CloudEvents.getEventsQueryUsingType(getEventype());
    }

}
