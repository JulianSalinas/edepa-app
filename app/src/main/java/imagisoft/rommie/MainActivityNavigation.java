package imagisoft.rommie;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.firebase.ui.auth.AuthUI;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.HashMap;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleBlock;


public class MainActivityNavigation extends MainActivityFirebase implements
        NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, View.OnClickListener{

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static final int SCHEDULE_TAB = 0;
    public static final int FAVORITES_TAB = 1;
    public static final int ONGOING_TAB = 2;

    /**
     * Variables para poder reciclar los fragmentos
     */
    private Fragment chatView;
    private Fragment newsView;
    private Fragment blankView;
    private Fragment aboutView;
    private Fragment configView;
    private Fragment themePicker;
    private Fragment scheduleView;
    private Fragment favoritesView;
    private Fragment exhibitorsView;
    private Fragment informationView;

    private HashMap<Integer, Fragment> fragments;

    private TextView currentSection;
    private FloatingActionLayout favoriteButton;

    /**
     * Atributos de control
     */
    private int currentTab;
    private boolean isScheduleView;

    @Override
    @SuppressLint("UseSparseArrays")
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        isScheduleView = true;
        toolbar.setTitle(R.string.app_name);
        tabLayout.setVisibility(View.GONE);
        currentTab = 0;

        fragments = new HashMap<>();
        fragments.put(R.id.schedule_view, new PagerFragmentSchedule());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragments.get(R.id.schedule_view));
        transaction.commitAllowingStateLoss();

    }

    @Override
    protected void onPostCreate(Bundle bundle) {

        View header = navigation.getHeaderView(0);
        favoriteButton = header.findViewById(R.id.favorite_button);
        currentSection = header.findViewById(R.id.current_section_view);

        favoriteButton.setOnClickListener(this);
        navigation.setNavigationItemSelectedListener(this);
        tabLayout.addOnTabSelectedListener(this);

        super.onPostCreate(bundle);

    }


    @Override
    public void onClick(View v) {

    }

    /**
     * Método utilizado al escoger una opción del menú de navegación
     * @param id del elemento del menú de navegación seleccionado
     */
    @Override
    public void navigateById(int id){

        if (id == R.id.nav_exit) onExit();

        else if(id == R.id.nav_exit_and_signout){
            AuthUI.getInstance().signOut(this);
            onExit();
        }

        if(!fragments.containsKey(id))
            fragments.put(id, createFragmentById(id));

        switchFragment(fragments.get(id));

    }

    public Fragment createFragmentById(int fragmentId){ switch (fragmentId){
        case R.id.nav_chat: return new ChatView();
        case R.id.nav_news: return new NewsView();
        case R.id.nav_about: return new AboutView();
        case R.id.nav_manage: return new ConfigView();
        case R.id.nav_pallete: return new ThemePicker();
        case R.id.nav_people: return new ExhibitorsView();
        case R.id.nav_infomation: return new InformationView();
        case R.id.nav_schedule: return new PagerFragmentSchedule();
        case 1: return BlankFragment.newInstance("No ongoing events");
        case 2: return new PagerFragmentFavorites();
        default: return new PagerFragmentSchedule();
    }}

    /**
     * Según el tab que escoge el usuario, se instancia la vista
     * (instanciación perezosa) y se coloca en pantalla
     * @param position: Número de tab de izq a der
     */
    public void navigateToPosition(int position) {
        currentTab = position;
        if (position == 0)
            navigateById(R.id.nav_schedule);
        else
            navigateById(position);
        paintTab(position);
    }

    /**
     * Coloca el indicador debajo del tab seleccionado
     * @param position: Número de tab de izq a der
     */
    private void paintTab(int position){
//        TabLayout.Tab tab = tabLayout.getTabAt(position);
//        assert tab != null;
//        tab.select();
    }

    /**
     * Sirve de apoyo a la función navigateToPosition para realizar
     * la instanciación
     * @param tabId: Alguno de los atributos estáticos definidos
     */
    public Fragment createTabFragment(int tabId){

//        case SCHEDULE_TAB:
//            if(tabOptions[tabId] == null)
//                return new PagerFragmentSchedule();
//            else return tabOptions[tabId];
//
//        case FAVORITES_TAB:
//            if (FavoriteList.getInstance().getSortedEvents().isEmpty()) {
//                if(tabOptions[tabId] == null || !(tabOptions[tabId] instanceof BlankFragment))
//                    return BlankFragment.newInstance(getResources()
//                            .getString(R.string.text_without_favorites));
//                else return tabOptions[tabId];
//            }
//            else return new PagerFragmentFavorites();
//
//
//        case ONGOING_TAB:
//            if(tabOptions[tabId] == null)
//                return BlankFragment.newInstance(getResources()
//                        .getString(R.string.text_without_ongoing));
//            else return tabOptions[tabId];
//
//        default:
//            return new PagerFragmentSchedule();
        return null;

    }

    /**
     * Evento que dispara la función switch framgent
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if(position != currentTab)
            navigateToPosition(position);
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
