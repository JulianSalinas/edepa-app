package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.view.MenuItem;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.Observer;
import imagisoft.edepa.UTestController;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class ActivityMain
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Observer {

    /**
     * Conexión con el controlador
     */
    UTestController controller = UTestController.getInstance();

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public int FADE_ANIMATION = 0;
    public int SLIDE_ANIMATION = 1;

    /**
     * Atributos en común para todas las aplicaciones. Barra de herramientas, menu lateral, etc.
     */
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigation;
    private ActionBarDrawerToggle toggle;
    private FloatingActionLayout favoriteButton;

    /**
     * Se inician todos los componenetes principales de la aplicacion
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setupToolbar();
        setupToggle();
        navigateById(R.id.nav_schedule);

        // Se subscribe al controlador
        controller.addObserver(this);

    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     */
    private void bindViews(){

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.main_drawer);

        navigation = findViewById(R.id.main_nav);
        navigation.setNavigationItemSelectedListener(this);

        favoriteButton = navigation.getHeaderView(0).findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(this);

    }

    /**
     * Onclick para el favoriteButton
     */
    @Override
    public void onClick(View v) {
        controller.testObserverPattern();
    }

    /**
     * Coloca la barra de herramientas
     */
    private void setupToolbar(){
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(null);
    }

    private void setupToggle(){
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        toggle.syncState();
    }

    /**
     * Función para reconocer si el menu lateral está o no abierto
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    /**
     * Al presionar un item del menu lateral se llama a la función para
     * colocar en la pantalla el fragment relacionado a ese item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        navigateToItem(item);
        return true;
    }

    /**
     * Crea un fragmento nuevo y lo coloca en la pantalla
     * @param item Opción elegida por el usuario
     */
    private void navigateToItem(MenuItem item){
        navigateById(item.getItemId());
    }

    public abstract void navigateById(int id);

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    void switchFragment(Fragment fragment){
        switchFragment(fragment, FADE_ANIMATION);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado usando un animación
     * @param fragment Asociado a la opción elegida por el usuario
     */
    void switchFragment(Fragment fragment, int animation){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animation == FADE_ANIMATION)
            transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        else transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}
