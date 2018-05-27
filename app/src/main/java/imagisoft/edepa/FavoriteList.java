package imagisoft.edepa;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;


public class FavoriteList extends Preferences implements FavoriteListener{

    /**
     * Eventos que el usuario marcó como favoritos y que están en memoria
     */
    private List<ScheduleEvent> events;

    /**
     * Contiene referencia a todas las clases que necesitan saber el
     * estado de los eventos favoritos
     */
    private List<FavoriteListener> listeners;

    /**
     * Identifica el archivo json del que se obtienen los favoritos
     */
    private static final String key = "FAVORITES";

    /**
     * Instancia única para que todos accedan a la misma lista de favoritos
     */
    private static final FavoriteList ourInstance = new FavoriteList();

    private Semaphore semaphoreListeners = new Semaphore(1);
    private Semaphore semaphoreFavorites = new Semaphore(1);

    /**
     * Patrón singleton
     * La clase no se debe instanciar, se debe obtener el único objeto de esta forma
     */
    public static FavoriteList getInstance() {
        return ourInstance;
    }

    /**
     * Se inicializa la lista de favoritos, esto antes de leer los favoritos del json
     */
    private FavoriteList() {
        events = Collections.synchronizedList(new ArrayList<>());
        listeners = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Agrega una nueva clase que necesita saber el estado de los eventos
     * @param listener: Clase que implementa FavoriteListener
     */
    public void addListener(FavoriteListener listener){

        try {

            semaphoreListeners.acquire();

            if(!listeners.contains(listener)) {
                listeners.add(listener);
                Log.i("FavoriteList::", "addListener::" + listener.toString());

                String str = "";
                for(ScheduleEvent event: events){
                    str = str.concat(event.getId() + ". ");
                }
                Log.i("FavoriteList::", "currentFavorites::" + str);

            }

            for(ScheduleEvent event: events)
                listener.onFavoriteAdded(event);

            semaphoreListeners.release();

        }
        catch (InterruptedException e) {

            Log.i("FavoriteList::", "addListenerError::" + e.getMessage());

        }

    }

    /**
     * Remueve un objeto que ya no necesita saber el estado de los eventos
     * @param listener: Clase que implementa FavoriteListener
     */
    public void removeListener(FavoriteListener listener){

        try {

            semaphoreListeners.acquire();

            if(listeners.contains(listener)) {
                listeners.remove(listener);
                Log.i("FavoriteList::", "removeListener::" + listener.toString());
            }

            semaphoreListeners.release();

        }
        catch (InterruptedException e) {

            Log.i("FavoriteList::", "removeListenerError::" + e.getMessage());

        }

    }

    public void removeAllListeners(){

        try {
            semaphoreListeners.acquire();
            listeners.clear();
            semaphoreListeners.release();
        }
        catch (InterruptedException e) {

            Log.i("FavoriteList::", "removeAllListenersError::" + e.getMessage());

        }
    }

    /**
     * Retorna true si no hay favoritos
     * @return True si la lista está vacía
     */
    public boolean isEmpty(){
        return events.isEmpty();
    }

    /**
     * Retorna True si el evento está en favoritos
     * @param event: Evento
     * @return True si la lista de favoritos contiene el evento
     */
    public boolean contains(ScheduleEvent event){
        return events.contains(event);
    }

    /**
     * Se debe llamar cuando el usuario marca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void addEvent(ScheduleEvent event){
        if(!events.contains(event)) {
            events.add(event);
            onFavoriteAdded(event);
            Log.i("FavoriteList::", "addEvent::" + event.getId());
        }
    }

    /**
     * Se debe llamar cuando el usuario desmarca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void removeEvent(ScheduleEvent event){
        if(events.contains(event)) {
            events.remove(event);
            onFavoriteRemoved(event);
            Log.i("FavoriteList::", "removeEvent::" + event.getId());
        }
    }

    @Override
    public void onFavoriteAdded(ScheduleEvent event) {

        try {

            semaphoreFavorites.acquire();

            for(FavoriteListener listener : listeners)
                listener.onFavoriteAdded(event);

            semaphoreFavorites.release();

            Log.i("FavoriteList::", "onFavoriteAdded::" + event.getId());

        }
        catch (InterruptedException e) {

            Log.i("FavoriteList::", "onFavoriteAddedError::" + e.getMessage());

        }

    }

    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {

        try {

            semaphoreFavorites.acquire();

            for(FavoriteListener listener : listeners)
                listener.onFavoriteRemoved(event);

            semaphoreFavorites.release();

            Log.i("FavoriteList::", "onFavoriteRemoved::" + event.getId());

        }
        catch (InterruptedException e) {

            Log.i("FavoriteList::", "onFavoriteRemovedError::" + e.getMessage());

        }

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
