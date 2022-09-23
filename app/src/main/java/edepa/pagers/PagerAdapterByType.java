package edepa.pagers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import edepa.model.EventType;
import edepa.schedule.IEventsByType;
import edepa.schedule.ScheduleByType;


/**
 * NOTA: La clase #FragmentPagerAdater a diferencia de
 * FragmentStatePagerAdapter es que la primera mantiene todos los
 * fragmentos en memoria mientras que el otros solo conserva
 * la variable savedStateInstance
 */
public abstract class PagerAdapterByType extends FragmentPagerAdapter {

    /**
     * Para dividir los eventos por día
     * Cada fecha representa una página
     * Es importante agregarlas en orden
     */
    protected List<EventType> types;

    /**
     * Constructor de {@link PagerAdapterByType}
     * NOTA: Cuando se usa un adaptador en un fragmento
     * se debe usar la función #getChildFragmentManager
     * @param fragment Del cual obtener el ChildFragmentManager
     */
    public PagerAdapterByType(Fragment fragment, List<EventType> types) {
        super(fragment.getChildFragmentManager());
        this.types = types;
    }

    /**
     * {@inheritDoc}
     * Esta función solo es necesaria para saber
     * cuandos días se tienen que mostrar en el páginador
     */
    @Override
    public int getCount() {
        return types.size();
    }

    /**
     * {@inheritDoc}
     * El ID que utiliza el fragmentManager para cada
     * una de los fragmentos del cronograma es la fecha
     * @param position Posición del fragmento
     */
    public long getItemId(int position) {
        return types.get(position).hashCode();
    }

    /**
     * {@inheritDoc}
     * Obtiene un fragmento según un fecha
     * @param position Posición de la fecha
     */
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = instantiateEventsFragment();
        args.putString(ScheduleByType.TYPE_KEY, types.get(position).toString());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * La posición de un fragmento corresponde con
     * su ID en el arreglo de fechas {@link #types}
     * NOTA: Esto arregla un bug donde varias páginas
     * son cargadas con el mismo fragmento
     * @param object: Objecto que se castea al fragmento
     * @return Posición del fragmento segun {@link #types}
     */
    @Override
    public int getItemPosition(Object object) {
        IEventsByType fragment = (IEventsByType) object;
        int index = types.indexOf(fragment.getEventype());
        return index > 0 ? index : POSITION_NONE;
    }

    /**
     * {@inheritDoc}
     * El nombre de una página es la fecha con
     * formato dd/mm/yyyy
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return types.get(position).toString();
    }

    /**
     * Función usada por {@link #getItem(int)} para obtener
     * una nueva instancia de un fragmento ScheduleFragment
     * Los argumentos son colocados en {@link #getItem(int)}
     * @return Callbacks Fragment que implemena la interfaz
     */
    protected abstract Fragment instantiateEventsFragment();

}