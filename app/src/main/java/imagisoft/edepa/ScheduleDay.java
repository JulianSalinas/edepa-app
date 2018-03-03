package imagisoft.edepa;

import java.util.ArrayList;

public class ScheduleDay {

    private Long date;

    private ArrayList<ScheduleBlock> blocks;

    public Long getDate() {
        return date;
    }

    public ArrayList<ScheduleBlock> getBlocks() {
        return blocks;
    }

    public ArrayList<ScheduleBlock> addBlock(ScheduleBlock block){
        blocks.add(block);
        return blocks;
    }

    public ScheduleDay(Long date) {
        this.date = date;
        this.blocks = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleDay)) return false;
        ScheduleDay that = (ScheduleDay) o;
        return date != null ? date.equals(that.date) : that.date == null;
    }

}
