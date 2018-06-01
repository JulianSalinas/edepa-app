package imagisoft.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import imagisoft.misc.DateConverter;


/**
 * Contiene toda la información de un evento en particular
 * Es necesario colocar los resumenes después de haber creado el evento
 * De igual forma es necesario agregar los expositores después de crearlo
 */
public class ScheduleEvent implements Comparable<ScheduleEvent>,Parcelable {

    /**
     * Atributos fundamentales
     */
    private String id;
    private String title;
    private String location;
    private ScheduleEventType eventype;

    /**
     * Las fechas se colocan con el formato dd/mm/yyyy hh:mm <am|pm>
     * pero se guardan como long para hacerlo más fácil entre plataformas
     */
    protected Long end;
    protected Long start;

    protected int favoritesAmount;

    /**
     * Getters y Setters de las fechas
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ScheduleEventType getEventype() {
        return eventype;
    }

    public void setEventype(ScheduleEventType eventype) {
        this.eventype = eventype;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public int getFavoritesAmount() {
        return favoritesAmount;
    }

    public void setFavoritesAmount(int favoritesAmount) {
        this.favoritesAmount = favoritesAmount;
    }

    public String getBriefEnglish() {
        return briefEnglish;
    }

    public void setBriefEnglish(String briefEnglish) {
        this.briefEnglish = briefEnglish;
    }

    public String getBriefSpanish() {
        return briefSpanish;
    }

    public void setBriefSpanish(String briefSpanish) {
        this.briefSpanish = briefSpanish;
    }

    public List<Exhibitor> getExhibitors() {
        return exhibitors;
    }

    public void setExhibitors(List<Exhibitor> exhibitors) {
        this.exhibitors = exhibitors;
    }

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
     * Contructor vacío requerido por firebase
     */
    public ScheduleEvent(){

    }

    public ScheduleEvent(String id, String title, String location,
                         ScheduleEventType eventype, Long end, Long start,
                         String briefEnglish, String briefSpanish, int favoritesAmount) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.eventype = eventype;
        this.end = end;
        this.start = start;
        this.briefEnglish = briefEnglish;
        this.briefSpanish = briefSpanish;
        this.favoritesAmount = favoritesAmount;
    }

    /**
     * Solo se retorna uno de los resumenes
     * Esto va a depender del idioma que tenga el usuario en la configuración
     * Si el resumen solo está disponible en un idioma entonces se usa ese
     * @param lang: "es" o "en"
     */
    public String getBrief(String lang) {

        String brief =
                briefEnglish != null && briefSpanish == null ? briefEnglish:
                briefEnglish == null && briefSpanish != null ? briefSpanish: null;

        if(brief != null) return brief;

        else if (briefEnglish != null & briefSpanish != null){
            return lang.equals("es") ? briefSpanish : briefEnglish;
        }

        else return createDefaultBrief();

    }

    /**
     * Se usa en caso que el administrador no haya régistrado ningún resumen
     * para el evento.
     * TODO: Mover esto de lugar
     */
    public String createDefaultBrief(){

        return "La actividad está programada en el horario de " +
                DateConverter.extractTime(getStart()) + " a " +
                DateConverter.extractTime(getEnd()) + " en el " + location +
                ". Puede utilizar el chat para realizar cualquier consulta.";

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
     * Permite pasar como parámetro al servicio de alarma
     */
    public static String toJson(ScheduleEvent event){
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    /**
     * Proceso inversa de la función toJSon
     */
    public static ScheduleEvent fromJson(String event){
        Gson gson = new Gson();
        return gson.fromJson(event, ScheduleEvent.class);
    }

    /**
     * Un evento es el mismo si tiene el mismo id y el mismo título
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEvent that = (ScheduleEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(ScheduleEvent event) {
        return getStart().compareTo(event.getStart());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.location);
        dest.writeInt(this.eventype == null ? -1 : this.eventype.ordinal());
        dest.writeValue(this.end);
        dest.writeValue(this.start);
        dest.writeInt(this.favoritesAmount);
        dest.writeString(this.briefEnglish);
        dest.writeString(this.briefSpanish);
        dest.writeTypedList(this.exhibitors);
    }

    protected ScheduleEvent(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.location = in.readString();
        int tmpEventype = in.readInt();
        this.eventype = tmpEventype == -1 ? null : ScheduleEventType.values()[tmpEventype];
        this.end = (Long) in.readValue(Long.class.getClassLoader());
        this.start = (Long) in.readValue(Long.class.getClassLoader());
        this.favoritesAmount = in.readInt();
        this.briefEnglish = in.readString();
        this.briefSpanish = in.readString();
        this.exhibitors = in.createTypedArrayList(Exhibitor.CREATOR);
    }

    public static final Creator<ScheduleEvent> CREATOR = new Creator<ScheduleEvent>() {
        @Override
        public ScheduleEvent createFromParcel(Parcel source) {
            return new ScheduleEvent(source);
        }

        @Override
        public ScheduleEvent[] newArray(int size) {
            return new ScheduleEvent[size];
        }
    };
}
