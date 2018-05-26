package imagisoft.rommie;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;


public abstract class PagerAdapter extends FragmentPagerAdapter {

    /**
     * Para dividir los eventos por dia en la vista
     */
    protected List<Long> dates;

    /**
     * Diccionario donde un día indica cuantos eventos tiene
     */
    protected HashMap<Long, Integer> eventsForDay;

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
        this.eventsForDay = new HashMap<>();
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

    public long getItemId(int position) {
        return dates.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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

        int index = 0;
        for(Long aDate: dates){
            if (aDate < date) index += 1;
            else break;
        }

        if (!dates.contains(date)){
            dates.add(index, date);
            notifyDataSetChanged();
        }

        // Actualiza la cantidad de eventos en una pagina
        if(!eventsForDay.containsKey(date))
            eventsForDay.put(date, 1);
        else
            eventsForDay.put(date, eventsForDay.get(date) + 1);

    }

    /**
     * Remueve una nueva página
     * @param event: último ScheduleEvent
     */
    protected void removePageIfLast(ScheduleEvent event) {

        long date = DateConverter.atStartOfDay(event.getStart());
        if (dates.contains(date) && eventsForDay.get(date) == 1){

            int index = dates.indexOf(date);
            dates.remove(index);
            notifyDataSetChanged();
        }

        eventsForDay.put(date, eventsForDay.get(date) - 1);

    }

    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    protected abstract EventsFragment createScheduleView(long date);


}