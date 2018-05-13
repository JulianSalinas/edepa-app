package imagisoft.rommie;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.afollestad.aesthetic.Aesthetic;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Locale;

import butterknife.BindView;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.Preferences;
import imagisoft.miscellaneous.ColorConverter;

import static imagisoft.rommie.CustomColor.APP_PRIMARY;
import static imagisoft.rommie.CustomColor.APP_PRIMARY_DARK;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class MainActivityCustom extends MainActivityClassic
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Constantes utilizadas para el servio de notificaciones
     */
    protected final String CHANNEL_KEY = "CHANNEL_KEY";
    protected final String REMAINDER_KEY = "REMAINDER_KEY";
    protected final String CURRENT_RESOURCE_KEY = "CURRENT_RESOURCE_KEY";

    /**
     * Lista de favoritos
     */
    protected FavoriteList favoriteList;

    /**
     * Es el encargado de programar las notificaciones
     */
    protected FirebaseJobDispatcher dispatcher;

    /**
     * Atributo particular de la aplicación para usar tabs para
     * mostrar el cronograma, los favoritos y los eventos en curso
     */
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    /**
     * Necesario para que los fragmenos que necesitan los tabs
     * puedan ocultarlos
     */
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    /**
     * Se define el tema y el lenguaje
     * Se activa el sistema de notificaciones
     * Se inicializa la lista de favoritos
     * @param bundle: No se utiliza
     */
    @Override
    protected void onCreate(Bundle bundle) {

        Aesthetic.attach(this);

        setTheme();
        setLanguage();
        setupDispatcher();

        favoriteList = FavoriteList.getInstance();
        favoriteList.loadFavorites(this);

        super.onCreate(bundle);

    }

    /**
     * Al cerrar la aplicación se guarda la lista de favoritos
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteList.saveFavorites(this);
    }

    /**
     * Cierra absolutamente la aplicación
     * Antes de hacerlo guarda los favoritos
     */
    public void onExit(){
        onDestroy();
        finishAndRemoveTask();
        System.exit(0);
    }

    /**
     * Se da color a las partes de la aplicación que no pueden ser cambiadas
     * por la librería aesthetics de forma automática.
     */
    private void setTheme(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int primaryColor = prefs.getInt(APP_PRIMARY.toString(), APP_PRIMARY.getColor());
        int primaryDark = prefs.getInt(APP_PRIMARY_DARK.toString(), APP_PRIMARY_DARK.getColor());
        getWindow().setStatusBarColor(primaryDark);
        getWindow().setNavigationBarColor(primaryColor);
    }

    /**
     * Se coloca el idioma de la aplicación en las preferencias
     * Si no ha sido colocado antes, por defecto toma el del teléfono.
     */
    public void setLanguage(){

        Preferences prefs = Preferences.getInstance();
        String lang = prefs.getStringPreference(this, Preferences.LANG_KEY_VALUE);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            prefs.setPreference(this, Preferences.LANG_KEY_VALUE, lang);
        }

        updateLanguage(lang);

    }

    /**
     * Al cambiar las preferencias se llama esta función para actualizar
     * el idioma en la aplicación, aun así se debe recrear la actividad
     * para poder aplicar los cambios en la interfaz.
     * @param lang: "en" o "es"
     */
    public void updateLanguage(String lang){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);
    }

    /**
     * Tambien cierra la barra de búsqueda
     * si es necesario
     */
    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen())
            searchView.closeSearch();
        super.onBackPressed();
    }

    /**
     * Pone a correr el sercicio de notificaciones
     */
    private void setupDispatcher(){
        GooglePlayDriver driver = new GooglePlayDriver(this);
        dispatcher = new FirebaseJobDispatcher(driver);
    }


    private void scheduleJob() {
        Job myJob = dispatcher.newJobBuilder()
                .setService(AlarmService.class)
                .setTag(CHANNEL_KEY)
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

    public Notification createNotification(String title, String content){
        return new NotificationCompat.Builder(this, CHANNEL_KEY)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).build();
    }

    public void showNotification(Notification notification){

        Object service = getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;

        assert manager != null;
        createNotificationChannel(manager);
        manager.notify(0, notification);

    }

    public void createNotificationChannel(NotificationManager manager){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_KEY, REMAINDER_KEY,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);

        }

    }

}
