package imagisoft.rommie;

import java.util.Set;
import java.util.ArrayList;

import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.springframework.util.LinkedMultiValueMap;

/**
 * Es el adaptador de la barra que contiene los días del cronograma
 * TODO: Estos días es necesario estraerlos con la fecha de inicio y la fecha fin del congreso
 */
public class SchedulePagerAdapter extends FragmentPagerAdapter {

    /**
     * Para dividir los eventos por dia en la vista
     */
    private ArrayList<String> dates;
    private ArrayList<ScheduleEvent> events;
    private LinkedMultiValueMap<String, ScheduleBlock> eventsByDay;

    /**
     * Paginador y arreglo de vistas que contendrá el paginador
     */
    private SchedulePager schedulePager;
    private ArrayList<ScheduleView> scheduleViews;

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public SchedulePagerAdapter(SchedulePager schedulePager) {

        super(schedulePager.getFragmentManager());

        this.dates = new ArrayList<>();
        this.events = new ArrayList<>();
        this.eventsByDay = new LinkedMultiValueMap<>();

        this.schedulePager = schedulePager;
        this.scheduleViews = schedulePager.getScheduleViews();

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
        return scheduleViews.get(position);
    }

    /**
     * Función para colocar los títulos (fechas) en la barra que contiene los días
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return dates.get(position);
    }

    private void createScheduleViews(){
        Set<String> keys = eventsByDay.keySet();
        for(String key : keys)
            scheduleViews.add(ScheduleView.newInstance(eventsByDay.get(key)));
    }

    /**
     * Divide los eventos por dias (formato dd/mm/yy)
     * @return HashTable (12/12/17, Evento)
     */
    public LinkedMultiValueMap<String, ScheduleBlock> getEventsByDay(){

        for(ScheduleEvent event : events)
            eventsByDay.add(UDateConverter.extractDate(event.getStart()), event);

        return eventsByDay;

    }

    /**
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class SchedulePagerAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                events.add(postSnapshot.getValue(ScheduleEvent.class));
            }

            eventsByDay = getEventsByDay();
            dates.addAll(eventsByDay.keySet());

            createScheduleViews();
            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Context context = schedulePager.getContext();
            dates.add(context.getResources().getString(R.string.text_no_connection));
            notifyDataSetChanged();
        }

    }

}