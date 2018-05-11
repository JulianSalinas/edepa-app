package imagisoft.rommie;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Locale;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.Preferences;

import static imagisoft.rommie.CustomColor.APP_ACCENT_DARK;
import static imagisoft.rommie.CustomColor.APP_PRIMARY_DARK;

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

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.navigation)
    NavigationView navigation;

    protected ActionBarDrawerToggle toggle;
    protected Stack<Fragment> profilePendingList = new Stack<>();

    /**
     * Únicos Getters y Setters necesarios
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    /**
     * Se inician todos los componentes principales de la aplicación
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

    /**
     * Cuando la actividad se vuelve visible al usuario
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "OnStart()");
        super.onStart();
    }

    /**
     * Después de volver desde otra app
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "OnResume()");
        super.onResume();
        if(!profilePendingList.isEmpty())
            switchFragment(profilePendingList.pop());
    }

    /**
     * Al salirse de la app sin cerrarla
     */
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
     * Función para reconocer si el menu lateral está o no abierto
     */
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);

        else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                super.onBackPressed();

        else onExit();

    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){

        if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED))
            replaceFragment(fragment);

        else addToProfilePendingList(fragment);

    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void addToProfilePendingList(Fragment fragment){
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


}
