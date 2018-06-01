package imagisoft.modelview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;

import java.util.HashMap;


public class ActivityNavigation extends ActivityCustom
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    /**
     * Usadas para cambiar los fragmentos usando un hilo
     * diferente para que la animación se vea mas fluida
     */
    private Handler handler;
    private Runnable pendingRunnable;

    /**
     * Vistas en el encanbezado del menú principal
     */
    TextView currentSection;
    private FloatingActionButton favoriteButton;

    /**
     * Colección de fragmentos o secciones de la aplicación
     */
    private HashMap<Integer, Fragment> independentFragments;

    /**
     * Hace referencia al layout que está en uso
     */
    protected int currentResource = R.id.events_view;

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
        independentFragments = new HashMap<>();

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        if(args != null)
            onCreateWithBundle(args);

        else if (bundle == null)
            onCreateWithNoArgs();

        View header = navigation.getHeaderView(0);
        favoriteButton = header.findViewById(R.id.favorite_button);
        currentSection = header.findViewById(R.id.current_section_view);

        favoriteButton.setOnClickListener(this);
        navigation.setNavigationItemSelectedListener(this);

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

            currentResource = args.getInt(key);
            independentFragments.put(currentResource, createFragmentById(currentResource));
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_container, independentFragments.get(currentResource))
                    .commitAllowingStateLoss();
        }

        getIntent().removeExtra(key);

    }

    /**
     * Inicializa la aplicación sin argumentos
     * Es usada cuando se abre la app desde el icono
     */
    protected void onCreateWithNoArgs(){

        currentResource = R.id.events_view;
        toolbar.setTitle(R.string.app_name);
        tabLayout.setVisibility(View.GONE);
        independentFragments.put(currentResource, createFragmentById(currentResource));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, independentFragments.get(currentResource))
                .commitAllowingStateLoss();

    }

    /**
     * Al tocar una notificación mientras la aplicación está abierta
     * hace que ésta funcion sea llamada para colocar la nueva pantalla
     * @param intent: Contiene que pantalla debe abrir la aplicación 
     */
    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        Bundle args = intent.getExtras();

        if(args != null){
            Intent refresh = new Intent(this, ActivityNavigation.class);
            refresh.putExtras(args);
            startActivity(refresh);
            finish();
        }
//        onCreateWithBundle(args);
    }

    /**
     * Se reinicia la aplicación, conservado el estado actual
     */
    public void restartApplication(){

        Bundle args = new Bundle();
        args.putInt("currentResource", currentResource);

        Intent refresh = new Intent(this, ActivityNavigation.class);
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
        handlePendingRunnable();
        return true;
    }

    /**
     * Pone a correr el hilo que se había programado para cambiar
     * el fragmento actual.
     */
    public void handlePendingRunnable(){
        if (pendingRunnable != null) {
            handler.post(pendingRunnable);
            pendingRunnable = null;
        }
    }

    /**
     * Ejecutada por el bóton de favoritos
     */
    @Override
    public void onClick(View v) {
        if(!independentFragments.containsKey(R.id.nav_schedule)) {
            independentFragments.put(R.id.nav_schedule, ScheduleTabs.newInstance(1));
            switchFragment(independentFragments.get(R.id.nav_schedule));
        }
        else{
            ScheduleTabs tabs = (ScheduleTabs) independentFragments.get(R.id.nav_schedule);
            tabs.setCurrentTab(1);
            switchFragment(independentFragments.get(R.id.nav_schedule));
        }
//        onBackPressed();
//        long currentTime = System.currentTimeMillis();
////        long nowPlus5Minutes = currentTime + TimeUnit.MINUTES.toMillis(5);
//        long eventStart = new Date(currentTime + 5 * 60 * 1000).getTime();
//        long diference = eventStart - currentTime;
//        startAlarmAtParticularTime(eventStart);
//        startAlarmAtParticularTime(diference);
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
        case R.id.nav_chat:
            return new ChatFragment();
        case R.id.nav_news:
            return new NewsFragment();
        case R.id.nav_about:
            return new AboutFragment();
        case R.id.nav_manage:
            return new ConfigFragment();
        case R.id.nav_pallete:
            return new ThemePicker();
        case R.id.nav_people:
            return new ExhibitorsFragment();
        case R.id.nav_infomation:
            return new InformationFragment();
        case R.id.events_view:
            return ScheduleTabs.newInstance(ScheduleTabs.SCHEDULE_TAB);
        default:
            return ScheduleTabs.newInstance(ScheduleTabs.SCHEDULE_TAB);

    }}

}
