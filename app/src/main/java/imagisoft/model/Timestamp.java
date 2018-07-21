package imagisoft.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

import imagisoft.modelview.chat.IChatItem;

/**
 * Únicamente sirve para poner marcas de tiempo en distintas
 * partes de la aplicación. Clases que necesitan registrar el tiempo
 * en que fueron creadas heredan de esta .
 */
public class Timestamp implements Parcelable, IChatItem {

    protected Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * Si no se especifica el momento de la creación, se
     * asume que es el actual
     */
    public Timestamp() {
        this.time = Calendar.getInstance().getTimeInMillis();
    }

    public Timestamp(Long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timestamp)) return false;
        Timestamp timestamp = (Timestamp) o;
        return Objects.equals(time, timestamp.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.time);
    }

    protected Timestamp(Parcel in) {
        this.time = (Long) in.readValue(Long.class.getClassLoader());
    }

}
