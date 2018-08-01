package imagisoft.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Preferences {

    /**
     * Constantes para poder guardar y cargar preferencias
     */
    public static final String LANG_KEY = "LANG_KEY";
    public static final String USER_KEY = "USER_KEY";
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String FIRST_USE_KEY = "FIRST_USE_KEY";
    public static final String NOTIFICATIONS_KEY = "NOTIFICATIONS_KEY";

    /**
     * Preferencias para activar o desactivar partes de la app
     * NOTA: Las key tienen que coincidir con el nombre en la BD
     */
    public static final String INFO_AVAILABLE_KEY = "INFO";
    public static final String NEWS_AVAILABLE_KEY = "NEWS";
    public static final String CHAT_AVAILABLE_KEY = "CHAT";
    public static final String PALETTE_AVAILABLE_KEY = "PALETTE";
    public static final String PEOPLE_AVAILABLE_KEY = "PEOPLE";

    /**
     * Preferencias que afectan la ejecución de la aplicación
     */
    public static final String UPDATE_DELAY = "UPDATE_DELAY";

    /**
     * Solo permite acceder a una instancia de preferencias
     */
    private static final Preferences ourInstance = new Preferences();

    /**
     * Metódo para que todos accedan a la misma configuración
     */
    public static Preferences getInstance() {
        return ourInstance;
    }

    /**
     * Obtiene un segmento de almacenamiento para guardar archivos local=e
     * @param context: Actividad desde donde se llama la aplicación
     */
    protected SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Retorna una instancia en la que se pueden editar las preferencias
     *  @param context: Actividad desde donde se llama la aplicación
     */
    protected SharedPreferences.Editor getSharedEditor(Context context){
        return getSharedPreferences(context).edit();
    }

    /**
     * Lee un string de las preferencias locales
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     */
    public String getStringPreference(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(key, null);
    }

    /**
     * Lee un string de las preferencias locales
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     */
    public Boolean getBooleanPreference(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getBoolean(key, true);
    }

    /**
     * Lee un entero de las preferencias locales
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     */
    public Integer getIntegerPreference(Context context, String key){
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    /**
     * Guarda un valor en el archivo de preferencias
     * @param context: Actividad desde donde se llama la aplicación
     * @param key: Asociado al valor que se debe obtener
     * @param value: Valor de la preferencia a guardar
     */
    public void setPreference(Context context, String key, Object value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        if(value instanceof String) editor.putString(key, (String) value);
        else if (value instanceof Boolean) editor.putBoolean(key, (Boolean) value);
        else editor.putInt(key, (Integer) value);
        editor.apply();
    }

}
