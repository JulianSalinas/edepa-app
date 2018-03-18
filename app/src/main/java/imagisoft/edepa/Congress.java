package imagisoft.edepa;

import java.util.ArrayList;

/**
 * Herede de ScheduleBlock solo para reutilizar los atributos
 * Start y End
 */
public class Congress extends ScheduleBlock{

    private String name;

    public String getName() {
        return name;
    }

    /**
     * Las fechas se colocan con el formato dd/mm/yyyy hh:mm <am|pm>
     * pero se guardan como long para hacerlo más fácil entre plataformas
     */
    public Congress(String name, String start, String end) {
        super(start, end);
        this.name = name;
    }

}
