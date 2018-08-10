package edepa.events;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edepa.model.ScheduleEvent;

public class EventsFavorites extends EventsSchedule {

    private List<ScheduleEvent> allEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allEvents = new ArrayList<>();
    }

    @Override
    public void addEvent(ScheduleEvent event) {
         if (favorites.contains(event.getKey()))
            super.addEvent(event);
         if(!allEvents.contains(event))
             allEvents.add(event);
    }

    @Override
    public void addFavorite(String eventKey) {
        super.addFavorite(eventKey);
        ScheduleEvent temp = new ScheduleEvent();
        temp.setKey(eventKey);
        int index = allEvents.indexOf(temp);
        if(index != -1) {
            ScheduleEvent event = allEvents.get(index);
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
            ScheduleEvent event = events.get(index);
            if (event != null) removeEvent(event);
        }
    }

}
