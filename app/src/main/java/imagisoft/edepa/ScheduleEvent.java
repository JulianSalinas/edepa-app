package imagisoft.edepa;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import android.content.Context;

/**
 * Contiene toda la información de un evento en particular
 * Es necesario colocar los resumenes después de haber creado el evento
 * De igual forma es necesario agregar los expositores después de crearlo
 */
public class ScheduleEvent extends ScheduleBlock {

    /**
     * Atributos fundamentales
     */
    private String id;
    private String title;
    private String location;
    private ScheduleEventType eventype;

    /**
     * Atributos requeridos pero excluyentes, es decir,
     * si se usa uno no se debe usar el otro
     */
    private String briefEnglish;
    private String briefSpanish;

    /**
     * Atributo opcional, no importa que este vacio
     */
    private List<Exhibitor> exhibitors;

    /**
     * Getters de los atributos fundamentales
     */
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public ScheduleEventType getEventype() {
        return eventype;
    }

    /**
     * Solo se retorna uno de los resumenes
     * Esto va a depender del idioma que tenga el usuario en la
     * configuración y de los idiomas presentes .
     * @param context: Actividad donde se ejecuta
     */
    public String getBrief(Context context) {

        String brief =
                briefEnglish != null && briefSpanish == null ? briefEnglish:
                briefEnglish == null && briefSpanish != null ? briefSpanish: null;

        if(brief != null) return brief;

        else if (briefEnglish != null & briefSpanish != null){

            String lang = Preferences.getInstance()
                    .getStringPreference(context, Preferences.LANG_KEY_VALUE);

            return !lang.equals("en") ? briefSpanish : briefEnglish;
        }

        else return null;

    }

    public void setBriefEnglish(String briefEnglish) {
        this.briefEnglish = briefEnglish;
    }

    public void setBriefSpanish(String briefSpanish) {
        this.briefSpanish = briefSpanish;
    }

    /**
     * Obtiene la lista de expositores asistentes
     */
    public List<Exhibitor> getExhibitors() {
        return exhibitors;
    }

    /**
     * Agrega un nuevo expositro al evento.
     * Esta función solo es usada para generar pruebas
     */
    public ScheduleEvent addExhibitor(Exhibitor exhibitor){
        exhibitors.add(exhibitor);
        return this;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public ScheduleEvent(){
        briefSpanish = "Sin resumen";
    }

    public ScheduleEvent(String id, String start, String end,
                         String location, String title, ScheduleEventType eventype){

        super(start, end);
        this.id = id;
        this.title = title;
        this.location = location;
        this.eventype = eventype;
        exhibitors = new ArrayList<>();

    }

    /**
     * Un evento es el mismo si tiene el mismo id y el mismo título
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleEvent)) return false;
        ScheduleEvent event = (ScheduleEvent) o;
        return id.equals(event.id) && title.equals(event.title);
    }

    /**
     * Función para realizar búsquedas indexadas
     */
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    /**
     * Permite pasar como parámetro al servicio de alarma
     */
    public static String toJson(ScheduleEvent event){

        Gson g = new Gson();
        return g.toJson(event);

    }

    /**
     * Proceso inversa de la función toJSon
     */
    public static ScheduleEvent fromJson(String event){

        Gson g = new Gson();
        return g.fromJson(event, ScheduleEvent.class);

    }

}
