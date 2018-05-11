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
     * Bandera para saber si han habido cambios desde la última consulta
     */
    private boolean changed;

    /**
     * Eventos que el usuario marcó como favoritos y que están en memoria
     */
    private List<ScheduleEvent> events;

    /**
     * Identifica el archivo json del que se obtienen los favoritos
     */
    private static final String key = "FAVORITES";

    /**
     * Instancia única para que todos accedan a la misma lista de favoritos
     */
    private static final FavoriteList ourInstance = new FavoriteList();


    /**
     * La clase no se debe instanciar, se debe obtener el único objeto de esta forma
     */
    public static FavoriteList getInstance() {
        return ourInstance;
    }

    /**
     * Se inicializa la lista de favoritos, esto antes de leer los favoritos del json
     */
    private FavoriteList() {
        events = new ArrayList<>();
    }

    /**
     * Retorna si han habido cambios desde la última consulta
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Consulta todos los favoritos (que están en memoria)
     * Deben ordenarse antes de retornarlos para que se puedan mostrar
     * de la forma esperada por la interfaz de usuario
     */
    public List<ScheduleEvent> getSortedEvents() {

        changed = false;

        // Ordena los elementos (sin crear una lista nueva)
        Collections.sort(events, (before, after) ->
                before.getStart() >= after.getStart() ? 1 : -1);

        return events;

    }

    /**
     * Se deb llamar cuando el usuario marca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void addEvent(ScheduleEvent event){
        changed = true;
        events.add(event);
    }

    /**
     * Se debe llamar cuando el usuario desmarca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void removeEvent(ScheduleEvent event){
        changed = true;
        events.remove(event);
    }

    /**
     * Carga la lista de favoritos en la variable 'events' de éste objeto
     * a partir delarchivo de preferencias.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void loadFavorites(Context context) {

        // Si es el primer uso de la aplicación, se debe crear el archivo
        // aunque la cantidad de favoritos este vacía en memoria
        Type type = new TypeToken<List<ScheduleEvent>>(){}.getType();
        SharedPreferences prefs = getSharedPreferences(context);
        if(!prefs.contains(key)) saveFavorites(context);

        // Se guarda toda la lista de favoritos como un json dentro de las
        // preferencias para obtener un acceso más eficiente.
        Gson gson = new Gson();
        String favs = prefs.getString(key, null);
        events = gson.fromJson(favs, type);


    }

    /**
     * Usa la función getFavoritesAsJson y lo guarda en el archivo de
     * preferencias. Si no hay eventos guarda un null
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void saveFavorites(Context context) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, gson.toJson(events));
        editor.apply();
    }

}
