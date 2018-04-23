package imagisoft.edepa;

import java.util.Calendar;

public class Timestamp {

    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Timestamp() {
        this.time = Calendar.getInstance().getTimeInMillis();
    }

    public Timestamp(Long time) {
        this.time = time;
    }

}
