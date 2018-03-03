package imagisoft.edepa;

import java.util.ArrayList;

public class ScheduleBlock {

    private Long start;
    private Long end;

    private ArrayList<ScheduleEvent> events;

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public ArrayList<ScheduleEvent> getEvents() {
        return events;
    }

    public ArrayList<ScheduleEvent> addEvent(ScheduleEvent event){
        events.add(event);
        return events;
    }

    public ScheduleBlock(Long start, Long end) {
        this.start = start;
        this.end = end;
        this.events = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleBlock)) return false;
        ScheduleBlock that = (ScheduleBlock) o;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        return end != null ? end.equals(that.end) : that.end == null;
    }

}
