package imagisoft.edepa;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Clase encargarda de la administración de los eventos
 * Filtrar eventos por tipom dividir en bloques ...
 */
public class Schedule {

    private ArrayList<ScheduleEvent> events;
    private Hashtable<String, ScheduleEvent> eventsByDay;

    /**
     * Contructores, el primero es requerido por firebase
     */
    public Schedule() {
        this.events = new ArrayList<>();
    }

    public Schedule(ArrayList<ScheduleEvent> events) {
        this.events = events;
    }

    /**
     * Función usada por el generador de datos
     */
    public void addEvent(ScheduleEvent event){
        events.add(event);
    }

    /**
     * Obtienen todos los eventos sin hacer distinciones
     */
    public ArrayList<ScheduleEvent> getEvents(){
        return events;
    }

    /**
     * Divide los eventos por dias (formato dd/mm/yy)
     * Implementa instaciación perezosa
     * @return HashTable (12/12/17, Evento)
     */
    public Hashtable<String, ScheduleEvent> getEventsByDay(){

        if(eventsByDay != null)
            return eventsByDay;

        eventsByDay = new Hashtable<>();
        for(ScheduleEvent event : events)
            eventsByDay.put(UDateConverter.extractDate(event.getStart()), event);

        return eventsByDay;

    }

    /**
     * Funcion para filtrar los eventos por tipo
     * @param filters Tipos que de eventos que se desean obtener
     */
    public ArrayList<ScheduleEvent> getEvents(ScheduleEventType[] filters){
        ArrayList<ScheduleEvent> results = new ArrayList<>();
        for(ScheduleEvent event : events){
            // TODO: Filtro de miedo
        }
        return null;
    }

}
