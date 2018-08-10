package edepa.interfaces;

import edepa.model.ScheduleEvent;

/**
 * Cualquier objeto que necesite una colección
 * de eventos obtenida de Firebase debe implementar
 * esta interfaz
 */
public interface IEventsSubject extends IFavoritesSubject {

    void addEvent(ScheduleEvent event);
    void changeEvent(ScheduleEvent event);
    void removeEvent(ScheduleEvent event);

}
