package imagisoft.edepa;

import android.os.Parcel;
import android.os.Parcelable;

import imagisoft.miscellaneous.DateConverter;

/**
 * Solo proporciona las horas para un bloque, evento o noticia
 */
public class ScheduleBlock implements Parcelable {

    /**
     * Las fechas se colocan con el formato dd/mm/yyyy hh:mm <am|pm>
     * pero se guardan como long para hacerlo más fácil entre plataformas
     */
    private Long end;
    private Long start;

    /**
     * Getters y Setters de las fechas
     */
    public Long getEnd() {
        return end;
    }

    public Long getStart() {
        return start;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public ScheduleBlock(){

    }

    /**
     * Contructor a partir de timestamps
     */
    public ScheduleBlock(Long start, Long end) {
        this.end = end;
        this.start = start;
    }

    /**
     * Constructor donde las fechas se ingresan con
     * el formato dd/mm/yyyy hh:mm <am|pm>
     */
    public ScheduleBlock(String start, String end) {
        this.end = DateConverter.stringToLong(end);
        this.start = DateConverter.stringToLong(start);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.end);
        dest.writeValue(this.start);
    }

    protected ScheduleBlock(Parcel in) {
        this.end = (Long) in.readValue(Long.class.getClassLoader());
        this.start = (Long) in.readValue(Long.class.getClassLoader());
    }

}
