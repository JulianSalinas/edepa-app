package imagisoft.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewedList extends Preferences {

    private List<String> keys;

    private static final String key = "SEEN";

    /**
     * Instancia única para que todos accedan a la misma
     * lista de noticias vistas
     */
    private static final ViewedList ourInstance = new ViewedList();

    /**
     * Patrón singleton
     * La clase no se debe instanciar, se debe obtener el único objeto de esta forma
     */
    public static ViewedList getInstance() {
        return ourInstance;
    }

    private ViewedList() {
        keys = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Retorna true si no hay noticias vistas
     * @return True si la lista está vacía
     */
    public boolean isEmpty(){
        return keys.isEmpty();
    }

    /**
     * Retorna True si la noticia ya ha sido vista
     * @param newMsg: Noticia
     * @return True si la noticia ya ha sido vista
     */
    public boolean isRead(Message newMsg){
        return keys.contains(newMsg.getKey());
    }

    /**
     * Se debe llamar cuando el usuario marca la estrellita de un evento
     * La bandera de cambios se activa
     */
    public void markAsRead(Message newMsg){
        String key = newMsg.getKey();
        if(!keys.contains(key)) {
            if(keys.size() > 1000)
                keys.remove(0);
            keys.add(key);
            Log.i("ViewedList::", "addNew::" + key);
        }
    }

    public void unmarkAsRead(Message newMsg){
        String key = newMsg.getKey();
        if(keys.contains(key)) {
            keys.remove(key);
            Log.i("ViewedList::", "removeNew::" + key);
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
        Type type = new TypeToken<List<String>>(){}.getType();
        SharedPreferences prefs = getSharedPreferences(context);
        if(!prefs.contains(key)) save(context);

        // Se guarda toda la lista de favoritos como un json dentro de las
        // preferencias para obtener un acceso más eficiente.
        Gson gson = new Gson();
        String seenList = prefs.getString(key, null);
        keys = gson.fromJson(seenList, type);

    }

    /**
     * Usa la función getFavoritesAsJson y lo guarda en el archivo de
     * preferencias. Si no hay eventos guarda un null
     * @param context: Actividad desde donde se llama la aplicación
     */
    public void save(Context context) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = getSharedEditor(context);
        editor.putString(key, gson.toJson(keys));
        editor.apply();
    }

}
