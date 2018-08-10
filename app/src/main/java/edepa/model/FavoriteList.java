package edepa.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.Type;
import java.util.concurrent.Semaphore;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class FavoriteList extends Preferences implements FavoriteListener{

    /**
     * IDs de los eventos que el usuario marcó
     * como favoritos
     */
    private List<String> eventsIds;

    /**
     * Contiene referencia a todas las clases que necesitan
     * saber el estado de los eventos favoritos
     */
    private List<FavoriteListener> listeners;

    /**
     * Identifica el archivo json del que se obtienen
     * los favoritos
     */
    private static final String key = "FAVORITES";

    /**
     * Instancia única para que todos accedan a la misma
     * lista de favoritos
     */
    private static final FavoriteList ourInstance = new FavoriteList();

    /**
     * Múltiples instancias pueden intentar
     * acceder a la lista al mismo tiempo
     * por lo que mejor se usan semáforos
     */
    private Semaphore semaphoreListeners = new Semaphore(1);
    private Semaphore semaphoreFavorites = new Semaphore(1);

    /**
     * Patrón singleton
     * La clase no se debe instanciar, se debe obtener el
     * único objeto de esta forma
     */
    public static FavoriteList getInstance() {
        return ourInstance;
    }

    /**
     * Se inicializa la lista de favoritos, esto antes de
     * leer los favoritos del json
     */
    private FavoriteList() {
        eventsIds = Collections.synchronizedList(new ArrayList<>());
        listeners = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Agrega una nueva clase que necesita saber el estado de los eventos
     * @param listener: Clase que implementa FavoriteListener
     */
    public void addListener(FavoriteListener listener){
        try { addListenerWrap(listener); }
        catch (InterruptedException e) {
            Log.i(toString(), "Error: " + e.getMessage());
        }

    }

    /**
     * @see #addListener(FavoriteListener)
     * @param listener Clase que implementa FavoriteListener
     * @throws InterruptedException Lanzada por {@link #semaphoreListeners}
     */
    private void addListenerWrap(FavoriteListener listener) throws InterruptedException {
        semaphoreListeners.acquire();

        if(!listeners.contains(listener)) {
            listeners.add(listener);

            for(String eventId: eventsIds)
                listener.onFavoriteAdded(eventId);

        }
        semaphoreListeners.release();
    }

    /**
     * Remueve un objeto que ya no necesita saber el estado de los eventos
     * @param listener: Clase que implementa FavoriteListener
     */
    public void removeListener(FavoriteListener listener){

        try {

            semaphoreListeners.acquire();

            List<FavoriteListener> newlistenters =
                    Collections.synchronizedList(new ArrayList<>());

            for (FavoriteListener currentListener : listeners){
                if(!currentListener.equals(listener))
                    newlistenters.add(currentListener);
                else
                    Log.i("FavoriteList::", "removeEventListener::" + listener.toString());
            }

            this.listeners = newlistenters;

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
        return eventsIds.isEmpty();
    }

    /**
     * Retorna True si el evento está en favoritos
     * @param event: Evento
     * @return True si la lista de favoritos contiene el evento
     */
    public boolean contains(ScheduleEvent event){
        return eventsIds.contains(event.getKey());
    }

    /**
     * Se debe llamar cuando el usuario marca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void addEvent(String event){
        if(!eventsIds.contains(event)) {
            eventsIds.add(event);
            onFavoriteAdded(event);
        }
    }

    /**
     * Se debe llamar cuando el usuario desmarca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void removeEvent(String event){
        if(eventsIds.contains(event)) {
            eventsIds.remove(event);
            onFavoriteRemoved(event);
        }
    }

    @Override
    public void onFavoriteAdded(String event) {

        try {

            semaphoreFavorites.acquire();

            for(FavoriteListener listener : listeners)
                listener.onFavoriteAdded(event);

            semaphoreFavorites.release();

        }
        catch (InterruptedException e) {

            Log.i("FavoriteList::", "onFavoriteAddedError::" + e.getMessage());

        }

    }

    @Override
    public void onFavoriteRemoved(String event) {

        try {

            semaphoreFavorites.acquire();

            for(FavoriteListener listener : listeners)
                listener.onFavoriteRemoved(event);

            semaphoreFavorites.release();

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
    public void load(Context context) {

        // Si es el primer uso de la aplicación, se debe crear el archivo
        // aunque la cantidad de favoritos este vacía en memoria
        Type type = new TypeToken<List<ScheduleEvent>>(){}.getType();
        SharedPreferences prefs = getSharedPreferences(context);
        if(!prefs.contains(key)) save(context);

        // Se guarda toda la lista de favoritos como un json dentro de las
        // preferencias para obtener un acceso más eficiente.
        Gson gson = new Gson();
        String favs = prefs.getString(key, null);
        eventsIds = gson.fromJson(favs, type);

    }

    /**
     * Usa la función getFavoritesAsJson y lo guarda en el archivo de
     * preferencias. Si no hay eventos guarda un null
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void save(Context context) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, gson.toJson(eventsIds));
        editor.apply();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
