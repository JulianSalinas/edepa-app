package imagisoft.edepa;

import java.util.ArrayList;

public class Schedule {

    private ArrayList<ScheduleEvent> events;

    public Schedule() {
        events = new ArrayList<>();
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
