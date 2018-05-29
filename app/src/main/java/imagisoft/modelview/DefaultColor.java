package imagisoft.modelview;

public enum DefaultColor {

    APP_PRIMARY (R.color.app_primary),
    APP_PRIMARY_DARK (R.color.app_accent),
    APP_ACCENT (R.color.app_accent),
    APP_ACCENT_DARK (R.color.app_accent_dark);

    /**
     * Color predeterminado
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
    DefaultColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
