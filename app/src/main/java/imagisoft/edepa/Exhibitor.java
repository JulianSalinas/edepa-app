package imagisoft.edepa;

import android.os.Parcel;
import android.os.Parcelable;

public class Exhibitor implements Parcelable {

    /**
     * Nombre donde se incluyen también apellidos de la persona
     */
    private String completeName;

    /**
     * El título podría ser solo el país y tal vez la institución
     * Ej: Julian Salinas
     *     Instituto Tecnológico de Costa Rica
     */
    private String personalTitle;

    /**
     * Getters y Setters de los atributos del expositor
     */
    public String getCompleteName() {
        return completeName;
    }

    public String getPersonalTitle() {
        return personalTitle;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public Exhibitor() {

    }

    /**
     * Constructor usado únicamente para generar pruebas
     */
    public Exhibitor(String completeName, String personalTitle) {

        this.completeName = completeName;
        this.personalTitle = personalTitle;

    }

    /*
    * Es la misma persona con solo poseer el mismo nombre
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exhibitor)) return false;
        Exhibitor exhibitor = (Exhibitor) o;
        return completeName.equals(exhibitor.completeName);
    }

    /**
     * Función para realizar búsquedas indexadas
     */
    @Override
    public int hashCode() {
        return completeName.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.completeName);
        dest.writeString(this.personalTitle);
    }

    protected Exhibitor(Parcel in) {
        this.completeName = in.readString();
        this.personalTitle = in.readString();
    }

    public static final Parcelable.Creator<Exhibitor> CREATOR = new Parcelable.Creator<Exhibitor>() {

        @Override
        public Exhibitor createFromParcel(Parcel source) {
            return new Exhibitor(source);
        }

        @Override
        public Exhibitor[] newArray(int size) {
            return new Exhibitor[size];
        }

    };

}
