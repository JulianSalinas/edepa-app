package imagisoft.edepa;

import imagisoft.rommie.R;

public enum ScheduleEventType {

    CONFERENCIA (R.color.material_green, R.drawable.gradient_quepal),
    TALLER (R.color.material_red, R.drawable.gradient_cherry),
    FERIA_EDEPA ( R.color.material_amber, R.drawable.gradient_sunkist),
    PONENCIA (R.color.material_deep_purple, R.drawable.gradient_purplin);

    private final int color;
    private final int resource;

    public int getColor() {
        return color;
    }

    public int getResource() {
        return resource;
    }

    ScheduleEventType(int color, int resource) {
        this.color = color;
        this.resource = resource;
    }

}