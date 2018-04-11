package imagisoft.edepa;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;


public class LocaleManager {

    /**
     * Patrón singleton
     */
    private static final LocaleManager ourInstance = new LocaleManager();

    /**
     * Instancia única para que todos accedan al administrador de idiomas/localidades
     */
    public static LocaleManager getInstance() {
        return ourInstance;
    }

    /**
     * Localidad seleccionada
     */
    private static Locale locale;

    /**
     * Se obtiene la localidad.
     * Usada para obtener en que idioma debe ir el resumen
     */
    public static Locale getLocale(){
        return locale;
    }

    /**
     * Constante para saber identificar luego donde cargar y guardar el idioma
     */
    private static final String LOCALE_KEY_VALUE = "LOCALE_KEY_VALUE";

    /**
     * Obtiene un segmento de almacenamiento para guardar el idioma
     * @param context: Actividad desde donde se llama la aplicación
     */
    public SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Se obtiene la localidad de las preferencias del usuario
     * De la localidad se obtiene el idioma, si no se encuentra
     * se coloca espanól por defecto
     */
    public void loadLocale(Context context) {

        SharedPreferences prefs = getSharedPreferences(context);
        String language = prefs.getString(LOCALE_KEY_VALUE, "es");
        changeLocale(context, language);

    }

    /**
     * Se guarda el idioma en ejecucíón para el próximo uso
     */
    public void saveLocale(Context context, String lang) {

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LOCALE_KEY_VALUE, lang);
        editor.apply();

    }

    /**
     * Luego de invocar está función se debe actualizar la actividad para que
     * se usen los nuevos strings
     * @param context: Actividad desde la que se invoca el cambio de idioma
     * @param lang: Puede ser "en" o "es"
     */
    public void changeLocale(Context context, String lang) {

        if (lang.equalsIgnoreCase("")) return;

        locale = new Locale(lang);
        saveLocale(context, lang);

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

}
