package imagisoft.edepa;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


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

    public void setBriefEnglish(String briefEnglish) {
        this.briefEnglish = briefEnglish;
    }

    public void setBriefSpanish(String briefSpanish) {
        this.briefSpanish = briefSpanish;
    }

    /**
     * Solo se retorna uno de los resumenes
     * Esto va a depender del idioma que tenga el usuario en la
     * configuración y de los idiomas presentes .
     * @param lang: "es" o "en
     */
    public String getBrief(String lang) {

        String brief =
                briefEnglish != null && briefSpanish == null ? briefEnglish:
                briefEnglish == null && briefSpanish != null ? briefSpanish: null;

        if(brief != null) return brief;

        else if (briefEnglish != null & briefSpanish != null){
            return !lang.equals("en") ? briefSpanish : briefEnglish;
        }

        else return createDefaultBrief();

    }

    public String createDefaultBrief(){

        return "La actividad está programada en el horario de " +
                UDateConverter.extractTime(getStart()) + " a " +
                UDateConverter.extractTime(getEnd()) + " en el " + location +
                ". Puede utilizar el chat para realizar cualquier consulta.";

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

    }

    /**
     * Constructor usado solo para tests, no utiliza los resumenes
     * por no se tiene información para eso.
     */
    public ScheduleEvent(String id, String start, String end,
                         String location, String title, ScheduleEventType eventype){

        super(start, end);
        this.id = id;
        this.title = title;
        this.location = location;
        this.eventype = eventype;
        this.exhibitors = new ArrayList<>();

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

}
