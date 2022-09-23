package edepa.settings;

import static edepa.model.Preferences.LANG_KEY;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

import edepa.model.Preferences;


public class SettingsLanguage {

    /**
     * Lenguajes disponibles en la aplicación
     */
    public static final String SPANISH = "es";
    public static final String ENGLISH = "en";

    /**
     * Se coloca el idioma de la aplicación en las preferencias
     * Si no ha sido colocado antes, por defecto toma el del teléfono.
     * @param context: Actividad de la que se pueden obtener los recursos
     * @param newLang: SettingsLanguage.SPANISH | SettingsLanguaje.ENGLISH
     */
    public static void setLanguage(Context context, String newLang){

        String currentLang = Preferences.getStringPreference(context, LANG_KEY);
        if (currentLang == null) {
            currentLang = Locale.getDefault().getLanguage();
            Preferences.setPreference(context, LANG_KEY, currentLang);
        }

        if(!currentLang.equals(newLang))
            Preferences.setPreference(context, LANG_KEY, currentLang);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = new Locale(currentLang);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    /**
     * Utilizado antes de iniciar la aplicación para cambiar el
     * lenguaje según las preferecias
     * @param context Actividad de la que se pueden obtener los recursos
     */
    public static void applyLanguage(Context context){
        String currentLang = Preferences.getStringPreference(context, LANG_KEY);
        if (currentLang == null) currentLang = Locale.getDefault().getLanguage();
        setLanguage(context, currentLang);
    }

    /**
     * Se obtiene el lenguaje actual
     * @return SettingsLanguage.SPANISH | SettingsLanguaje.ENGLISH
     */
    public static String getCurrentLanguage(Context context){
        return Preferences.getStringPreference(context, LANG_KEY);
    }

}
