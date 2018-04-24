package imagisoft.rommie;

public enum CustomColor {

    APP_PRIMARY (R.color.app_primary),
    APP_PRIMARY_DARK (R.color.app_primary_dark),
    APP_ACCENT (R.color.app_accent),
    APP_ACCENT_DARK (R.color.app_accent_dark);
//    APP_HEADER_COLOR(R.color.app_detail_light),
//    APP_HEADER_TEXT_COLOR(R.color.app_primary_font);

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
    CustomColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
