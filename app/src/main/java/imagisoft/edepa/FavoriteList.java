package imagisoft.edepa;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class FavoriteList {

    /**
     * Patrón singleton
     */
    private static final FavoriteList ourInstance = new FavoriteList();

    /**
     * Instancia única para que todos accedan a la misma lista de favoritos
     */
    public static FavoriteList getInstance() {
        return ourInstance;
    }

    /**
     * Identifica el archivo json del que se obtienen los favoritos
     */
    private String key;

    /**
     * Eventos que el usuario marcó como favoritos
     */
    private List<ScheduleBlock> events;

    /**
     * Se obtiene los eventos favoritos del usuario
     */
    public List<ScheduleBlock> getEvents() {
        return events;
    }

    /**
     * Se llama cuando el usuario marca la estrellita de un evento
     */
    public void addEvent(ScheduleEvent event){
        events.add(event);
    }

    /**
     * Se llama cuando el usuario desmarca la estrellita de un evento
     */
    public void removeEvent(ScheduleEvent event){
        events.remove(event);
    }

    /**
     * Se inicializa la lista de favoritos
     */
    private FavoriteList() {

        key = "FAVORITES";
        events = new ArrayList<>();

    }

    /**
     * Obtiene un segmento de almacenamiento para guardar los eventos
     * @param context: Actividad desde donde se llama la aplicación
     */
    public SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Crear un string con formato json a partir de los eventos
     */
    public String getFavoritesAsJson(){

        JSONArray json = new JSONArray();
        for (ScheduleBlock event : events) json.put(event);
        return json.toString();

    }

    /**
     * Carga a partir del archivo de preferencias compartido, los favoritos en la
     * variable events de ésta clase.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public List<ScheduleBlock> loadFavorites(Context context) {

        SharedPreferences prefs = getSharedPreferences(context);
        try { return loadFavoritesAux(new JSONArray(prefs.getString(key, null))); }
        catch (JSONException e) { return events; }

    }

    /**
     * Usa la función getFavoritesAsJson y lo guarda en el archivo compartido de preferencias
     * Si no hay eventos guarda un null
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void saveFavorites(Context context) {

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, !events.isEmpty() ? getFavoritesAsJson() : null);
        editor.apply();

    }

    /**
     * Ayuda a la función loadFavorites a manejar el error que se genera al cargar
     * por primera vez la lista de favoritos, ya que, la primer vez no existe
     */
    public List<ScheduleBlock> loadFavoritesAux(JSONArray json) throws JSONException{

        for (int i = 0; i < json.length(); i++) events.add((ScheduleEvent) json.get(i));
        return events;

    }

}
