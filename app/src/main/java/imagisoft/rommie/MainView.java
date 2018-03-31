package imagisoft.rommie;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.Toast;
import android.view.MenuItem;
import android.widget.TextView;
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

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class MainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public int FADE_ANIMATION = 0;
    public int SLIDE_ANIMATION = 1;

    /**
     * Atributos en común para todas las aplicaciones. Barra de herramientas, menu lateral, etc.
     */
    protected Toolbar toolbar;
    protected CollapsingToolbarLayout toolbarLayout;

    private DrawerLayout drawer;
    private NavigationView navigation;
    private ActionBarDrawerToggle toggle;
    private FloatingActionLayout favoriteButton;

    /**
     * Para colocar el nombre de la sección actual en el menú lateral
     */
    protected TextView currentSection;

    /**
     * Se inician todos los componentes principales de la aplicación
     */
    @Override
    protected void onCreate (Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        bindViews();
        setupToolbar();
        setupToggle();
        navigateById(R.id.nav_schedule);
    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     */
    private void bindViews(){

        toolbar = findViewById(R.id.toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);

        drawer = findViewById(R.id.main_drawer);

        navigation = findViewById(R.id.main_nav);
        navigation.setNavigationItemSelectedListener(this);

        View header = navigation.getHeaderView(0);
        favoriteButton = header.findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(this);

        currentSection = header.findViewById(R.id.current_section_view);

    }

    /**
     * Onclick para el favoriteButton
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Coloca la barra de herramientas
     */
    private void setupToolbar(){
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(null);
    }

    /**
     * Coloca el botón de menú lateral
     */
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

    /**
     * Ayuda a la función navigateToItem. La clase MainViewNavigation
     * es la que implementa esta función
     * @param id: Identificación de botón presionado
     */
    protected abstract void navigateById(int id);

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){
        switchFragment(fragment, FADE_ANIMATION);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado usando un animación
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment, int animation){
        FragmentTransaction transaction = createTransactionWithCustomAnimation(animation);
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Coloca una animación personalizada al cambiar de fragmento
     */
    public FragmentTransaction createTransactionWithCustomAnimation(int animation){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int in = animation == FADE_ANIMATION ? R.animator.fade_in : R.animator.slide_in_left;
        int out = animation == FADE_ANIMATION ? R.animator.fade_out : R.animator.slide_out_right;
        transaction.setCustomAnimations(in, out);
        return transaction;
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
