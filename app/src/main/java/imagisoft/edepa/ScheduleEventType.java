package imagisoft.edepa;

import imagisoft.rommie.R;

public enum ScheduleEventType {

    /**
     * Cada tipo de evento tiene un color de énfasis
     */
    CONFERENCIA (R.color.material_green),

    TALLER (R.color.material_red ),

    FERIA_EDEPA (R.color.material_amber),

    PONENCIA (R.color.material_deep_purple);

    /**
     * Para el cronograma se muestran en un color,
     * al mostrar los detalles se muestra un gradiente del
     * mismo tono (resource)
     */
    private final int color;

    /**
     * Getters y Setters de los atributos del enumerado
     */
    public int getColor() {
        return color;
    }

    /**
     * Constructor que solo es necesario para que la clase/enumerado
     * lo identifique. No se usa en práctica
     */
    ScheduleEventType(int color) {
        this.color = color;
    }

}