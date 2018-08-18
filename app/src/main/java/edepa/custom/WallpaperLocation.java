package edepa.custom;

import edepa.modelview.R;

public enum WallpaperLocation {

    BIBLIOTECA (
            "((biblio?t?e?c?a?)|(figueres)|(ferrer))",
            R.drawable.tec_biblioteca),

    CENTRO_ARTES ("(centro)?(\\\\s|\\\\w|d)*artes?",
            R.drawable.tec_centro_artes),

    EDIFICIO_A4 ("((edifi?c?i?o?)|(audito?r?i?o))\\\\s+a(4|5)",
            R.drawable.tec_biblioteca),

    EDIFICIO_C1 ("((edifi?c?i?o?)|(audito?r?i?o))\\\\s+c1",
            R.drawable.tec_edificio_c1),

    EDIFICIO_D ("((edifi?c?i?o?)|(audito?r?i?o))\\s+d",
            R.drawable.tec_edificio_d),

    EDIFICIO_F ("((edifi?c?i?o?)|(audito?r?i?o))\\s+f2",
            R.drawable.tec_edificio_f2),

    LAIMI_1 ("((labo?r?a?t?o?r?i?o)|(laimi?))\\s*1?",
            R.drawable.tec_laimi_1),

    LAIMI_2 ("((labo?r?a?t?o?r?i?o)|(laimi?))\\s*2?",
            R.drawable.tec_laimi_2),

    TIERRA_MEDIO ("(((labo?r?a?t?o?r?i?o)|(laimi?))\\s+b3\\s*10?)|((tierr\\w+)|(medi\\w+))",
            R.drawable.tec_tierra_media);

    private final String regex;

    private final int resource;

    public String getRegex() {
        return regex;
    }

    public int getResource() {
        return resource;
    }

    WallpaperLocation(String regex, int resource) {
        this.regex = regex;
        this.resource = resource;
    }

}