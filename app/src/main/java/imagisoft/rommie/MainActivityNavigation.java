package imagisoft.rommie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.HashMap;


public class MainActivityNavigation extends MainActivityFirebase implements
        NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, View.OnClickListener{

    private TextView currentSection;
    private FloatingActionLayout favoriteButton;

    private HashMap<Integer, Fragment> tabbedFragments;
    private HashMap<Integer, Fragment> independentFragments;

    protected int currentResource = R.id.nav_schedule;

    @Override
    @SuppressLint("UseSparseArrays")
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        tabbedFragments = new HashMap<>();
        independentFragments = new HashMap<>();

        if (bundle != null){
//            currentResource = bundle.getInt(CURRENT_RESOURCE_KEY);
//            toolbar.setTitle(R.string.app_name);
//            tabLayout.setVisibility(View.GONE);
//            independentFragments.put(currentResource, createFragmentById(currentResource));
        }

        if (bundle == null) {

            currentResource = R.id.schedule_view;
            toolbar.setTitle(R.string.app_name);
            tabLayout.setVisibility(View.GONE);
            independentFragments.put(currentResource, new PagerFragmentSchedule());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, independentFragments.get(currentResource));
            transaction.commitAllowingStateLoss();

        }


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

    public void restartApplication(){
        Intent refresh = new Intent(this, MainActivityNavigation.class);
        startActivity(refresh);
        finish();
    }

    /**
     * Al presionar un item del menu lateral se llama a la función para
     * colocar en la pantalla el fragment relacionado a ese item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        navigateById(item.getItemId());
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(CURRENT_RESOURCE_KEY, currentResource);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {

        super.onRestoreInstanceState(bundle);

        if(bundle != null) {
            currentResource = bundle.getInt(CURRENT_RESOURCE_KEY);
            navigateById(currentResource);
        }

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Método utilizado al escoger una opción del menú de navegación
     * @param idResource del elemento del menú de navegación seleccionado
     */
    public void navigateById(int idResource){

        if (idResource == R.id.nav_exit) onExit();

        else if(idResource == R.id.nav_exit_and_signout){
            AuthUI.getInstance().signOut(this);
            onExit();
        }

        if(!independentFragments.containsKey(idResource))
            independentFragments.put(idResource, createFragmentById(idResource));

        currentResource = idResource;
        switchFragment(independentFragments.get(idResource), true);

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
        default: return new PagerFragmentSchedule();
    }}

    /**
     * Según el tab que escoge el usuario, se instancia la vista
     * (instanciación perezosa) y se coloca en pantalla
     * @param position: Número de tab de izq a der
     */
    public void navigateToPosition(int position) {
//        currentTab = position;
//        if (position == 0)
//            navigateById(R.id.nav_schedule);
//        else
//            navigateById(position);
//        paintTab(position);
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
//        int position = tab.getPosition();
//        if(position != currentTab)
//            navigateToPosition(position);
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
