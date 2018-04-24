package imagisoft.rommie;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import android.support.v7.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowAnimationFrameStats;
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

import com.afollestad.aesthetic.Aesthetic;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import static imagisoft.rommie.CustomColor.*;
import imagisoft.edepa.FavoriteList;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class MainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public final int FADE_ANIMATION = 0;
    public final int SLIDE_ANIMATION = 1;

    /**
     * Variables usadas para correr el servicio de notificaciones
     */
    private FirebaseJobDispatcher dispatcher;
    private final String NOTIFICATION_ID = "Recordatorios";
    private final String CHANNEL = "Servicio de notificaciones";

    /**
     * Atributos en común para todas las aplicaciones.
     */
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView navigation;
    protected ActionBarDrawerToggle toggle;

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Se inician todos los componentes principales de la aplicación
     */
    @Override
    protected void onCreate (Bundle bundle) {

        Aesthetic.attach(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().setStatusBarColor(pref.getInt(APP_PRIMARY_DARK.toString(), APP_PRIMARY_DARK.getColor()));
        getWindow().setNavigationBarColor(pref.getInt(APP_ACCENT_DARK.toString(), APP_ACCENT_DARK.getColor()));

        super.onCreate(bundle);
        if (Aesthetic.isFirstTime()) setTheme();

        FavoriteList.getInstance().loadFavorites(this);
        setContentView(R.layout.main_drawer);

        bindMainViews();
        bindNavigationViews();
        setupToolbar();
        setupToggle();
        setupDispatcher();
        navigateById(R.id.nav_schedule);
        scheduleJob();

    }

    /**
     * Coloca el tema según los colores que esten en las preferencias
     */
    public void setTheme(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Aesthetic.get()
                .isDark(true)
                .colorPrimary(pref.getInt(APP_PRIMARY.toString(), APP_PRIMARY.getColor()))
                .colorPrimaryDark(pref.getInt(APP_PRIMARY_DARK.toString(), APP_PRIMARY_DARK.getColor()))
                .colorAccent(pref.getInt(APP_ACCENT.toString(), APP_ACCENT.getColor()))
                .apply();
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
        showStatusMessage(getString(R.string.turned_on_notifications));
    }

    private void cancelJob() {
        dispatcher.cancelAll();
        showStatusMessage(getString(R.string.turned_off_notifications));
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
