package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.abs;

import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;


public abstract class PagerAdapter extends FragmentPagerAdapter {

    /**
     * Para dividir los eventos por dia en la vista
     */
    protected List<Long> dates;

    /**
     * Diccionario donde un clave contiene multiples bloques que
     * pertenecen al mismo día
     */
    protected LinkedMultiValueMap<Long, ScheduleEvent> eventsForDay;

    /**
     * Representa donde está ubicado este adaptador
     */
    protected PagerFragment fragment;

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public PagerAdapter(PagerFragment fragment) {
        super(fragment.getChildFragmentManager());
        this.fragment = fragment;
        this.dates = new ArrayList<>();
        this.eventsForDay = new LinkedMultiValueMap<>();
    }

    /*
     * Esta función solo es necesaria para saber
     * cuandos dias se tienen que mostrar
     */
    @Override
    public int getCount() {
        return dates.size();
    }

    /*
     * Se colocan todos los eventos que ocurren en un día específico
     */
    @Override
    public Fragment getItem(int position) {
        return createScheduleView(dates.get(position));
    }

    /**
     * Función para colocar los títulos (fechas) en
     * la barra que contiene los días
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return DateConverter.extractDate(dates.get(position));
    }

    /**
     * Agrega una nueva página
     * @param event: ScheduleEvent
     */
    public void addPageIfNotExists(ScheduleEvent event){
        long date = DateConverter.atStartOfDay(event.getStart());
        if (!dates.contains(date)){
            dates.add(date);
            eventsForDay.add(date, event);
            notifyDataSetChanged();
        }
    }

    protected void removePageIfLast(ScheduleEvent event) {
        long date = DateConverter.atStartOfDay(event.getStart());
        if (dates.contains(date) && dates.size() == 1){
            dates.remove(date);
            eventsForDay.remove(date);
            notifyDataSetChanged();
        }
    }

    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    protected abstract EventsFragment createScheduleView(long date);


}