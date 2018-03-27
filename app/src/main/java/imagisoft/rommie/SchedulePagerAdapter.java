package imagisoft.rommie;

import java.util.Set;
import java.util.ArrayList;
import imagisoft.edepa.Schedule;
import imagisoft.edepa.ScheduleBlock;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.springframework.util.LinkedMultiValueMap;

/**
 * Es el adaptador de la barra que contiene los días del cronograma
 * TODO: Estos días es necesario estraerlos con la fecha de inicio y la fecha fin del congreso
 */
public class SchedulePagerAdapter extends FragmentPagerAdapter {

    /**
     * Fechas que se mostrarán en el viewPager
     */
    private ArrayList<String> dates;

    /**
     * Para dividir los eventos por dia en la vista
     */
    private LinkedMultiValueMap<String, ScheduleBlock> eventsByDay;

    /**
     * SchedulePager al que se debe colocar este adaptador
     */
    private SchedulePager schedulePager;

    /**
     * Arreglo de vistas que contendrá el paginador
     */
    private ArrayList<ScheduleView> scheduleViews;

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public SchedulePagerAdapter(SchedulePager schedulePager) {

        super(schedulePager.getFragmentManager());

        this.dates = new ArrayList<>();
        this.schedulePager = schedulePager;

        scheduleViews = new ArrayList<>();
        scheduleViews.addAll(schedulePager.getScheduleViews());

        this.schedulePager
                .getFirebase()
                .getScheduleReference()
                .addValueEventListener(new SchedulePagerAdapterValueEventListener());

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
     * TODO: Se debe crear el filtro que divida las actividades en días
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
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class SchedulePagerAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Schedule schedule = schedulePager.getFirebase().getSchedule();
            eventsByDay = schedule.getEventsByDay();
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