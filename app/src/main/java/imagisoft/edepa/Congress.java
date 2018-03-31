package imagisoft.edepa;

import java.util.ArrayList;

/**
 * Herede de ScheduleBlock solo para reutilizar los atributos
 * Start y End
 */
public class Congress extends ScheduleBlock{

    private String name;
    private String description;
    private String writtenLocation;
    private float xCoord;
    private float yCoord;
    private String locationTag;


    public String getName() {
        if(name == null)
            return "No disponible";
        return name;
    }

    public String getDescription() {
        if(description == null)
            return "No disponible";
        return description;
    }

    public String getWrittenLocation() {
        if(writtenLocation == null)
            return "No disponible";
        return writtenLocation;
    }

    public float getxCoord() { return xCoord; }

    public float getyCoord() { return yCoord; }

    public String getLocationTag() { return locationTag; }

    public Congress(){ }

    /**
     * Las fechas se colocan con el formato dd/mm/yyyy hh:mm <am|pm>
     * pero se guardan como long para hacerlo más fácil entre plataformas
     */

}
