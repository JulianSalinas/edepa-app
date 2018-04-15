package imagisoft.edepa;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;


public class FavoriteList extends Preferences {

    /**
     * Instancia única para que todos accedan a la misma lista de favoritos
     */
    private static final FavoriteList ourInstance = new FavoriteList();

    public static FavoriteList getInstance() {
        return ourInstance;
    }

    /**
     * Identifica el archivo json del que se obtienen los favoritos
     */
    private final String key = "FAVORITES";

    /**
     * Bandera para saber si han habido cambios
     */
    private boolean changed = true;

    public boolean isChanged() {
        return changed;
    }

    /**
     * Eventos que el usuario marcó como favoritos
     */
    private List<ScheduleEvent> events;

    public List<ScheduleEvent> getSortedEvents() {

        changed = false;

        Collections.sort(events, (before, after) ->
                before.getStart() >= after.getStart() ? 1 : -1);

        return events;

    }

    /**
     * Se llama cuando el usuario marca la estrellita de un evento
     */
    public void addEvent(ScheduleEvent event){
        changed = true;
        events.add(event);
    }

    /**
     * Se llama cuando el usuario desmarca la estrellita de un evento
     */
    public void removeEvent(ScheduleEvent event){
        changed = true;
        events.remove(event);
    }

    /**
     * Se inicializa la lista de favoritos
     */
    private FavoriteList() {
        events = new ArrayList<>();
    }

    /**
     * Crear un string con formato json a partir de los eventos
     */
    private String getFavoritesAsJson(){
        JSONArray json = new JSONArray();
        for (ScheduleBlock event : events) json.put(event);
        return json.toString();
    }

    /**
     * Carga a partir del archivo de preferencias compartido, los favoritos en la
     * variable events de ésta clase.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void loadFavorites(Context context) {

        SharedPreferences prefs = getSharedPreferences(context);
        if(!prefs.contains(key)) saveFavorites(context);

        try {
            loadFavoritesAux(new JSONArray(prefs.getString(key, " ")));
        }
        catch (JSONException e) {
            Log.i(key, e.getMessage());
        }

    }

    /**
     * Ayuda a la función loadFavorites a manejar el error que se genera al cargar
     * por primera vez la lista de favoritos, ya que, la primer vez no existe
     */
    private void loadFavoritesAux(JSONArray json) throws JSONException{
        for (int i = 0; i < json.length(); i++)
            events.add((ScheduleEvent) json.get(i));
    }

    /**
     * Usa la función getFavoritesAsJson y lo guarda en el archivo compartido de preferencias
     * Si no hay eventos guarda un null
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void saveFavorites(Context context) {
        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, getFavoritesAsJson());
        editor.apply();
    }

}
