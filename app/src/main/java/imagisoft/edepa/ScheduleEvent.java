package imagisoft.edepa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import imagisoft.rommie.ScheduleView;

public class ScheduleEvent {

    private Long id;
    private Long start;
    private Long end;
    private String header;
    private String brief;
    private EventType eventype;

    private ArrayList<Exhibitor> exhibitors;

    public Long getId() {
        return id;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public String getHeader() {
        return header;
    }

    public String getBrief() {
        return brief;
    }

    public EventType getEventype() {
        return eventype;
    }

    public ArrayList<Exhibitor> addExhibitor(Exhibitor exhibitor){
        exhibitors.add(exhibitor);
        return this.exhibitors;
    }

    public ScheduleEvent(Long id, Long start, Long end, String header, String brief, EventType eventype) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.header = header;
        this.brief = brief;
        this.eventype = eventype;
        this.exhibitors = new ArrayList<>();
    }

}
