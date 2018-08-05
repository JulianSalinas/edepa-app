package imagisoft.modelview.schedule.paged;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import imagisoft.misc.DateConverter;
import imagisoft.model.Cloud;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.activity.MainFragment;
import imagisoft.modelview.interfaces.IPageListener;
import imagisoft.modelview.interfaces.IPageSubject;
import imagisoft.modelview.schedule.events.EventsAdapter;
import imagisoft.modelview.schedule.events.EventsFragment;
import imagisoft.modelview.schedule.events.EventsSchedule;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;


/**
 * NOTA: La clase #FragmentPagerAdater a diferencia de
 * FragmentStatePagerAdapter es que la primera mantiene todos los
 * fragmentos en memoria mientras que el otros solo conserva
 * la variable savedStateInstance
 */
public abstract class PaggedAdapter extends PagerFirebase implements IPageListener {

    /**
     * Para dividir los eventos por día
     * Cada fecha representa una página
     * Es importante agregarlas en orden
     */
    protected List<Long> dates;

    /**
     * Es necesario para obtener las referencias
     * utilizadas de Firebase
     */
    protected PaggedFragment fragment;

    /**
     * Constructor
     * NOTA: Cuando se usa un adaptador en un fragmento
     * se debe usar la función #getChildFragmentManager
     * @param fragment Del cual obtener el ChildFragmentManager
     */
    public PaggedAdapter(PaggedFragment fragment) {
        super(fragment.getChildFragmentManager());
        this.fragment = fragment;
        this.dates = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * Esta función solo es necesaria para saber
     * cuandos días se tienen que mostrar en el páginador
     */
    @Override
    public int getCount() {
        return dates.size();
    }

    /**
     * {@inheritDoc}
     * Obtiene un fragmento según un fecha
     * @param position Posición de la fecha
     */
    @Override
    public Fragment getItem(int position) {
        return getEventsFragment();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Bundle args = new Bundle();
        Object object = super.instantiateItem(container, position);
        EventsFragment fragment = (EventsFragment) object;
        args.putLong("date", dates.get(position));
        fragment.setArguments(args);
        fragment.setPageListener(this);
        return fragment;
    }

    /**
     * {@inheritDoc}
     * El ID que utiliza el fragmentManager para cada
     * una de los fragmentos del cronograma es la fecha
     * @param position Posición del fragmento
     */
    public long getItemId(int position) {
        return dates.get(position);
    }

    /**
     * La posición de un fragmento corresponde con
     * su ID en el arreglo de fechas {@link #dates}
     * NOTA: Esto arregla un bug donde varias páginas
     * son cargadas con el mismo fragmento
     * @param object: Objecto que se castea al fragmento
     * @return Posición del fragmento segun {@link #dates}
     */
    @Override
    public int getItemPosition(Object object) {
        IPageSubject fragment = (IPageSubject) object;
        int index = dates.indexOf(fragment.getDate());
        return index > 0 ? index : POSITION_NONE;
    }

    /**
     * {@inheritDoc}
     * El nombre de una página es la fecha con
     * formato dd/mm/yyyy
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return DateConverter.extractDate(dates.get(position));
    }

    /**
     * Remueve una página
     * Se utiliza cuando la vista de eventos se queda vacía
     * @param date Fecha de la página por remover
     * @see #addPage(long)
     */
    @Override
    public void onPageRemoved(long date){
        dates.remove(date);
        notifyDataSetChanged();
        String dateStr = DateConverter.extractDate(date);
        Log.i(toString(), "onPageRemoved(" + dateStr + ")");
    }

    @Override
    public void onPageChanged(long pageDate) {
        int index = dates.indexOf(pageDate);
        if (index != -1) fragment.setCurrentPage(index);
    }

    /**
     * Agrega una página
     * Es utilizada por {@link #addPageIfNotExists(ScheduleEvent)}
     * @param date Fecha de los eventos en la página
     * @see #onPageRemoved(long)
     */
    private void addPage(long date){
        int index = findIndexToAddPage(date);
        dates.add(index, date);
        notifyDataSetChanged();
        String dateStr = DateConverter.extractDate(date);
        Log.i(toString(), "addPage(" + dateStr + ")");
    }

    /**
     * Agrega una nueva página en caso de que no exista
     * Es utilizada cada vez que se agrega o mueve un evento
     * @param event: Donde se extrae la fecha para colocar en la página
     * @see #addPage(long)
     */
    public void addPageIfNotExists(ScheduleEvent event){
        long date = DateConverter.atStartOfDay(event.getStart());
        if (!dates.contains(date)) addPage(date);
    }

    /**
     * Encuentra en que posición debe insertarse una fecha
     * @param date Fecha de la páginaw
     * @return índice donde se debe insertar la página
     * @see #addPage(long)
     */
    private int findIndexToAddPage(long date){
        int index = 0;
        for (Long aDate : dates) {
            if (aDate < date) index += 1;
            else break;
        }
        return index;
    }

    /**
     * Función usada por {@link #getItem(int)} para obtener
     * una nueva instancia de un fragmento EventsFragment
     * Los argumentos son colocados en {@link #getItem(int)}
     * @return IEventsSubject Fragment que implemena la interfaz
     */
    protected abstract Fragment getEventsFragment();

    /**
     * Subclase de #PaggedAdapter, esta obtiene los eventos
     * por medio de Firebase y agrega las páginas necesarias
     */
    public static class Schedule extends PaggedAdapter {

        /**
         * Constructor
         * @param fragment Del cual obtener el FragmentManager
         */
        public Schedule(PaggedFragment fragment) {
            super(fragment);
            Cloud.getInstance()
                    .getReference(Cloud.SCHEDULE)
                    .orderByChild("startRunnable")
                    .addChildEventListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Fragment getEventsFragment() {
            return new EventsSchedule();
        }

        /**
         * Se extrae la fecha del evento y si no hay una pagína
         * con esa fecha entonces se crea una nueva conteniendo
         * el nuevo evento
         */
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            super.onChildAdded(dataSnapshot, s);
            ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
            if (event != null && event.getStart() != 0L) {
                event.setKey(dataSnapshot.getKey());
                addPageIfNotExists(event);
            }
        }

        /**
         * Solamente se registra que hubo un cambio en la fecha de
         * una de los eventos y que por tanto tuvo que haber una
         * operación en el páginador
         */
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            super.onChildChanged(dataSnapshot, s);
            ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
            if (event != null && event.getStart() != 0L){
                event.setKey(dataSnapshot.getKey());
                addPageIfNotExists(event);
            }
        }
    }

    /**
     * Subclase de #PaggedAdapter, esta obtiene los eventos
     * por medio de Firebase y agrega las páginas necesarias
     */
    public static class Favorites extends PaggedAdapter {

        /**
         * Constructor
         * @param fragment Del cual obtener el ChildFragmentManager
         */
        public Favorites(PaggedFragment fragment) {
            super(fragment);
            Cloud.getInstance()
                    .getReference(Cloud.SCHEDULE)
                    .orderByChild("startRunnable")
                    .addChildEventListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Fragment getEventsFragment() {
            return new EventsSchedule();
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            super.onChildAdded(dataSnapshot, s);
            ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
            if (event != null && event.getStart() != 0L) {
                event.setKey(dataSnapshot.getKey());
                addPageIfNotExists(event);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            super.onChildChanged(dataSnapshot, s);
            ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
            if (event != null && event.getStart() != 0L){
                event.setKey(dataSnapshot.getKey());
                addPageIfNotExists(event);
            }
        }

    }

}