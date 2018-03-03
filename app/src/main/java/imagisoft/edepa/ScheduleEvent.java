package imagisoft.edepa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import imagisoft.rommie.ScheduleView;

public class ScheduleEvent {

    private Long id;

    private Long start;
    private Long end;

    private String eventype;
    private String header;
    private String brief;

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

    public String getEventype() {
        return eventype;
    }

    public String getHeader() {
        return header;
    }

    public String getBrief() {
        return brief;
    }

    public ArrayList<Exhibitor> addExhibitor(Exhibitor exhibitor){
        exhibitors.add(exhibitor);
        return this.exhibitors;
    }

    public ScheduleEvent(Long id, Long start, Long end, String eventype, String header, String brief) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.eventype = eventype;
        this.header = header;
        this.brief = brief;
        this.exhibitors = new ArrayList<>();
    }

}
