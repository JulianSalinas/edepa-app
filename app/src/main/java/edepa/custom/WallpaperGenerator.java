package edepa.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;

import edepa.model.Event;
import edepa.minilibs.RegexSearcher;
import edepa.modelview.R;

/**
 * Clase utilizada para obtener la imagen de un lugar
 * según un texto. Se utilizar expresiones regulares
 * para buscar la la imagen que mejor coincida con el texto
 */
public class WallpaperGenerator {

    public static final int NO_IMAGE_FOUND = -404;

    /**
     * Necesario para poder acceder a los recursos
     */
    private Context context;

    public WallpaperGenerator(Context context) {
        this.context = context;
    }

    /**
     * A partir de un evento se retorna un Drawable con la imagen
     * @param event contiene el texto (localización) del evento
     *              que se necesita para obtener la imagen
     * @return DrawableRes con la imagen buscada
     */
    public int getWallpaper(Event event){
        int resource = parseText(event.getLocation());

        // No se encontró imagen
        // TODO: Retornar una por defecto.
        // TODO: Por ahora se colocar el accent de enfásis
        if (resource == NO_IMAGE_FOUND)
            resource = R.drawable.img_pattern;
        return resource;
    }

    /**
     * Traduce el texto dado a su respectiva imagen. Por ejemplo
     * si se recibe 'Centro de las artes' se retorna la fotografía
     * del Centro de las artes. Si el texto no guarda relación con
     * ninguna imagen, se retorna un Drawable por defecto
     * @param text Localización
     * @return R.drawable.tec_image o {@link #NO_IMAGE_FOUND}
     */
    public int parseText(String text){
        text = RegexSearcher.normalize(text);
        for (WallpaperLocation location : WallpaperLocation.values()){
            String query = location.getRegex();
            if(RegexSearcher.autoSearch(query, text).size() > 0) {
                return location.getResource();
            }
        }
        return NO_IMAGE_FOUND;
    }

}
