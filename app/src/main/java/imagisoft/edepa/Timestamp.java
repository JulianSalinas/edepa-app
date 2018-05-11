package imagisoft.edepa;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Únicamente sirve para poner marcas de tiempo en distintas
 * partes de la aplicación. Clases que necesitan registrar el tiempo
 * en que fueron creadas heredan de esta .
 */
public class Timestamp implements Parcelable {

    private Long time;

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

    public static final Parcelable.Creator<Timestamp> CREATOR = new Parcelable.Creator<Timestamp>() {

        @Override
        public Timestamp createFromParcel(Parcel source) {
            return new Timestamp(source);
        }

        @Override
        public Timestamp[] newArray(int size) {
            return new Timestamp[size];
        }

    };
}
