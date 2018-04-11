package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.view.ViewPager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentStatePagerAdapter;


public class SchedulePagerFragment extends MainViewFragment {

    /**
     * Barra debajo de los tabs para colocar los días
     */
    private ViewPager pager;

    /**
     * El adaptador contiene los fragmentos del páginador
     */
    private FragmentStatePagerAdapter adapter;

    /**
     * Crea la vista del paginador, es decir, donde se colocan los días
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view =  inflater.inflate(R.layout.schedule_pager, container, false);
        pager = view.findViewById(R.id.view_pager);
        return view;

    }

    /**
     * Al crearse el fragmento se prepara el paginador para mostrar los días
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        this.setupViewPager();

    }

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    private void setupViewPager() {

        // Se revisa porque al entrar por seguna vez, no es necesario colocar el adaptador
        // de lo contrario se reinicia a la posición inicial (visualmente)
        if(adapter == null)
            adapter = new SchedulePagerAdapter(this);

        pager.setAdapter(adapter);

    }

}
