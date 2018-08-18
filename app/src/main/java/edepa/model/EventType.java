package edepa.model;

import edepa.modelview.R;


public enum EventType {

    /**
     * Cada tipo de evento tiene un color de énfasis
     */
    TALLER (R.string.text_atelier, R.color.material_red),
    FERIA_EDEPA (R.string.text_fair, R.color.material_amber),
    CONFERENCIA (R.string.text_conference, R.color.material_green),
    PONENCIA (R.string.text_presentation, R.color.material_deep_purple);

    /**
     * Para el cronograma se muestran en un color,
     * al mostrar los detalles se muestra un gradiente del
     * mismo tono (resource)
     */
    private final int colorResource;

    /**
     * Obtiene el string relacionado con cada tipo de evento
     * ya sea en ingles o español
     */
    private final int stringResource;

    /**
     * Getters y Setters de los atributos del enumerado
     */
    public int getColorResource() {
        return colorResource;
    }

    public int getStringResource() {
        return stringResource;
    }

    /**
     * Constructor que solo es necesario para que la clase/enumerado
     * lo identifique. No se usa en práctica
     */
    EventType(int stringResource, int colorResource) {
        this.colorResource = colorResource;
        this.stringResource = stringResource;
    }

}