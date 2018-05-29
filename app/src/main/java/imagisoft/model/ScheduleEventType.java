package imagisoft.model;

import imagisoft.modelview.R;

public enum ScheduleEventType {

    /**
     * Cada tipo de evento tiene un color de énfasis
     */
    CONFERENCIA (R.string.text_conference, R.color.material_green),

    TALLER (R.string.text_atelier, R.color.material_red),

    FERIA_EDEPA (R.string.text_fair, R.color.material_amber),

    PONENCIA (R.string.text_presentation, R.color.material_deep_purple);

    /**
     * Para el cronograma se muestran en un color,
     * al mostrar los detalles se muestra un gradiente del
     * mismo tono (resource)
     */
    private final int color;

    /**
     * Obtiene el string relacionado con cada tipo de evento
     * ya sea en ingles o español
     */
    private final int resString;

    /**
     * Getters y Setters de los atributos del enumerado
     */
    public int getColor() {
        return color;
    }

    public int getResString() {
        return resString;
    }

    /**
     * Constructor que solo es necesario para que la clase/enumerado
     * lo identifique. No se usa en práctica
     */
    ScheduleEventType(int resString, int color) {
        this.color = color;
        this.resString = resString;
    }

}