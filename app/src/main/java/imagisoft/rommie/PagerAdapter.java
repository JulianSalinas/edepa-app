package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.abs;

import imagisoft.edepa.ScheduleBlock;
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
    protected Fragment [] pages;

    /**
     * Lista de eventos, sin contar los bloques que solo muestran
     * una hora determinada
     */
    protected List<ScheduleEvent> events;

    /**
     * Combinación de eventos con bloques que solo muestran la hora
     */
    protected List<ScheduleBlock> blocks;

    /**
     * Diccionario donde un clave contiene multiples bloques que
     * pertenecen al mismo día
     */
    protected LinkedMultiValueMap<String, ScheduleBlock> blocksByDay;

    /**
     * Representa donde está ubicado este adaptador
     */
    protected  MainActivityFragment fragment;

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public PagerAdapter( MainActivityFragment fragment) {

        super(fragment.getChildFragmentManager());
        this.fragment = fragment;

        this.dates = new ArrayList<>();
        this.events = new ArrayList<>();
        this.blocks = new ArrayList<>();
        this.blocksByDay = new LinkedMultiValueMap<>();
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
//        if(pages[position] == null)
//            pages[position] = createScheduleView(position);
        return createScheduleView(position);
    }

    /**
     * Se usa para que la vista se puede actualizar
     * Usa más recursos pero con la cantidad de datos no se nota
     */
//    public int getItemPosition(Object object){
//        return POSITION_NONE;
//    }

    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    protected ScheduleView createScheduleView(int position){
        List<ScheduleBlock> blocks = blocksByDay.get(dates.get(position));
        return ScheduleView.newInstance(blocks);
    }

    /**
     * Función para colocar los títulos (fechas) en la barra que contiene los días
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return dates.get(position);
    }

    /**
     * Agrega un nuevo evento
     * @param event: ScheduleEvent
     */
    public void addEvent(ScheduleEvent event){
        events.add(event);
    }

    /**
     * Agrega un nuevo conjunto de eventos
     * @param events: ArrayList<ScheduleEvent>
     */
    public void addEvents(List<ScheduleEvent> events){
        this.events.addAll(events);
    }


    /**
     * Cuando algún evento se agrega es necesario actualizar
     */
    @Override
    public void notifyDataSetChanged(){

        // Llena la variable blocks
        createBlocks();

        // Se reinicia para no agregar repetidos
        dates = new ArrayList<>();
        blocksByDay = new LinkedMultiValueMap<>();

        // Llena la variable blocksByDate
        groupBlocksByDay();
        dates.addAll(blocksByDay.keySet());

        pages = new Fragment[dates.size()];
        super.notifyDataSetChanged();

    }

    /**
     * Divide los eventos por dias (formato dd/mm/yy)
     */
    public void groupBlocksByDay(){
        for(ScheduleBlock event : blocks)
            blocksByDay.add(DateConverter.extractDate(event.getStart()), event);
    }

    /**
     * Algoritmo para ordenar y agrupar los eventos por su fechas
     */
    public void createBlocks() {

        if(events.size() > 0) {
            blocks = new ArrayList<>();

            // La hora del primer evento marca la hora del primer bloque
            ScheduleEvent first = events.get(0);
            ScheduleBlock block = new ScheduleBlock(first.getStart(), first.getEnd());

            // Se añade el encabezado del bloque junto con el primer evento en la vista
            blocks.add(block);
            blocks.add(first);

            // Se agrupan los eventos si estan anidados (un evento empieza cuando otro está activo),
            // Si empienzan casi a la misma hora (10 mins = 600000 millis),
            // Si al terminar un evento solo faltan 10 mins para que inicie el siguiente
            for (int i = 1; i < events.size(); i++) {

                long upEnd = events.get(i - 1).getEnd();
                long upStart = events.get(i - 1).getStart();
                long downEnd = events.get(i).getEnd();
                long downStart = events.get(i).getStart();

                boolean areNested = upStart > downStart && downStart > upEnd;

                // Agrupar en este caso es equivalente a actualizar la hora de finalización
                // del bloque. El inicio es colocado cuando dicho bloque se crea
                boolean diffCondition = abs(upEnd - downStart) <= 600000 ||
                        abs(upStart - downStart) < 600000;

                if (diffCondition || areNested) block.setEnd(downEnd);

                // Si no se cumplen las condiciones para agrupar, se inicia un nuevo bloque
                else {
                    block = new ScheduleBlock(downStart, downEnd);
                    blocks.add(block);
                }

                blocks.add(events.get(i));

            }
        }
    }

}