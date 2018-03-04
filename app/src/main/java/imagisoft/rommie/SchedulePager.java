package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Contiene los fragmentos donde se muestras las actividades del congreso
 */
public class SchedulePager extends Fragment {

    public SchedulePager() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_pager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: Información de prueba, solamente para mostrar como se ve
        ArrayList<String> dates = new ArrayList<>();
        for(int i = 12; i<=18; i++)
            dates.add(String.valueOf(i) + "/08/2018");

        SchedulePagerAdapter adapter = new SchedulePagerAdapter(dates);
        ViewPager viewPager = getView().findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
    }

    /**
     * Es el adaptador de la barra que contiene los días del cronograma
     * TODO: Estos días es necesario estraerlos con la fecha de inicio y la fecha fin del congreso
     */
    public class SchedulePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> dates;

        private SchedulePagerAdapter(ArrayList<String> dates) {
            super(getChildFragmentManager());
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

}
