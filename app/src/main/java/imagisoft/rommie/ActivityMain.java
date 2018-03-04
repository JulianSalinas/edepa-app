package imagisoft.rommie;

import android.os.Bundle;
import android.content.Context;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public int FADE_ANIMATION = 0;
    public int SLIDE_ANIMATION = 1;

    /**
     * Atributos en común para todas las aplicaciones. Barra de herramientas, menu lateral, etc.
     */
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigation;

    /**
     * Se inician todos los componenetes principales de la aplicacion
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarConfiguration();
        setDrawerConfiguration();
        setToggleConfiguration();
        setNavigationViewConfiguration();
        switchFragment(new ScheduleTabs());
    }

    /**
     * Coloca la barra de herramientas
     */
    private void setToolbarConfiguration(){
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.nav_schedule));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getResources().getString(R.string.app_name));
    }

    /**
     * Coloca el menu lateral de la aplicación
     */
    private void setDrawerConfiguration(){
        drawer = findViewById(R.id.main_drawer);
        // drawer.animate();
    }

    private void setToggleConfiguration(){
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        toggle.syncState();
    }

    /**
     * Coloca todos los items contenidos en "nav_menu" en el menu lateral
     */
    private void setNavigationViewConfiguration(){
        navigation = findViewById(R.id.main_nav);
        navigation.setNavigationItemSelectedListener(this);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        navigateToItem(item);
        return true;
    }

    /**
     * TODO: Se deben agregar los demás fragmentos nuevos aquí
     * Crea un fragmento nuevo y lo coloca en la pantalla
     * @param item Opción elegida por el usuario
     */
    private void navigateToItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_exit:
                finishAffinity(); break;
            case R.id.nav_infomation:
                switchFragment(new FragmentInfo()); break;
            case R.id.nav_schedule:
                switchFragment(new ScheduleTabs()); break;
            case R.id.nav_agenda:
                switchFragment(new ScheduleTabs()); break;
            default:
                switchFragment(new ScheduleTabs()); break;
        }
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    void switchFragment(Fragment fragment){
        switchFragment(fragment, FADE_ANIMATION);
    }

    /**
     * TODO: Se pueden agregar animaciones a futuro
     * Coloca en la pantalla un fragmento previamente creado usando un animación
     * para hacer la transición de un fragmento a otro.
     * @param fragment Asociado a la opción elegida por el usuario
     */
    void switchFragment(Fragment fragment, int animation){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animation == FADE_ANIMATION)
            transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        else transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

}
