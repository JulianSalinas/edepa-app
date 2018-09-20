package edepa.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Set;


public class Preferences {

    /**
     * Constantes para poder guardar y cargar preferencias
     */
    public static final String LANG_KEY = "LANG_KEY";
    public static final String USER_KEY = "USER_KEY";
    public static final String USE_PHOTO_KEY = "USE_PHOTO_KEY";
    public static final String ALLOW_PHOTO_KEY = "ALLOW_PHOTO_KEY";

    public static final String FIRST_USE_KEY = "FIRST_USE_KEY";
    public static final String FAVORITES_KEY = "FAVORITES_KEY";
    public static final String NOTIFICATIONS_KEY = "NOTIFICATIONS_KEY";
    public static final String THEME_KEY = "THEME_KEY";
    public static final String COMMENTS_KEY = "comments";

    /**
     * Preferencias que afectan la ejecución de la aplicación
     */
    public static final String UPDATE_DELAY = "UPDATE_DELAY";
    public static final String UNREAD_MESSAGES_KEY = "UNREAD_MESSAGES_KEY";

    /**
     * Para definir el tipo de vista de los eventos
     */
    public static final String VIEW_KEY = "VIEW_KEY";
    public static final String VIEW_DEFAULT = "VIEW_DEFAULT";
    public static final String VIEW_BY_TYPE = "VIEW_BY_TYPE";


    /**
     * Obtiene un segmento de almacenamiento para guardar archivos local=e
     * @param context: Actividad desde donde se llama la aplicación
     */
    protected static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Lee un string de las preferencias locales
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     */
    public static String getStringPreference(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(key, null);
    }

    /**
     * Lee un string de las preferencias locales
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     */
    public static Boolean getBooleanPreference(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getBoolean(key, true);
    }

    public static Boolean getBooleanPreference(Context context, String key, boolean failover) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getBoolean(key, failover);
    }

    /**
     * Lee un entero de las preferencias locales
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     */
    public static Integer getIntegerPreference(Context context, String key){
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    public static Set<String> getArrayPreference(Context context, String key){
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getStringSet(key, null);
    }

    /**
     * Guarda un valor en el archivo de preferencias
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     * @param value: Valor de la preferencia a guardar
     */
    public static void setPreference(Context context, String key, Object value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        if(value instanceof String) editor.putString(key, (String) value);
        else if (value instanceof Boolean) editor.putBoolean(key, (Boolean) value);
        else editor.putInt(key, (Integer) value);
        editor.apply();
    }

    /**
     * Remueve una preferencia existente
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe eliminar
     */
    public static void removePreference(Context context, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

}
