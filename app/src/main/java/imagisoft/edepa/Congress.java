package imagisoft.edepa;

import java.util.ArrayList;

public class Congress {

    private String name;

    private Long start;
    private Long end;

    private ArrayList<ScheduleDay>days;

    public String getName() {
        return name;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public ArrayList<ScheduleDay> getDays() {
        return days;
    }

    public Congress(String name, Long start, Long end) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.days = new ArrayList<>();
    }

}
