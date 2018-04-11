package imagisoft.rommie;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.abs;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.springframework.util.LinkedMultiValueMap;


public class SchedulePager extends FragmentStatePagerAdapter {

    /**
     * Para dividir los eventos por dia en la vista
     */
    protected ArrayList<String> dates;
    protected ArrayList<ScheduleBlock> events;
    protected LinkedMultiValueMap<String, ScheduleBlock> eventsByDay;

    /**
     * Se guarda referencia a la páginas para reutilizarlas
     */
    protected Fragment [] pages;

    /**
     * Representa donde está ubicado este adaptador
     */
    protected MainViewFragment schedulePager;

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public SchedulePager(MainViewFragment schedulePager) {

        super(schedulePager.getChildFragmentManager());

        this.schedulePager = schedulePager;

        this.dates = new ArrayList<>();
        this.events = new ArrayList<>();
        this.eventsByDay = new LinkedMultiValueMap<>();

        this.pages = new Fragment[dates.size()];

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

        if(pages[position] == null)
            pages[position] = createScheduleView(position);
        return pages[position];

    }

    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    private ScheduleView createScheduleView(int position){

        List<ScheduleBlock> events = eventsByDay.get(dates.get(position));
        return ScheduleView.newInstance(events);

    }

    /**
     * Función para colocar los títulos (fechas) en la barra que contiene los días
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return dates.get(position);
    }

    /**
     * Divide los eventos por dias (formato dd/mm/yy)
     * @return HashTable (12/12/17, Evento)
     */
    public LinkedMultiValueMap<String, ScheduleBlock> getEventsByDay(){

        for(ScheduleBlock event : events)
            eventsByDay.add(UDateConverter.extractDate(event.getStart()), event);

        return eventsByDay;

    }

    /**
     * Algoritmo para ordenar y agrupar los eventos por su fechas
     * @param scheduleEvents: Eventos que se deben ordenar
     */
    public void orderEvents(ArrayList<ScheduleBlock> scheduleEvents){

        // La hora del primer evento marca la hora del primer bloque
        ScheduleEvent first = (ScheduleEvent) scheduleEvents.get(0);
        ScheduleBlock block = new ScheduleBlock(first.getStart(), first.getEnd());

        // Se añade el encabezado del bloque junto con el primer evento en la vista
        events.add(block);
        events.add(first);

        // Se agrupan los eventos si estan anidados (un evento empieza cuando otro está activo),
        // Si empienzan casi a la misma hora (10 mins = 600000 millis),
        // Si al terminar un evento solo faltan 10 mins para que inicie el siguiente
        for(int i = 1; i < scheduleEvents.size(); i++){

            long upEnd = scheduleEvents.get(i-1).getEnd();
            long upStart = scheduleEvents.get(i-1).getStart();
            long downEnd = scheduleEvents.get(i).getEnd();
            long downStart = scheduleEvents.get(i).getStart();

            boolean areNested = upStart > downStart && downStart > upEnd;

            // Agrupar en este caso es equivalente a actualizar la hora de finalización
            // del bloque. El inicio es colocado cuando dicho bloque se crea
            boolean diffCondition = abs(upEnd - downStart) <= 600000 ||
                    abs(upStart - downStart) < 600000;

            if(diffCondition || areNested) block.setEnd(downEnd);

            // Si no se cumplen las condiciones para agrupar, se inicia un nuevo bloque
            else {
                block = new ScheduleBlock(downStart, downEnd);
                events.add(block);
            }

            events.add(scheduleEvents.get(i));

        }

    }

}