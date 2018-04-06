package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.view.ViewPager;
import android.support.annotation.NonNull;

/**
 * Contiene los fragmentos donde se muestras las actividades del congreso
 */
public class SchedulePager extends MainViewFragment {

    /**
     * Barra debajo de los tabs para colocar los días
     */
    private ViewPager viewPager;

    /**
     * Crea la vista del paginador, es decir, donde se colocan los días
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_pager, container, false);
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
     * y coloca la interfaz para solo mostrar dichos dias
     */
    private void setupViewPager() {

        assert getView() != null;
        viewPager = getView().findViewById(R.id.view_pager);
        viewPager.setAdapter(new SchedulePagerAdapter(this));

    }

}
