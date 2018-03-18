package imagisoft.edepa;

import java.util.ArrayList;

/**
 * Contiene toda la información de un evento en particular
 * Es necesario colocar los resumenes despues de haber creado el evento
 * De igual forma es necesario agregar los expositores después de crearlo
 */
public class ScheduleEvent extends ScheduleBlock {

    private String id;
    private Long end;
    private Long start;
    private String title;
    private String briefEnglish;
    private String briefSpanish;
    private ScheduleEventType eventype;
    private ArrayList<Exhibitor> exhibitors;

    public String getId() {
        return id;
    }

    @Override
    public Long getEnd() {
        return end;
    }

    @Override
    public Long getStart() {
        return start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefEnglish() {
        return briefEnglish;
    }

    public void setBriefEnglish(String briefEnglish) {
        this.briefEnglish = briefEnglish;
    }

    public String getBriefSpanish() {
        return briefSpanish;
    }

    public void setBriefSpanish(String briefSpanish) {
        this.briefSpanish = briefSpanish;
    }

    public ScheduleEventType getEventype() {
        return eventype;
    }

    public ArrayList<Exhibitor> getExhibitors() {
        return exhibitors;
    }

    public void addExhibitor(Exhibitor exhibitor){
        exhibitors.add(exhibitor);
    }

    ScheduleEvent(String id, String start, String end, String title, ScheduleEventType eventype){
        super(start, end);
        this.id = id;
        this.title = title;
        this.eventype = eventype;
        this.briefSpanish = "No hay resumen";
        this.briefEnglish = "There is not a brief";
        exhibitors = new ArrayList<>();
    }

}
