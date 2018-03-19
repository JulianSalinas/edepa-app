package imagisoft.edepa;

import java.util.ArrayList;

/**
 * Clase encargarda de la administración de los eventos
 * Filtrar eventos por tipom dividir en bloques ...
 */
public class Schedule {


    private ArrayList<ScheduleEvent> events;

    public Schedule() {
        this.events = new ArrayList<>();
    }

    public Schedule(ArrayList<ScheduleEvent> events) {
        this.events = events;
    }

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
     * Funcion para filtrar los eventos por tipo
     * @param filters Tipos que de eventos que se desean obtener
     */
    public ArrayList<ScheduleEvent> getEvents(ScheduleEventType[] filters){
        ArrayList<ScheduleEvent> results = new ArrayList<>();
        for(ScheduleEvent event : events){

        }
        return null;
    }

}
