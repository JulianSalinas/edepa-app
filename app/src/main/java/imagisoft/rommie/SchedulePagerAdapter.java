package imagisoft.rommie;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Es el adaptador de la barra que contiene los días del cronograma
 * TODO: Estos días es necesario estraerlos con la fecha de inicio y la fecha fin del congreso
 */
public class SchedulePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> dates;

    public SchedulePagerAdapter(ArrayList<String> dates, FragmentManager manager) {
        super(manager);
        this.dates = dates;
    }

    /*
     * Esta función solo es necesario para saber cuandos días se tiene que mostrar
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

        // TODO: Agregar párametro al schedule view para saber que actividades se deben mostrar
        return new ScheduleView();
    }

    /**
     * Función para colocar los títulos (fechas) en la barra que contiene los días
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return dates.get(position);
    }

}