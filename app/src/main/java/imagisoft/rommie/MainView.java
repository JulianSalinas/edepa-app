package imagisoft.rommie;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.Locale;

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
     * Variables usadas para correr el servicio de notificaciones
     */
    private FirebaseJobDispatcher dispatcher;

    /**
     * El Id de las notificaciones y el canal están basados en el lenguaje del teléfono
     */
    private static final String CHANNEL = Locale.getDefault() == Locale.ENGLISH ?
            "Servcio de notificaciones" : "Notifications Service";

    private static final String NOTIFICATION_ID = Locale.getDefault() == Locale.ENGLISH ?
            "Reminders" : "Recordatorios";

    /**
     * Atributos en común para todas las aplicaciones. Barra de herramientas, menu lateral, etc.
     */
    protected Toolbar toolbar;
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
        setContentView(R.layout.main_drawer);
        bindViews();
        setupToolbar();
        setupToggle();
        setupDispatcher();
        navigateById(R.id.nav_schedule);
        scheduleJob();
    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     */
    private void bindViews(){

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.main_drawer);

        navigation = findViewById(R.id.main_nav);
        navigation.setNavigationItemSelectedListener(this);

        View header = navigation.getHeaderView(0);
        favoriteButton = header.findViewById(R.id.favorite_button);
        currentSection = header.findViewById(R.id.current_section_view);

        favoriteButton.setOnClickListener(this);

    }

    /**
     * Onclick para el favoriteButton
     */
    @Override
    public void onClick(View v) {
        showNotification("Hola mundo cruel");
    }

    /**
     * Pone a correr el sercicio de notificaciones
     */
    private void setupDispatcher(){
        GooglePlayDriver driver = new GooglePlayDriver(this);
        dispatcher = new FirebaseJobDispatcher(driver);
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
