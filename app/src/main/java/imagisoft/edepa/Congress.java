package imagisoft.edepa;

/**
 * Se hereda de ScheduleBlock solo para reutilizar los atributos
 * Start y End
 */
public class Congress extends ScheduleBlock{

    /**
     * Atributos considerados como fundamentales
     */
    private String name;
    private String location;
    private String description;

    /**
     * Atributos marcados como opcionales
     * Se utilizan para poder obtener los datos que se pueden\
     * colocar en Google Maps
     */
    private float xCoord;
    private float yCoord;
    private String locationTag;

    /**
     * Getters y Setters de los atributos fundamentales
     */
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Getters y Setters de los atributos opcionales
     */
    public float getxCoord() {
        return xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public String getLocationTag() {
        return locationTag;
    }

}
