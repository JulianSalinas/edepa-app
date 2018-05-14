package imagisoft.rommie;


public class ScheduleViewOngoing extends ScheduleView {

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleViewOngoing newInstance() {
        return new ScheduleViewOngoing();
    }

    protected void setupAdapter(){

        // Se revisa porque al entrar por seguna vez, no es necesario colocar el adaptador
        if(adapter == null)
            adapter = new ScheduleViewAdapterOngoing(this);

    }

}
