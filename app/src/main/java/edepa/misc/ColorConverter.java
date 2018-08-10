package edepa.misc;

import android.graphics.Color;

/**
 * Sirve para realizar ciertas operaciones con los colores
 * como aclararlos u oscurecerlos
 */
public class ColorConverter {

    /**
     * Enclarece un color en 12
     * @param color: color base
     * @return color un tono más claro
     */
    public static int lighten(int color) {
        return lighten(color, 12);
    }

    /**
     * Oscurece un color en 12
     * @param color: color base
     * @return color un tono más oscuro
     */
    public static int darken(int color) {
        return darken(color, 12);
    }

    /**
     * Oscurece un color dado
     * @param base: Color base
     * @param amount: Cantidad que se debe oscurecer.
     *                Normalmente 12 para los colores material
     */
    public static int darken(int base, int amount) {
        float[] hsv = new float[3];
        Color.colorToHSV(base, hsv);
        float[] hsl = hsv2hsl(hsv);
        hsl[2] -= amount / 100f;
        if (hsl[2] < 0)
            hsl[2] = 0f;
        hsv = hsl2hsv(hsl);
        return Color.HSVToColor(hsv);
    }

    /**
     * Enclarece un color dado
     * @param base: Color base
     * @param amount: Cantidad que se debe enclarecer
     *                Normalmente 12 para los colores material
     */
    public static int lighten(int base, int amount) {
        float[] hsv = new float[3];
        Color.colorToHSV(base, hsv);
        float[] hsl = hsv2hsl(hsv);
        hsl[2] += amount / 100f;
        if (hsl[2] > 1)
            hsl[2] = 1f;
        hsv = hsl2hsv(hsl);
        return Color.HSVToColor(hsv);
    }

    /**
     * Convierte un color de HSV (Hue, Saturation, Value)
     * a HSL (Hue, Saturation, Lightness)
     * https://gist.github.com/xpansive/1337890
     * @param hsv HSV color array
     * @return hsl color array
     */
    private static float[] hsv2hsl(float[] hsv) {

        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        float nhue = (2f - sat) * val;
        float nsat = sat * val / (nhue < 1f ? nhue : 2f - nhue);

        if (nsat > 1f)
            nsat = 1f;

        return new float[] { hue, nsat, nhue / 2f };

    }

    /**
     * Reversa hsv2hsl
     * https://gist.github.com/xpansive/1337890
     * @param hsl HSL color array
     * @return hsv color array
     */
    private static float[] hsl2hsv(float[] hsl) {

        float hue = hsl[0];
        float sat = hsl[1];
        float light = hsl[2];

        sat *= light < .5 ? light : 1 - light;
        return new float[]{ hue, 2f * sat / (light + sat), light + sat };

    }

}
