package imagisoft.edepa;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
     * Carga a partir del archivo de preferencias compartido, los favoritos en la
     * variable events de ésta clase.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void loadFavorites(Context context) {

        Type type = new TypeToken<List<ScheduleEvent>>(){}.getType();
        SharedPreferences prefs = getSharedPreferences(context);
        if(!prefs.contains(key)) saveFavorites(context);

        Gson gson = new Gson();
        String favs = prefs.getString(key, null);
        events = gson.fromJson(favs, type);


    }

    /**
     * Usa la función getFavoritesAsJson y lo guarda en el archivo compartido de preferencias
     * Si no hay eventos guarda un null
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void saveFavorites(Context context) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, gson.toJson(events));
        editor.apply();
    }

}
