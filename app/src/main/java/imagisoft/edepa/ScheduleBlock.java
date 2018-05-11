package imagisoft.edepa;

import imagisoft.miscellaneous.DateConverter;

/**
 * Solo proporciona las horas para un bloque, evento o noticia
 */
public class ScheduleBlock {

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

}
