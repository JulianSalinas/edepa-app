package imagisoft.modelview.schedule;

import java.util.List;
import java.util.ArrayList;

import imagisoft.misc.DateConverter;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.activity.ActivityFragment;
import imagisoft.modelview.views.DateFragment;

import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;


/**
 * NOTA: La clase #FragmentPagerAdater a diferencia de
 * FragmentStatePagerAdapter es que la primera mantiene todos los
 * fragmentos en memoria mientras que el otros solo conserva
 * la variable savedStateInstance
 */
public abstract class PagerAdapter extends PagerFirebase implements IEventsListener {

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
    protected ActivityFragment fragment;

    /**
     * Constructor
     * NOTA: Cuando se usa un adaptador en un fragmento
     * se debe usar la función #getChildFragmentManager
     * @param fragment Del cual obtener el ChildFragmentManager
     */
    public PagerAdapter(ActivityFragment fragment) {
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
        long date = dates.get(position);
        String tag = fragment.getTag();
        Bundle args = new Bundle();
        args.putLong("date", date);
        args.putString("tag", tag);
        Fragment frag = getEventsFragment();
        frag.setArguments(args);
        return frag;
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
        IEventsSubject fragment = (IEventsSubject) object;
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
    public void removeDate(long date){
        dates.remove(date);
        notifyDataSetChanged();
        String dateStr = DateConverter.extractDate(date);
        Log.i(toString(), "removeDate(" + dateStr + ")");
    }

    /**
     * Agrega una página
     * Es utilizada por {@link #addPageIfNotExists(ScheduleEvent)}
     * @param date Fecha de los eventos en la página
     * @see #removeDate(long)
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
     * @param date Fecha de la página
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
     * Subclase de #PagerAdapter, esta obtiene los eventos
     * por medio de Firebase y agrega las páginas necesarias
     */
    public static class Schedule extends PagerAdapter{

        /**
         * Constructor
         * @param fragment Del cual obtener el FragmentManager
         */
        public Schedule(ActivityFragment fragment) {
            super(fragment);
            fragment.getActivityCustom()
                    .getScheduleReference()
                    .orderByChild("start")
                    .addChildEventListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Fragment getEventsFragment() {
            return new EventsFragment.Schedule();
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
                event.setId(dataSnapshot.getKey());
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
                Log.i(toString(), "onChildChanged(DataSnapshot, String)");
            }
        }

    }

    /**
     * Subclase de #PagerAdapter, esta obtiene los eventos
     * por medio de Firebase y agrega las páginas necesarias
     */
    public static class Favorites extends PagerAdapter {

        /**
         * Para realizar la consulta de los favortos
         */
        protected FirebaseAuth auth = FirebaseAuth.getInstance();
        protected FirebaseUser user = auth.getCurrentUser();

        /**
         * Constructor
         * @param fragment Del cual obtener el ChildFragmentManager
         */
        public Favorites(ActivityFragment fragment) {
            super(fragment);
            fragment.getActivityCustom()
                    .getScheduleReference()
                    .orderByChild("start")
                    .addChildEventListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Fragment getEventsFragment() {
            return new EventsFragment.Favorites();
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            super.onChildAdded(dataSnapshot, s);
            ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
            if (event != null && event.getStart() != 0L) {
                event.setId(dataSnapshot.getKey());
                addPageIfNotExists(event);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            super.onChildChanged(dataSnapshot, s);
        }

    }

}