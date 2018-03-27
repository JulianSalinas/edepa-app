package imagisoft.edepa;

import java.util.ArrayList;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Clase encargarda de la administración de los eventos
 * Filtrar eventos por tipom dividir en bloques ...
 */
public class Schedule {

    private ArrayList<ScheduleEvent> events;
    private LinkedMultiValueMap<String, ScheduleBlock> eventsByDay;

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
     * TODO: Arreglar
     */
    public ArrayList<ScheduleBlock> getEvents(){
        ArrayList<ScheduleBlock> eventsOrBlocks = new ArrayList<>();
        eventsOrBlocks.addAll(events);
        return eventsOrBlocks;
    }

    /**
     * Divide los eventos por dias (formato dd/mm/yy)
     * Implementa instaciación perezosa
     * @return HashTable (12/12/17, Evento)
     * TODO: Arreglar
     */
    public LinkedMultiValueMap<String, ScheduleBlock> getEventsByDay(){

        if(eventsByDay != null)
            return eventsByDay;

        eventsByDay = new LinkedMultiValueMap<>();
        for(ScheduleEvent event : events)
            eventsByDay.add(UDateConverter.extractDate(event.getStart()), event);

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
