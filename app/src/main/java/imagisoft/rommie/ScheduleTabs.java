package imagisoft.rommie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends Fragment implements TabLayout.OnTabSelectedListener {

    private ArrayList<Fragment> tabs;

    public ScheduleTabs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_tabs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        setupInitialConfiguration();

        assert v != null;
        TabLayout tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);
    }

    /**
     * Se coloca la barra de los días en la vista de cada tab
     * TODO: Se debe hacer una versión especial para el tab en curso, ya que solo se muestra el día actual
     */
    private void setupInitialConfiguration() {
        tabs = new ArrayList<>();
        tabs.add(new SchedulePager());
        tabs.add(new SchedulePager());
        tabs.add(new SchedulePager());
        switchFragment(tabs.get(0));
    }

    /**
     * Cambia el contenido según el tab que se presione
     * @param fragment que contiene el nuevo contenido (actividades o eventos )
     */
    void switchFragment(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_out_right, R.animator.slide_in_left);
        transaction.replace(R.id.tabs_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Evento que dispara la función switch framgent
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switchFragment(tabs.get(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Se requiere sobrescribir
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Se requiere sobrescribir
    }

}
