package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.database.ServerValue;

import imagisoft.edepa.BlankFragment;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends MainViewFragment implements TabLayout.OnTabSelectedListener {

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static final int SCHEDULE_TAB = 0;
    public static final int DIARY_TAB = 1;
    public static final int ONGOING_TAB = 2;

    /**
     * TabLayout con sus tres vistas principales
     */
    private TabLayout tabLayout;
    private SchedulePager schedule;
    private SchedulePager diary;
    private BlankFragment ongoing;

    private int currentTab;

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

        currentTab = 0;
        schedule = new SchedulePager();
        switchFragment(schedule);

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
        int pos = tab.getPosition();
        if(pos != currentTab){

            currentTab = pos;
            switch (pos){

                case SCHEDULE_TAB:
//                    if(schedule == null)
                        schedule = new SchedulePager();
                    switchFragment(schedule);
                    break;

                case DIARY_TAB:
//                    if(diary == null)
                        diary = new SchedulePager();
                    switchFragment(diary);
                    break;

                case ONGOING_TAB:
//                    if(ongoing == null)
                        ongoing = new BlankFragment();
                    switchFragment(ongoing);
                    break;

            }

        }

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
