package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.abs;

import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;

/**
 * Los eventos debe ser suminitrados por la clase implementante
 * Cuando se agreguen todos los eventos se debe usar la función notifyDataSetChanged()
 */
public abstract class PagerAdapter extends FragmentPagerAdapter {

    /**
     * Para dividir los eventos por dia en la vista
     */
    protected List<String> dates;

    /**
     * Se guarda referencia a la páginas para reutilizarlas
     */
    protected HashMap<String, EventsView> pages;

    /**
     * Diccionario donde un clave contiene multiples bloques que
     * pertenecen al mismo día
     */
    protected LinkedMultiValueMap<String, ScheduleEvent> eventsForDay;

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
        this.pages = new HashMap<>();
        this.dates = new ArrayList<>();
        this.eventsForDay = new LinkedMultiValueMap<>();

    }

    /*
     * Esta función solo es necesaria para saber cuandos dias se tienen que mostrar
     */
    @Override
    public int getCount() {
        return dates.size();
    }

    /*
     * Se colocan todos los eventos que ocurran en un día específico
     */
    @Override
    public Fragment getItem(int position) {
        String date = dates.get(position);
        return createScheduleView(date);
    }

    /**
     * Función para colocar los títulos (fechas) en la barra que contiene los días
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return dates.get(position);
    }

    /**
     * Agrega una nueva página
     * @param event: ScheduleEvent
     */
    public void addPageIfNotExists(ScheduleEvent event){

        String date = DateConverter.extractDate(event.getStart());

        if (!dates.contains(date)){

            dates.add(date);

            Collections.sort(dates, (before, after) -> {
                long beforeHour = DateConverter.stringToLong(before + " 12:00 pm");
                long afterHour =  DateConverter.stringToLong(after + " 12:00 pm");
                return beforeHour == afterHour ? 0: beforeHour >= afterHour ? 1: -1;
            });

            eventsForDay.add(date, event);
            notifyDataSetChanged();

        }

    }

    protected void removePageIfLast(ScheduleEvent event) {

    }

    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    protected abstract EventsView createScheduleView(String date);


}