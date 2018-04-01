package imagisoft.rommie;

import java.util.ArrayList;
import static java.lang.Math.abs;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.springframework.util.LinkedMultiValueMap;

/**
 * Es el adaptador de la barra que contiene los días del cronograma
 * TODO: Estos días es necesario estraerlos con la fecha de inicio y la fecha fin del congreso
 */
public class SchedulePagerAdapter extends FragmentStatePagerAdapter {

    /**
     * Para dividir los eventos por dia en la vista
     */
    private ArrayList<String> dates;
    private ArrayList<ScheduleBlock> events;
    private LinkedMultiValueMap<String, ScheduleBlock> eventsByDay;

    private SchedulePager schedulePager;

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public SchedulePagerAdapter(SchedulePager schedulePager) {

        super(schedulePager.getChildFragmentManager());

        this.dates = new ArrayList<>();
        this.events = new ArrayList<>();
        this.eventsByDay = new LinkedMultiValueMap<>();
        this.schedulePager = schedulePager;

        Query query = this.schedulePager
                .getFirebase()
                .getScheduleReference()
                .orderByChild("start");

        query.addValueEventListener(new SchedulePagerAdapterValueEventListener());
    }

    /*
     * Esta función solo es necesaria para saber cuandos dias se tiene que mostrar
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
        return ScheduleView.newInstance(eventsByDay.get(dates.get(position)));
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

    /**
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class SchedulePagerAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            // Se extraen todos los eventos de firebase
            ArrayList<ScheduleBlock> scheduleEvents = new ArrayList<>();
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                scheduleEvents.add(postSnapshot.getValue(ScheduleEvent.class));

            orderEvents(scheduleEvents);

            eventsByDay = getEventsByDay();
            dates.addAll(eventsByDay.keySet());
            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // TODO: Manejar el error
        }

    }

}