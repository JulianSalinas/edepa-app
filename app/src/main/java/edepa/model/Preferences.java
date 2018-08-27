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
    public static final String FIRST_USE_KEY = "FIRST_USE_KEY";
    public static final String NOTIFICATIONS_KEY = "NOTIFICATIONS_KEY";
    public static final String THEME_KEY = "THEME_KEY";

    /**
     * Preferencias para activar o desactivar partes de la app
     * NOTA: Las key tienen que coincidir con el nombre en la BD
     */
    public static final String PEOPLE_AVAILABLE_KEY = "PEOPLE";

    /**
     * Preferencias que afectan la ejecución de la aplicación
     */
    public static final String UPDATE_DELAY = "UPDATE_DELAY";
    public static final String UNREAD_MESSAGES_KEY = "UNREAD_MESSAGES_KEY";

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

    public static void setArrayPreference(Context context, String key, Set<String> values) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putStringSet(key, values).apply();
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
