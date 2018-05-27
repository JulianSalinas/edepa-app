package imagisoft.rommie;


import android.arch.lifecycle.Lifecycle;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import imagisoft.edepa.FavoriteList;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends ActivityFragment implements TabLayout.OnTabSelectedListener{

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static final int SCHEDULE_TAB = 0;
    public static final int FAVORITES_TAB = 1;
    public static final int ONGOING_TAB = 2;

    /**
     * Cambia de fragmento hacia uno donde se pueden realizar
     * las búsquedas
     */
    private MaterialSearchView searchView;

    /**
     * TabLayout con sus tres vistas principales
     */
    private int currentTab;
    private EventsFragment ongoingFragment;
    private PagerFragmentSchedule scheduleFragment;
    private PagerFragmentFavorites favoritesFragment;

    private FavoriteList favoriteList;

    /**
     * Usadas para cambiar los fragmentos usando un hilo
     * diferente para que la animación se vea mas fluida
     */
    private Handler handler;
    private Runnable pendingRunnable;

    private List<Fragment> profilePendingList = new ArrayList<>();

    /**
     * Función que la actividad usa para cambiar a un tab específico
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
        handler = new Handler();

        Bundle args = getArguments();
        if(args != null)
            currentTab = args.getInt("currentTab");

    }

    @Override
    public void setupResource() {
        this.resource = R.layout.schedule_tabs_view;
    }

    /**
     * Se agrega los tabs al toolbar de la actividad y se navaga
     * hacia el última tab seleccionado
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        favoriteList = FavoriteList.getInstance();
        getTabLayout().addOnTabSelectedListener(this);

//        tabLayout = getTabLayout();
        searchView = getSearchView();
        searchView.setHint(getResources().getString(R.string.text_search));
//        searchView.setOnQueryTextListener(this);
        searchView.setVoiceSearch(true);
        searchView.setVoiceIcon(getResources().getDrawable(R.drawable.ic_voice));
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);

        if(bundle != null)
            currentTab = bundle.getInt("currentTab");

        navigateToPosition(currentTab);
        handlePendingRunnable();

    }

    @Override
    public void setupActivityView() {
        setToolbarVisibility(View.VISIBLE);
        setTabLayoutVisibility(View.VISIBLE);
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
        if(savedInstanceState != null) {
            currentTab = savedInstanceState.getInt("currentTab");
            paintTab(currentTab);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!profilePendingList.isEmpty())
            navigateToPosition(currentTab);
    }

    /**
     * Pone a correr el hilo que se había programado para cambiar
     * el fragmento actual.
     */
    public void handlePendingRunnable(){
        if (pendingRunnable != null) {
            handler.postDelayed(pendingRunnable, 200);
            pendingRunnable = null;
        }
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
        if(position != currentTab) {
            navigateToPosition(position);
            handlePendingRunnable();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Get this tab fragment
        View v = tab.getCustomView();

        if (v != null) {
            setAlpha(v, false);

            // Stop the animation if the tab was animating.
            // This makes the transition smooth
            Animation viewAnimation = v.getAnimation();

            if (viewAnimation != null)
                viewAnimation.cancel();
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Se requiere sobrescribir
    }

    /**
     * Coloca el indicador debajo del tab seleccionado
     * @param position: Número de tab de izq a der
     */
    public void paintTab(int position){
        TabLayout.Tab tab = getTabLayout().getTabAt(position);
        assert tab != null;
        tab.select();
    }

    /**
     * Según el tab que escoge el usuario, se instancia la vista
     * (instanciación perezosa) y se coloca en pantalla
     * @param position: Número de tab de izq a der
     */
    public void navigateToPosition(int position) {

        currentTab = position;
        paintTab(position);

        pendingRunnable = () -> {

            if(position == SCHEDULE_TAB){
                if(scheduleFragment == null)
                    scheduleFragment = new PagerFragmentSchedule();
                switchFragment(scheduleFragment);
            }

            else if (position == FAVORITES_TAB){

                if(favoriteList.isEmpty()) {
                    String description = activity.getString(R.string.text_without_favorites);
                    switchFragment(BlankFragment.newInstance(description));
                }

                else {
                    if(favoritesFragment == null)
                        favoritesFragment = new PagerFragmentFavorites();
                    switchFragment(favoritesFragment);
                }

            }

            else {

                if (ongoingFragment == null)
                    ongoingFragment = EventsFragmentOngoing.newInstance();
                switchFragment(ongoingFragment);

            }

        };

    }

    private void setAlpha(View view, boolean selected) {
        if (selected) {
            if (Build.VERSION.SDK_INT < 11) {
                AlphaAnimation alpha = new AlphaAnimation(1F, 1F);
                alpha.setDuration(0);
                alpha.setFillAfter(true);
                view.startAnimation(alpha);
            } else {
                view.setAlpha(1);
            }
        } else {
            if (Build.VERSION.SDK_INT < 11) {
                AlphaAnimation alpha = new AlphaAnimation(0.7F, 0.7F);
                alpha.setDuration(0);
                alpha.setFillAfter(true);
                view.startAnimation(alpha);
            } else {
                view.setAlpha((float) 0.7);
            }
        }
    }


}