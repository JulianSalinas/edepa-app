package imagisoft.rommie;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends MainViewFragment implements TabLayout.OnTabSelectedListener {

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static int SCHEDULE_TAB = 0;
    public static int DIARY_TAB = 1;
    public static int ONGOING_TAB = 2;

    /**
     * TabLayout con sus tres vistas principales
     */
    private TabLayout tabLayout;
    private ArrayList<Fragment> tabs;


    /**
     * Se crea la vista que contiene el tabLayout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_tabs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        assert getView() != null;
        tabLayout = getView().findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);

        tabs = new ArrayList<>();
        tabs.add(new SchedulePager());
        tabs.add(new SchedulePager());
        tabs.add(new ScheduleView());
        switchFragment(tabs.get(SCHEDULE_TAB));
    }

    /**
     * Cambia el contenido según el tab que se presione
     * @param fragment que contiene el nuevo contenido (actividades o eventos )
     */
    public void switchFragment(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
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
