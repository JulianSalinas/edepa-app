package edepa.custom;

import edepa.modelview.R;

public enum WallpaperLocation {

    BIBLIOTECA (
            "((biblio?t?e?c?a?)|(figueres)|(ferrer))",
            R.drawable.tec_biblioteca),

    CENTRO_ARTES ("(centro)?(\\s|\\w|d)*artes?",
            R.drawable.tec_centro_artes),

    CIC ("((cic)|(centro(\\w|\\s)+investigacion(\\w|\\s)+computacion))",
            R.drawable.tec_edificio_a5),

    EDIFICIO_A4 ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+a4",
            R.drawable.tec_edificio_a4),

    EDIFICIO_A5 ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+a5",
            R.drawable.tec_edificio_a5),

    EDIFICIO_B1 ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+b1",
            R.drawable.tec_edificio_b1),

    EDIFICIO_C1 ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+c1",
            R.drawable.tec_edificio_c1),

    EDIFICIO_D ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+d",
            R.drawable.tec_edificio_d),

    EDIFICIO_F ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+f2?",
            R.drawable.tec_edificio_f2),

    EDIFICIO_B3 ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))\\s+b3?",
            R.drawable.tec_edificio_b3),

    EDIFICIO_ELECTRONICA ("((aula)|(edifi?c?i?o?)|(audito?r?i?o))?((\\w|\\s)+)?electron?i?c?a?",
            R.drawable.tec_edificio_electronica),

    LAIMI_1 ("((labo?r?a?t?o?r?i?o?)|(laimi?))\\s*1",
            R.drawable.tec_laimi_1),

    LAIMI_2 ("((labo?r?a?t?o?r?i?o?)|(laimi?))\\s*2",
            R.drawable.tec_laimi_2),

    LAB_H_AZUL("(((labo?r?a?t?o?r?i?o?)|(laimi?))\\s+h)(\\w|\\s)(rojo)",
            R.drawable.tec_laboratorio_h_rojo),

    LAB_H_ROJO("(((labo?r?a?t?o?r?i?o?)|(laimi?))\\s+h)(\\w|\\s)(azul)",
            R.drawable.tec_laboratorio_h_azul),

    LAB_H("(((labo?r?a?t?o?r?i?o?)|(laimi?))\\s+h)",
            R.drawable.tec_laboratorio_h),

    LAB_208("((aula)|(edifi?c?i?o?)|(audito?r?i?o)|(labo?r?a?t?o?r?i?o?)|(laimi?))\\s+208",
            R.drawable.tec_laboratorio_208),

    LAB_213("((aula)|(edifi?c?i?o?)|(audito?r?i?o)|(labo?r?a?t?o?r?i?o?)|(laimi?))\\s+213",
            R.drawable.tec_laboratorio_213),

    TIERRA_MEDIA("(((labo?r?a?t?o?r?i?o)|(laimi?))\\s+b3\\s*10?)|((tierr\\w+)|(medi\\w+))",
            R.drawable.tec_tierra_media),

    TEC_SEDE_CARTAGO("(tecn?o?l?o?g?i?c?o?[\\s+]?)|(sede((\\w|\\s)+)?cartago)",
            R.drawable.tec_sede_cartago);

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