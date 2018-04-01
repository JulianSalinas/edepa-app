package imagisoft.edepa;

/**
 * Solo proporciona las horas para un bloque o una activdad
 * Tiene como hijo la clase ScheduleEvent
 */
public class ScheduleBlock {

    /**
     * Las fechas se colocan con el formato dd/mm/yyyy hh:mm <am|pm>
     * pero se guardan como long para hacerlo más fácil entre plataformas
     */
    private Long end;
    private Long start;

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

    public ScheduleBlock(){
        // Requerido por firebase
    }

    public ScheduleBlock(Long start, Long end) {
        this.end = end;
        this.start = start;
    }

    public ScheduleBlock(String start, String end) {
        this.end = UDateConverter.stringToLong(end);
        this.start = UDateConverter.stringToLong(start);
    }

}
