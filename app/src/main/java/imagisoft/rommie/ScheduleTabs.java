package imagisoft.rommie;


import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import imagisoft.edepa.FavoriteList;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends MainActivityFragment implements TabLayout.OnTabSelectedListener {

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static final int SCHEDULE_TAB = 0;
    public static final int FAVORITES_TAB = 1;
    public static final int ONGOING_TAB = 2;

    MaterialSearchView searchView;

    /**
     * TabLayout con sus tres vistas principales
     */
    private int currentTab;
    private Fragment[] tabOptions;
    private List<Fragment> profilePendingList = new ArrayList<>();

    /**
     * Funciónque la actividad usa para cambiar a un tab específico
     */
    public void setCurrentTab(int currentTab){
        this.currentTab = currentTab;
    }

    /**
     * Contructor que coloca la vista en el tab deseado
     */
    public static ScheduleTabs newInstance(int currentTab) {

        Bundle args = new Bundle();
        args.putInt("currentTab", currentTab);

        ScheduleTabs fragment = new ScheduleTabs();
        fragment.setArguments(args);

        return fragment;

    }

    /**
     * Se inicializan las variables no gráficas
     */
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setHasOptionsMenu(true);

        this.tabOptions = new Fragment[3];
        this.resource = R.layout.schedule_tabs_view;

        Bundle args = getArguments();
        if(args != null)
            currentTab = args.getInt("currentTab");

    }

    /**
     * Se agrega los tabs al toolbar de la actividad y se navaga
     * hacia el última tab seleccionado
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        getTabLayout().addOnTabSelectedListener(this);

        setToolbarVisibility(View.VISIBLE);
        setTabLayoutVisibility(View.VISIBLE);

        searchView = getSearchView();
        searchView.setHint(getResources().getString(R.string.text_search));
//        searchView.setOnQueryTextListener(this);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);

        if(bundle != null)
            currentTab = bundle.getInt("currentTab");

        tabOptions[currentTab] = createTabFragment(currentTab);
        replaceFragment(tabOptions[currentTab]);
        paintTab(currentTab);

    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.closeSearch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchView.closeSearch();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        searchView.setMenuItem(searchItem);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("currentTab", currentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            currentTab = savedInstanceState.getInt("currentTab");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!profilePendingList.isEmpty())
            navigateToPosition(currentTab);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){

        Lifecycle.State state = getLifecycle().getCurrentState();

        // Si la app está en pausa no se remplaza el fragmento
        if(state.isAtLeast(Lifecycle.State.RESUMED))
            replaceFragment(fragment);

        else updateProfilePendingList(fragment);

    }

    /**
     * Usada por la función switchFragment
     * Crear y ejecuta la transacción para cambiar el fragmento
     * @param fragment: Fragmento a colocat en pantalla
     */
    private void replaceFragment(Fragment fragment){

        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.tabs_container, fragment)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .commit();

    }

    /**
     * Usada por la función switchFragment
     * Si la aplicación está pausada, todas los fragmentos que se
     * deban colorcar en pantalla hacen fila en profilePendigList
     * @param fragment: Fragmento que se iba a colocar pero la aplicación
     *                  estaba en pausa
     */
    private void updateProfilePendingList(Fragment fragment){
        profilePendingList.clear();
        profilePendingList.add(fragment);
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

    /**
     * Según el tab que escoge el usuario, se instancia la vista
     * (instanciación perezosa) y se coloca en pantalla
     * @param position: Número de tab de izq a der
     */
    public void navigateToPosition(int position) {
        currentTab = position;
        tabOptions[position] = createTabFragment(position);
        switchFragment(tabOptions[position]);
        paintTab(position);
    }

    /**
     * Coloca el indicador debajo del tab seleccionado
     * @param position: Número de tab de izq a der
     */
    private void paintTab(int position){
        TabLayout.Tab tab = getTabLayout().getTabAt(position);
        assert tab != null;
        tab.select();
    }

    /**
     * Sirve de apoyo a la función navigateToPosition para realizar
     * la instanciación
     * @param tabId: Alguno de los atributos estáticos definidos
     */
    public Fragment createTabFragment(int tabId){ switch (tabId){

        case SCHEDULE_TAB:
            if(tabOptions[tabId] == null)
                return new PagerFragmentSchedule();
            else return tabOptions[tabId];

        case FAVORITES_TAB:
            if (FavoriteList.getInstance().getSortedEvents().isEmpty()) {
                if(tabOptions[tabId] == null || !(tabOptions[tabId] instanceof BlankFragment))
                    return BlankFragmentTabbed.newInstance(getResources()
                            .getString(R.string.text_without_favorites));
                else return tabOptions[tabId];
            }
            else return new PagerFragmentFavorites();


        case ONGOING_TAB:
            if(tabOptions[tabId] == null)
                return BlankFragmentTabbed.newInstance(getResources()
                        .getString(R.string.text_without_ongoing));
            else return tabOptions[tabId];

        default:
            return new PagerFragmentSchedule();

    }}

}