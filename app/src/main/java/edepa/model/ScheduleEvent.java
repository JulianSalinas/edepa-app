package edepa.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import edepa.misc.DateConverter;


/**
 * Contiene toda la información de un evento en particular
 * Es necesario colocar los resumenes después de haber creado el evento
 * De igual forma es necesario agregar los expositores después de crearlo
 */
public class ScheduleEvent implements Comparable<ScheduleEvent>, Parcelable {

    /**
     * Atributos fundamentales
     */
    private String key;
    private String title;
    private String location;
    private ScheduleEventType eventype;

    /**
     * Las fechas se colocan con el formato dd/mm/yyyy hh:mm <am|pm>
     * pero se guardan como long para hacerlo más fácil entre plataformas
     */
    protected Long end;
    protected Long start;

    protected Long date;

    protected int favoritesAmount;

    protected boolean isFavorite;

    /**
     * Getters y Setters de las fechas
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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

    public List<String> getPeople() {
        return new ArrayList<>(people.keySet());
    }

    public void setPeople(HashMap<String, Boolean> people) {
        this.people = people;
    }

    /**
     * Atributos requeridos pero excluyentes, es decir,
     * si se usa uno no se debe usar el otro
     */
    private String briefEnglish;
    private String briefSpanish;

    private HashMap<String, Boolean> people;

    /**
     * Contructor vacío requerido por firebase
     */
    public ScheduleEvent(){
        this.people = new HashMap<>();
    }

    public ScheduleEvent(String key, String title, String location,
                         ScheduleEventType eventype, Long end, Long start, Long date,
                         String briefEnglish, String briefSpanish, int favoritesAmount) {
        this.key = key;
        this.title = title;
        this.location = location;
        this.eventype = eventype;
        this.end = end;
        this.start = start;
        this.date = date;
        this.briefEnglish = briefEnglish;
        this.briefSpanish = briefSpanish;
        this.favoritesAmount = favoritesAmount;
        this.people = new HashMap<>();
    }

    public boolean hasSameDate(ScheduleEvent event){
        return this.getDate().equals(event.getDate());
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
    public ScheduleEvent addPerson(Person person){
        people.put(person.getPersonKey(), true);
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
     * Un evento es el mismo si tiene el mismo key y el mismo título
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEvent that = (ScheduleEvent) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public int compareTo(ScheduleEvent event) {
        return event.getStart().compareTo(getStart());
    }

    @Override
    public String toString() {
        return getTitle();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.title);
        dest.writeString(this.location);
        dest.writeInt(this.eventype == null ? -1 : this.eventype.ordinal());
        dest.writeValue(this.end);
        dest.writeValue(this.start);
        dest.writeValue(this.date);
        dest.writeInt(this.favoritesAmount);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.briefEnglish);
        dest.writeString(this.briefSpanish);
        dest.writeSerializable(this.people);
    }

    protected ScheduleEvent(Parcel in) {
        this.key = in.readString();
        this.title = in.readString();
        this.location = in.readString();
        int tmpEventype = in.readInt();
        this.eventype = tmpEventype == -1 ? null : ScheduleEventType.values()[tmpEventype];
        this.end = (Long) in.readValue(Long.class.getClassLoader());
        this.start = (Long) in.readValue(Long.class.getClassLoader());
        this.date = (Long) in.readValue(Long.class.getClassLoader());
        this.favoritesAmount = in.readInt();
        this.isFavorite = in.readByte() != 0;
        this.briefEnglish = in.readString();
        this.briefSpanish = in.readString();
        this.people = (HashMap<String, Boolean>) in.readSerializable();
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
