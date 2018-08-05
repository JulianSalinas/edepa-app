package imagisoft.modelview.interfaces;

import imagisoft.model.ScheduleEvent;

/**
 * Cualquier objeto que necesite una colección
 * de eventos obtenida de Firebase debe implementar
 * esta interfaz
 */
public interface IEventsSubject {

    void addEvent(ScheduleEvent event);
    void changeEvent(ScheduleEvent event);
    void removeEvent(ScheduleEvent event);

    void addFavorite(String eventKey);
    void removeFavorite(String eventKey);

}
