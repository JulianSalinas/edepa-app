package imagisoft.modelview;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;

import java.util.Stack;
import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class ActivityClassic extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigation;


    protected ActionBarDrawerToggle toggle;
    protected Stack<Fragment> profilePendingList = new Stack<>();

    /**
     * Necesario para que los fragmentos la puedan editar
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Se inician todos los componentes principales de la aplicación.
     * Al rotar la pantalla se reinicia la actividad y se llama otra vez.
     */
    @Override
    protected void onCreate (Bundle bundle) {

        super.onCreate(bundle);

        setContentView(R.layout.main_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        toggle.syncState();

    }

    /**
     * Cuando la aplicación regresa de la pausa, coloca el
     * último fragmento que quedo pendiente
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(!profilePendingList.isEmpty())
            switchFragment(profilePendingList.pop());
    }

    /**
     * Cierra absolutamente la aplicación
     */
    public void onExit(){
        finishAndRemoveTask();
        System.exit(0);
    }

    /**
     * Función usada al presionar el botón hacia atrás
     */
    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);

        if (fm.getBackStackEntryCount() > 0)
            super.onBackPressed();

        else onExit();

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
    public void replaceFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_content, fragment)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .commit();

    }

    public void overlapFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.main_content, fragment)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .commit();

    }

    public void removeFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
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
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Utiliza en vez de un string un recurso
     * @param resource: R.string.<resource>
     */
    public void showStatusMessage(int resource){
        String msg = getResources().getString(resource);
        showStatusMessage(msg);
    }

}
