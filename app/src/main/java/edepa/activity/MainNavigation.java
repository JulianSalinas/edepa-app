package edepa.activity;

import edepa.modelview.R;
import edepa.model.Cloud;
import edepa.model.Preferences;
import edepa.about.InfoFragment;
import edepa.chat.ChatFragment;
import edepa.about.AboutFragment;
import edepa.news.NewsFragment;
import edepa.people.PeopleFragment;
import edepa.pagers.PagerDetails;
import edepa.pagers.TabbedFragment;
import edepa.settings.SettingsFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static edepa.model.Preferences.CHAT_AVAILABLE_KEY;
import static edepa.model.Preferences.INFO_AVAILABLE_KEY;
import static edepa.model.Preferences.NEWS_AVAILABLE_KEY;
import static edepa.model.Preferences.PALETTE_AVAILABLE_KEY;
import static edepa.model.Preferences.PEOPLE_AVAILABLE_KEY;

/**
 * Clase encargada de manejar toda la navegabilidad
 * de la aplicación
 */
public class MainNavigation extends MainActivity
        implements ValueEventListener, ChildEventListener {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreateFirstCreation(){
        super.onCreateFirstCreation();

        if(!handleIntent(getIntent())){
            String tag = "SCHEDULE_FRAGMENT";
            Fragment frag = new TabbedFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, frag, tag)
                    .commitNow();
        }

    }

    private boolean handleIntent(Intent intent){

        Bundle args = intent.getExtras();
        if(args != null && args.containsKey(Cloud.NEWS)) {
            args.remove(Cloud.NEWS);
            openNews();
            runPendingRunnable();
            return true;
        }

        else if (args != null && args.containsKey(Cloud.CONFIG)){
            args.remove(Cloud.CONFIG);
            openSettings();
            runPendingRunnable();
            return true;
        }

        return false;

    }

    /**
     * Al tocar una notificación mientras la aplicación está abierta
     * hace que ésta funcion sea llamada para colocar la nueva pantalla
     * @param intent: Contiene que pantalla debe abrir la aplicación
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * Se reinicia la aplicación, conservado el estado actual
     */
    public void restartApplication(String currentSection){

        Intent refresh = new Intent(this, MainNavigation.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        refresh.putExtra(currentSection, true);

        int code = 123456;
        PendingIntent intent = PendingIntent.getActivity(
                this, code, refresh, PendingIntent.FLAG_CANCEL_CURRENT);

        startActivity(refresh);


        AlarmManager mgr = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        if (mgr != null) {
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, intent);
            exit();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeDefaultPreferences() {
        super.writeDefaultPreferences();
        Cloud.getInstance()
                .getReference(Cloud.CONFIG)
                .addListenerForSingleValueEvent(this);
    }

    /**
     * Conecta la configuración online con las preferencias
     * locales
     * @see #disconnectPreferencesListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectPreferencesListener(){
        Cloud.getInstance()
                .getReference(Cloud.CONFIG)
                .addChildEventListener(this);
    }

    /**
     * Desconecta la configuración online cuando la
     * aplicación se pausa
     * @see #connectPreferencesListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectPreferencesListener(){
        Cloud.getInstance()
                .getReference(Cloud.CONFIG)
                .removeEventListener((ChildEventListener) this);
    }

    /**
     * Se configura el fragment del botón fav del menú lateral
     * @see #disconnectFavoriteButtonListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectFavoriteButtonListener(){
        getNavigationView()
                .getHeaderView(0)
                .findViewById(edepa.modelview.R.id.favorite_button)
                .setOnClickListener(v -> {});
        Log.i(toString(), "connectFavoriteButtonListener()");
    }

    /**
     * Se remueve el fragment del botón fav del menú lateral
     * @see #connectFavoriteButtonListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectFavoriteButtonListener(){
        getNavigationView()
                .getHeaderView(0)
                .findViewById(edepa.modelview.R.id.favorite_button)
                .setOnClickListener(null);
        Log.i(toString(), "disconnectFavoriteButtonListener()");
    }

    /**
     * Configura la opción información
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationInformation(){
        MenuItem menuItem = menu.findItem(R.id.nav_infomation);
        boolean isAvailable = isAvailable(INFO_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> openInformation());
    }

    /**
     * Configura la opción de noticias
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationNews(){
        MenuItem menuItem = menu.findItem(R.id.nav_news);
        boolean isAvailable = isAvailable(NEWS_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> openNews());
    }

    /**
     * Configura la opción de chat
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationChat(){
        MenuItem menuItem = menu.findItem(R.id.nav_chat);
        boolean isAvailable = isAvailable(CHAT_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> openChat());
    }

    /**
     * Configura la opción expositores o personas
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationPeople(){
        MenuItem menuItem = menu.findItem(R.id.nav_people);
        boolean isAvailable = isAvailable(PEOPLE_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> openPeople());
    }

    /**
     * Configura la opción paleta de colores
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationPalette(){
        MenuItem menuItem = menu.findItem(R.id.nav_palette);
        boolean isAvailable = isAvailable(PALETTE_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> openPalette());
    }

    /**
     * Configura la opción cronograma
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationSchedule(){
        MenuItem menuItem = menu.findItem(R.id.nav_schedule);
        menuItem.setOnMenuItemClickListener(item -> openSchedule());
    }

    /**
     * Configura la opción preferencias
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationSettings(){
        MenuItem menuItem = menu.findItem(R.id.nav_settings);
        menuItem.setOnMenuItemClickListener(item -> openSettings());
    }

    /**
     * Configura la opción "acerca de "
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationAbout(){
        MenuItem menuItem = menu.findItem(R.id.nav_about);
        menuItem.setOnMenuItemClickListener(item -> openAbout());
    }

    /**
     * Configura la opción de salir
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationExit(){
        MenuItem menuItem = menu.findItem(R.id.nav_exit);
        menuItem.setOnMenuItemClickListener(item -> exit());
    }

    /**
     * Configura la opción de cerrar sesión y salir
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationExitAndSignOut(){
        MenuItem menuItem = menu.findItem(R.id.nav_exit_and_signout);
        menuItem.setOnMenuItemClickListener(item -> exitAndSignOut());
    }

    /**
     * Coloca el fragmento de Información en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openInformation(){
        String tag = "INFO_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new InfoFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el fragmento de Cronograma en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openSchedule(){
        String tag = "SCHEDULE_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new TabbedFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

//    Fragment details = new EventDetail();

    public void openDetails(String eventKey){
        String tag = "DETAILS_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new PagerDetails();
//        Fragment frag = details;
        Bundle args = new Bundle();
        args.putString("eventKey", eventKey);
        frag.setArguments(args);
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        runPendingRunnable();
    }

    /**
     * Coloca el fragmento de Personas en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openPeople(){
        String tag = "PEOPLE_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new PeopleFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el fragmento de Noticias en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openNews(){
        String tag = "NEWS_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new NewsFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el fragmento de Chat en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openChat(){
        String tag = "CHAT_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new ChatFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el fragmento de Preferencias en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openSettings(){
        String tag = "SETTINGS_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new SettingsFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el fragmento de Paleta de colores en
     * {@link #pendingRunnable} para que al momento de cerrar el
     * menú lateral el fragmento sea colocado en pantalla
     */
    public boolean openPalette(){
        Log.i(toString(), "openPalette()");
        return false;
    }

    /**
     * Coloca el fragmento de About en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openAbout() {
        String tag = "ABOUT_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new AboutFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Se ejecuta cuando se abre la barra de búsqueda
     * Se coloca un fragmento para realizar las búsquedas
     * @see #onSearchViewClosed()
     */
    @Override
    public void onSearchViewShown() {
        Log.i(toString(), "onSearchViewShown()");
    }

    /**
     * Se ejecuta cuando se cierra la barra de búsqueda
     * Se remueve el fragmento para realizar las búsquedas
     * @see #onSearchViewShown()
     */
    @Override
    public void onSearchViewClosed() {
        Log.i(toString(), "onSearchViewClosed()");
    }

    /**
     * Actualiza la interfaz una vez que las
     * preferencias que están disponibles online
     * han sido cargadas localmente
     * @see #onDataChange(DataSnapshot)
     */
    public void update(){
        updateMenu(NEWS_AVAILABLE_KEY);
        updateMenu(INFO_AVAILABLE_KEY);
        updateMenu(CHAT_AVAILABLE_KEY);
        updateMenu(PEOPLE_AVAILABLE_KEY);
        updateMenu(PALETTE_AVAILABLE_KEY);
        Log.i(toString(), "update()");
    }

    /**
     * Actualiza alguna opción del menú lateral
     * @param key: Key de la clase Preferences
     */
    private void updateMenu(String key){
        switch (key.toUpperCase()){
            case NEWS_AVAILABLE_KEY: setupNavigationNews(); break;
            case INFO_AVAILABLE_KEY: setupNavigationInformation(); break;
            case CHAT_AVAILABLE_KEY: setupNavigationChat(); break;
            case PEOPLE_AVAILABLE_KEY: setupNavigationPeople(); break;
            case PALETTE_AVAILABLE_KEY: setupNavigationPalette(); break;
        }
    }

    /**
     * Escribe en las preferencias si una sección de la
     * aplicación debe estar disponible
     * @param key: Key de la clase Preferences
     * @param value: La sección debe o no estar disponible
     */
    public void setAvailable(String key, Boolean value){
        key = key.toLowerCase();
        Preferences.getInstance().setPreference(this, key, value);
    }

    /**
     * Utilizada por la funcion {@link #onDataChange(DataSnapshot)}
     * @param key: Key de la clase Preferences
     * @param dataSnapshot: Donde se extrae en valor que se debe colocar
     *                      en las preferencias
     */
    public void setAvailable(String key, DataSnapshot dataSnapshot){
        key = key.toLowerCase();
        Boolean value = dataSnapshot.child(key).getValue(Boolean.class);
        Preferences.getInstance().setPreference(this, key, value);
    }

    /**
     * Determina con base a las preferencias si una sección de
     * la aplicación debe estar disponible
     * @param key: Key de la clase Preferences
     * @return True si la sección debe estar disponible
     */
    public boolean isAvailable(String key){
        key = key.toLowerCase();
        return Preferences
                .getInstance()
                .getBooleanPreference(this, key);
    }

    /**
     * Obtienen todas las preferencias desde el dataSnapshot
     * y las escribe con {@link #setAvailable(String, DataSnapshot)}
     * @param dataSnapshot: Contiene las preferencias según la BD
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.i(toString(), "onDataChange(DataSnapshot)");
        setAvailable(Preferences.INFO_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.NEWS_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.CHAT_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.PALETTE_AVAILABLE_KEY, dataSnapshot);
        setAvailable(Preferences.PEOPLE_AVAILABLE_KEY, dataSnapshot);
        update();
    }

    /**
     * Es activada cuando cambia alguna de las preferencias
     * disponibles en Firebase
     * @see #connectPreferencesListener()
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        Boolean value = dataSnapshot.getValue(Boolean.class);
        if (value != null && key != null) {
            setAvailable(key, value);
            updateMenu(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // Requerido
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        // Requerido
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        // Requerido
    }

    /**
     * Ha ocurrido un error al leer las preferencias desde Firebase
     * @param databaseError: Contiene el mensaje con el error
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

}
