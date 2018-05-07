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
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Locale;
import java.util.Stack;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.Preferences;

import static imagisoft.rommie.CustomColor.APP_ACCENT;
import static imagisoft.rommie.CustomColor.APP_ACCENT_DARK;
import static imagisoft.rommie.CustomColor.APP_PRIMARY;
import static imagisoft.rommie.CustomColor.APP_PRIMARY_DARK;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Variables usadas para correr el servicio de notificaciones
     */
    private FirebaseJobDispatcher dispatcher;
    private final String CURRENT_RESOURCE_KEY = "CURRENT_RESOURCE";
    private final String NOTIFICATION_ID = "Recordatorios";
    private final String CHANNEL = "Servicio de notificaciones";

    /**
     * Atributos en común para todas las aplicaciones.
     */
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView navigation;
    protected ActionBarDrawerToggle toggle;

    private Stack<Fragment> profilePendingList = new Stack<>();

    public Toolbar getToolbar() {
        return toolbar;
    }

    int currentResource = R.id.nav_schedule;

    /**
     * Se inician todos los componentes principales de la aplicación
     */
    @Override
    protected void onCreate (Bundle bundle) {

        Aesthetic.attach(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().setStatusBarColor(prefs.getInt(APP_PRIMARY_DARK.toString(), APP_PRIMARY_DARK.getColor()));
        getWindow().setNavigationBarColor(prefs.getInt(APP_ACCENT_DARK.toString(), APP_ACCENT_DARK.getColor()));

        String lang = prefs.getString("LANG_KEY_VALUE", null);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            Preferences.getInstance()
                    .setPreference(this, Preferences.LANG_KEY_VALUE, lang);
        }

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);

        super.onCreate(bundle);
        if (Aesthetic.isFirstTime()) setTheme();

        FavoriteList.getInstance().loadFavorites(this);
        setContentView(R.layout.main_drawer);

        bindMainViews();
        setupToolbar();
        setupToggle();
        bindNavigationViews();

        setupDispatcher();
        navigateById(currentResource);
//        scheduleJob();

    }

    /**
     * Coloca el tema según los colores que esten en las preferencias
     */
    public void setTheme(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Aesthetic.get()
                .isDark(false)
                .colorPrimary(pref.getInt(APP_PRIMARY.toString(), APP_PRIMARY.getColor()))
                .colorPrimaryDark(pref.getInt(APP_PRIMARY_DARK.toString(), APP_PRIMARY_DARK.getColor()))
                .colorAccent(pref.getInt(APP_ACCENT.toString(), APP_ACCENT.getColor()))
                .apply();
    }

    @Override
    public void onSaveInstanceState(Bundle bundel) {
        bundel.putInt(CURRENT_RESOURCE_KEY, currentResource);
        super.onSaveInstanceState(bundel);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!profilePendingList.isEmpty())
            switchFragment(profilePendingList.pop());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FavoriteList.getInstance().saveFavorites(this);
    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     */
    private void bindMainViews(){

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.main_drawer);
        navigation = findViewById(R.id.main_nav);
        navigation.setNavigationItemSelectedListener(this);

    }

    /**
     * Sección de las vista que hay en el encabezado
     */
    public abstract void bindNavigationViews();

    /**
     * Onclick para el favoriteButton para colocar el tab de favoritos
     */
    public abstract void onFavoriteButtonClick();

    /**
     * Coloca la barra de herramientas
     */
    private void setupToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
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
     * Pone a correr el sercicio de notificaciones
     */
    private void setupDispatcher(){
        GooglePlayDriver driver = new GooglePlayDriver(this);
        dispatcher = new FirebaseJobDispatcher(driver);
    }

    /**
     * Función para reconocer si el menu lateral está o no abierto
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                super.onBackPressed();
            else {
                finishAndRemoveTask();
                System.exit(0);
            }
        }
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
     * Ayuda a la función navigateToItem. La clase MainActivityNavigation
     * es la que implementa esta función
     * @param id: Identificación de botón presionado
     */
    protected abstract void navigateById(int id);

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){
        if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            FragmentTransaction transaction = createTransactionWithCustomAnimation();
            transaction.replace(R.id.main_container, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
        else {
            profilePendingList.clear();
            profilePendingList.add(fragment);
        }
    }

    /**
     * Coloca una animación personalizada al cambiar de fragmento
     */
    public FragmentTransaction createTransactionWithCustomAnimation(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        return transaction;
    }

    private void scheduleJob() {
        Job myJob = dispatcher.newJobBuilder()
                .setService(AlarmService.class)
                .setTag(CHANNEL)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 20))
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(myJob);
        showStatusMessage(getString(R.string.text_turned_on_notifications));
    }

    private void cancelJob() {
        dispatcher.cancelAll();
        showStatusMessage(getString(R.string.text_turned_off_notifications));
    }

    public Notification createNotification(String content){
        return new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle("Scheduled Notification")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_information)
                .setAutoCancel(true).build();
    }

    public void showNotification(String content){

        Notification notification = createNotification(content);
        Object service = getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;

        assert manager != null;
        createNotificationChannel(manager);
        manager.notify(0, notification);

    }

    public void createNotificationChannel(NotificationManager manager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int priority = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL, NOTIFICATION_ID, priority);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
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
