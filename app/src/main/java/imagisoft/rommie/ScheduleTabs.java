package imagisoft.rommie;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.ArrayList;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends Fragment implements TabLayout.OnTabSelectedListener {

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static int SCHEDULE_TAB = 0;
    public static int DIARY_TAB = 1;
    public static int ONGOING_TAB = 2;

    private int activeTab = 0;
    private TabLayout tabLayout;
    private ArrayList<Fragment> tabs;

    public ScheduleTabs() {
        // Required empty public constructor
    }

    public static ScheduleTabs newInstance(int tab) {
        ScheduleTabs fragment = new ScheduleTabs();
        Bundle args = new Bundle();
        args.putInt("tab", tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            activeTab = getArguments().getInt("tab");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_tabs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        assert v != null;
        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);

        setupInitialConfiguration();
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
        switchFragment(tabs.get(activeTab));
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
     * Para que la activity pueda colocar uno de los tabs
     */
    public void switchToSchedule(){
        activeTab = SCHEDULE_TAB;
        selectTab();
    }

    public void switchToDiary(){
        activeTab = DIARY_TAB;
        selectTab();
    }

    public void switchToOngoing(){
        activeTab = ONGOING_TAB;
        selectTab();
    }

    public void selectTab(){
        TabLayout.Tab tab = tabLayout.getTabAt(activeTab);
        assert tab != null; tab.select();
        onTabSelected(tabLayout.getTabAt(activeTab));
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
