package imagisoft.modelview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import imagisoft.misc.RegexUtil;
import imagisoft.model.FavoriteList;
import imagisoft.model.FavoriteListener;
import imagisoft.model.Preferences;
import imagisoft.model.ScheduleEvent;
import imagisoft.model.ViewedList;
import imagisoft.modelview.activity.MainActivityFirebase;
import imagisoft.services.AlarmReceiver;
import imagisoft.services.AlarmService;

import static imagisoft.modelview.DefaultColor.APP_PRIMARY;
import static imagisoft.modelview.DefaultColor.APP_PRIMARY_DARK;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class ActivityCustom extends MainActivityFirebase
        implements NavigationView.OnNavigationItemSelectedListener, FavoriteListener {

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

//        Aesthetic.attach(this);

        setTheme();
        setLanguage();
        setupDispatcher();
        setupFavoriteList();
        super.onCreate(bundle);
        setWelcomeMessage();

    }

    private void setWelcomeMessage(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String welcomeMsg = getResources().getString(R.string.text_welcome);

        if (user != null && user.getDisplayName() != null)
            welcomeMsg += " " + RegexUtil.findFirstName(user.getDisplayName());

//        ((TextView) navigation
//                .getHeaderView(0)
//                .findViewById(R.id.welcome_text_view))
//                .setText((welcomeMsg + "!"));

    }

    private void setTheme() {
        int primaryColor = getDefaultColor(APP_PRIMARY);
        int primaryDark = getDefaultColor(APP_PRIMARY_DARK);
        getWindow().setStatusBarColor(primaryDark);
        getWindow().setNavigationBarColor(primaryColor);
    }

    /**
     * Usada por settheme para obtener los colores por defecto
     * @param defaultColor: DefaultColor  de la aplicación
     * @return color por defecto en DefaultColor
     */
    private int getDefaultColor(DefaultColor defaultColor){
        return PreferenceManager
                .getDefaultSharedPreferences(this)
                .getInt(defaultColor.toString(), defaultColor.getColor());
    }

    /**
     * Se coloca el idioma de la aplicación en las preferencias
     * Si no ha sido colocado antes, por defecto toma el del teléfono.
     */
    public void setLanguage(){

        Preferences prefs = Preferences.getInstance();
        String lang = prefs.getStringPreference(this, Preferences.LANG_KEY);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            prefs.setPreference(this, Preferences.LANG_KEY, lang);
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
     * Mueve los favoritos a memoria y coloca un
     * listener para ejecutar acciones cuando se agregue un evento
     */
    private void setupFavoriteList(){
        favoriteList = FavoriteList.getInstance();
        favoriteList.addListener(this);
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
     * Crea una alarma con base al evento agregado en favoritos
     * @param event: Evento favorito
     */
    @Override
    public void onFavoriteAdded(ScheduleEvent event) {

        DatabaseReference eventReference = getScheduleReference().child(event.getId());

        eventReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ScheduleEvent event = mutableData.getValue(ScheduleEvent.class);

                if (event == null)
                    return Transaction.success(mutableData);
                else {
                    event.setFavoritesAmount(event.getFavoritesAmount() + 1);
                }

                mutableData.setValue(event);
                Log.i("ActivityCustom::", "favoritesAmountUpdated::" +
                        String.valueOf(event.getFavoritesAmount()));

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

        new Handler().post(() -> {
            FavoriteList.getInstance().save(this);
            ViewedList.getInstance().save(this);
        });

    }

    /**
     * Remueve la alarma al borrar el evento de favoritos
     * @param event: Evento removido de favoritos
     */
    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {

        DatabaseReference eventReference = getScheduleReference().child(event.getId());

        eventReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ScheduleEvent event = mutableData.getValue(ScheduleEvent.class);

                if (event == null)
                    return Transaction.success(mutableData);
                else {
                    event.setFavoritesAmount(event.getFavoritesAmount() - 1);
                }

                mutableData.setValue(event);
                Log.i("ActivityCustom::", "favoritesAmountUpdated::" +
                        String.valueOf(event.getFavoritesAmount()));

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

    }

    /**
     * Pone a correr el sercicio de notificaciones
     */
    private void setupDispatcher(){
        GooglePlayDriver driver = new GooglePlayDriver(this);
        dispatcher = new FirebaseJobDispatcher(driver);
    }

    public void startAlarmAtParticularTime(ScheduleEvent event) {
        long now = System.currentTimeMillis();
        // long eventStart = event.getStart();
        long eventStart = now + TimeUnit.SECONDS.toMillis(20);
        long alarmTime = eventStart - now;
        startAlarmAtParticularTime(alarmTime, event);
    }

    public void startAlarmAtParticularTime(long alarmTime, ScheduleEvent event) {

        Bundle args = new Bundle();
        args.putParcelable("event", event);

        Intent intent = new Intent(".AlarmReceiver");
        intent.setClass(this, AlarmReceiver.class);
        intent.putExtras(args);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            startService(intent);
        }


    }

    private void createEventAlarm() {
        Job myJob = dispatcher.newJobBuilder()
                .setService(AlarmService.class)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 20))
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(myJob);
    }

    @Override
    public String toString() {
        return "ActivityCustom{" + super.toString() + "}";
    }

}
