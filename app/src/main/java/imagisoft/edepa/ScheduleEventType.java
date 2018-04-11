package imagisoft.edepa;

import imagisoft.rommie.R;

public enum ScheduleEventType {

    /**
     * Cada tipo de evento tiene un color de énfasis
     */
    CONFERENCIA (
            R.color.material_green,
            R.drawable.gradient_quepal),

    TALLER (
            R.color.material_red,
            R.drawable.gradient_cherry),

    FERIA_EDEPA (
            R.color.material_amber,
            R.drawable.gradient_sunkist),

    PONENCIA (
            R.color.material_deep_purple,
            R.drawable.gradient_purplin);

    /**
     * Para el cronograma se muestran en un color,
     * al mostrar los detalles se muestra un gradiente del
     * mismo tono (resource)
     */
    private final int color;
    private final int resource;

    /**
     * Getters y Setters de los atributos del enumerado
     */
    public int getColor() {
        return color;
    }

    public int getResource() {
        return resource;
    }

    /**
     * Constructor que solo es necesario para que la clase/enumerado
     * lo identifique. No se usa en práctica
     */
    ScheduleEventType(int color, int resource) {

        this.color = color;
        this.resource = resource;

    }

}