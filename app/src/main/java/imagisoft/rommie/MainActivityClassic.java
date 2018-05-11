package imagisoft.rommie;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class MainActivityClassic extends AppCompatActivity {

    protected static String TAG = "MainActivity";

    /**
     * Atributos en común para todas las aplicaciones.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.navigation)
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

        Log.i(TAG, "OnCreate()");
        super.onCreate(bundle);

        setContentView(R.layout.main_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        toggle.syncState();

    }

    @Override
    protected void onStart() {
        Log.i(TAG, "OnStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {

        Log.i(TAG, "OnResume()");
        super.onResume();

        if(!profilePendingList.isEmpty())
            switchFragment(profilePendingList.pop(), false);

    }

    @Override
    protected void onPause() {
        Log.i(TAG, "OnPause()");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        Log.i(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(bundle);
    }

    /**
     * Al cerrar la aplicación
     */
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
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

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);

        else if (fm.getBackStackEntryCount() > 0)
            super.onBackPressed();

        else onExit();

    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment, boolean addToBackStack){

        Lifecycle.State state = getLifecycle().getCurrentState();

        if(state.isAtLeast(Lifecycle.State.RESUMED))
            replaceFragment(fragment, addToBackStack);

        else {
            profilePendingList.clear();
            profilePendingList.add(fragment);
        }

    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack){

        FragmentTransaction ft =
                 getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out);

        if (addToBackStack)
            ft.addToBackStack(null).commit();

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
