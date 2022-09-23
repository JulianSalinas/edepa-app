package edepa.pagers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import edepa.minilibs.TimeConverter;
import edepa.schedule.ScheduleFragment;


/**
 * NOTA: La clase #FragmentPagerAdater a diferencia de
 * FragmentStatePagerAdapter es que la primera mantiene todos los
 * fragmentos en memoria mientras que el otros solo conserva
 * la variable savedStateInstance
 */
public abstract class PagerAdapter extends FragmentPagerAdapter {

    /**
     * Para dividir los eventos por día
     * Cada fecha representa una página
     * Es importante agregarlas en orden
     */
    protected List<Long> dates;

    /**
     * Constructor de {@link PagerAdapter}
     * NOTA: Cuando se usa un adaptador en un fragmento
     * se debe usar la función #getChildFragmentManager
     * @param fragment Del cual obtener el ChildFragmentManager
     */
    public PagerAdapter(Fragment fragment, List<Long> dates) {
        super(fragment.getChildFragmentManager());
        this.dates = dates;
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
     * El ID que utiliza el fragmentManager para cada
     * una de los fragmentos del cronograma es la fecha
     * @param position Posición del fragmento
     */
    public long getItemId(int position) {
        return dates.get(position);
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
        args.putLong("date", dates.get(position));
        fragment.setArguments(args);
        return fragment;
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
        ScheduleFragment fragment = (ScheduleFragment) object;
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
        return TimeConverter.extractDateUTC(dates.get(position));
    }

    /**
     * Función usada por {@link #getItem(int)} para obtener
     * una nueva instancia de un fragmento ScheduleFragment
     * Los argumentos son colocados en {@link #getItem(int)}
     * @return Callbacks Fragment que implemena la interfaz
     */
    protected abstract Fragment instantiateEventsFragment();

}