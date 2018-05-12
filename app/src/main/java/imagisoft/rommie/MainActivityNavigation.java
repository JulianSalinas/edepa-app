package imagisoft.rommie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.HashMap;


public class MainActivityNavigation extends MainActivityFirebase
        implements NavigationView.OnNavigationItemSelectedListener,
        TabLayout.OnTabSelectedListener, View.OnClickListener{

    /**
     * Usadas para cambiar los fragmentos usando un hilo
     * diferente para que la animación se vea mas fluida
     */
    Handler handler;
    Runnable pendingRunnable;

    /**
     * Vistas en el encanbezado del menú principal
     */
    private TextView currentSection;
    private FloatingActionLayout favoriteButton;

    /**
     * Colección de fragmentos o secciones de la aplicación
     */
    private HashMap<Integer, Fragment> tabbedFragments;
    private HashMap<Integer, Fragment> independentFragments;

    /**
     * Hace referencia al layout que está en uso
     */
    protected int currentResource = R.id.nav_schedule;

    /**
     * Se define cúal es el layout que va a utilizar
     * dependiendo desde donde se abra la aplicación
     * @param bundle: No se utiliza
     */
    @Override
    @SuppressLint("UseSparseArrays")
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        handler = new Handler();
        tabbedFragments = new HashMap<>();
        independentFragments = new HashMap<>();

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        if(args != null)
            onCreateWithBundle(args);

        else if (bundle == null)
            onCreateWithNoArgs();

    }

    /**
     * Inicializa la aplicación utilizando parametros
     * Es usada cuando se cambia el idioma o la app se abre
     * desde una notificación
     * @param args: Argumentos dentro de Intent
     */
    protected void onCreateWithBundle(Bundle args){

        onCreateWithNoArgs();
        String key = "currentResource";
        if (args.containsKey(key)) {

            currentResource = args.getInt("currentResource");
            independentFragments.put(currentResource, createFragmentById(currentResource));
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_container, independentFragments.get(currentResource))
                    .commitAllowingStateLoss();
        }

        getIntent().removeExtra("currentResource");

    }

    /**
     * Inicializa la aplicación sin argumentos
     * Es usada cuando se abre la app desde el icono
     */
    protected void onCreateWithNoArgs(){

        currentResource = R.id.schedule_view;
        toolbar.setTitle(R.string.app_name);
        tabLayout.setVisibility(View.GONE);
        independentFragments.put(currentResource, new PagerFragmentSchedule());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, independentFragments.get(currentResource))
                .commitAllowingStateLoss();

    }

    /**
     * Después que se crea la activida y se configura, se colocan
     * los ventos del botón de favoritos y los tabs
     * @param bundle: No se utiliza
     */
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

    /**
     * Se reinicia la aplicación, conservado el estado actual
     */
    public void restartApplication(){

        Bundle args = new Bundle();
        args.putInt("currentResource", currentResource);

        Intent refresh = new Intent(this, MainActivityNavigation.class);
        refresh.putExtras(args);

        startActivity(refresh);
        finish();

    }

    /**
     * Al presionar un item del menu lateral se llama a la función para
     * colocar en la pantalla el fragment relacionado a ese item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigateById(item.getItemId());
        handlePandinRunnable();
        return true;
    }

    /**
     * Pone a correr el hilo que se había programado para cambiar
     * el fragmento actual.
     */
    public void handlePandinRunnable(){
        if (pendingRunnable != null) {
            handler.postDelayed(pendingRunnable, 300);
            pendingRunnable = null;
        }
    }

    /**
     * Ejecutada por el bóton de favoritos
     */
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

        pendingRunnable = () -> {

            if(!independentFragments.containsKey(idResource))
                independentFragments.put(idResource, createFragmentById(idResource));

            currentResource = idResource;
            switchFragment(independentFragments.get(idResource));

        };
        
        drawer.closeDrawer(GravityCompat.START);

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
