package imagisoft.edepa;

import imagisoft.rommie.R;

public enum ScheduleEventType {


    CONFERENCIA (R.color.material_pink),
    TALLER (R.color.material_green),
    FERIA_EDEPA ( R.color.material_amber),
    PONENCIA (R.color.material_blue);

    private final int color;

    public int getColor() {
        return color;
    }

    ScheduleEventType(int color) {
        this.color = color;
    }

}