package edepa.loaders;

import edepa.model.ScheduleEvent;
import edepa.interfaces.IEventsSubject;

import com.google.firebase.database.DataSnapshot;

/**
 * Clase que sirve para poblar de eventos los fragmentos
 * que implementan {@link IEventsSubject}, antes de usar
 * esta clase resulta conveniente usar antes {@link FavoritesLoader}
 */
public class EventsLoader extends BaseLoader {

    /**
     * Adaptador que contiene los eventos del objeto
     * que se pretende poblar
     */
    protected IEventsSubject eventsSubject;

    /**
     * Contructor
     * @param eventsSubject Objeto que implementa {@link IEventsSubject}
     */
    public EventsLoader(IEventsSubject eventsSubject) {
        this.eventsSubject = eventsSubject;
    }

    /**
     * TODO: Documentar
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            event.setKey(dataSnapshot.getKey());
            eventsSubject.addEvent(event);
        }
    }

    /**
     * TODO: Documentar
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            event.setKey(dataSnapshot.getKey());
            eventsSubject.changeEvent(event);
        }
    }

    /**
     * TODO: Documentar
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null) {
            event.setKey(dataSnapshot.getKey());
            eventsSubject.removeEvent(event);
        }
    }

}
