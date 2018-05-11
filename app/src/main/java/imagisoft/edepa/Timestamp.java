package imagisoft.edepa;

import java.util.Calendar;

/**
 * Únicamente sirve para poner marcas de tiempo en distintas
 * partes de la aplicación. Clases que necesitan registrar el tiempo
 * en que fueron creadas heredan de esta .
 */
public class Timestamp {

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

}
