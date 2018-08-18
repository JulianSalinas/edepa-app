package edepa.events;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edepa.model.Event;

public class EventsFavorites extends EventsSchedule {

    private List<Event> allEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allEvents = new ArrayList<>();
    }

    @Override
    public void addEvent(Event event) {
         if (favorites.contains(event.getKey()))
            super.addEvent(event);
         if(!allEvents.contains(event))
             allEvents.add(event);
    }

    @Override
    public void addFavorite(String eventKey) {
        super.addFavorite(eventKey);
        Event temp = new Event.Builder().key(eventKey).build();
        int index = allEvents.indexOf(temp);
        if(index != -1) {
            Event event = allEvents.get(index);
            event.setFavorite(true);
            super.addEvent(event);
            eventsAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void removeFavorite(String eventKey) {
        super.removeFavorite(eventKey);
        int index = getFavoriteIndex(eventKey);
        if(index != -1) {
            Event event = events.get(index);
            if (event != null) removeEvent(event);
        }
    }

}
