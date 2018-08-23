package edepa.app;

import edepa.cloud.CloudNavigation;
import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.model.Preferences;
import edepa.info.InfoFragment;
import edepa.chat.ChatFragment;
import edepa.info.AboutFragment;
import edepa.notices.NoticesFragment;
import edepa.people.PeopleFragment;
import edepa.pagers.TabbedFragment;
import edepa.search.SearchFragment;
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
public class NavigationActivity
        extends MainActivity implements CloudNavigation.CloudNavigationListener {

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

        Intent refresh = new Intent(this, NavigationActivity.class);
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
     * Conecta la configuración online con las preferencias locales
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void connectPreferencesListener(){
        CloudNavigation cloudNavigation = new CloudNavigation(this);
        cloudNavigation.requestNavigationSections();
    }

    /**
     * Se configura el fragment del botón fav del menú lateral
     * @see #disconnectFavoriteButtonListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectFavoriteButtonListener(){
        getNavigationView()
                .getHeaderView(0)
                .findViewById(R.id.favorite_button)
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
                .findViewById(R.id.favorite_button)
                .setOnClickListener(null);
        Log.i(toString(), "disconnectFavoriteButtonListener()");
    }

    /**
     * Configura la opción información
     */
    @Override
    public void onInfoStateChange(boolean state) {
        MenuItem menuItem = menu.findItem(R.id.nav_infomation);
        menuItem.setOnMenuItemClickListener(state ? item -> openInformation() : null);
        menuItem.setVisible(state);
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
     * Configura la opción de noticias
     */
    @Override
    public void onNewsStateChange(boolean state) {
        MenuItem menuItem = menu.findItem(R.id.nav_news);
        menuItem.setOnMenuItemClickListener(state ? item -> openNews() : null);
        menuItem.setVisible(state);
    }

    /**
     * Coloca el fragmento de Noticias en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openNews(){
        String tag = "NEWS_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new NoticesFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Configura la opción de chat
     */
    @Override
    public void onChatStateChange(boolean state) {
        MenuItem menuItem = menu.findItem(R.id.nav_chat);
        menuItem.setOnMenuItemClickListener(state ? item -> openChat() : null);
        menuItem.setVisible(state);
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
     * Configura la opción expositores o personas
     */
    @Override
    public void onPeopleStateChange(boolean state) {
        MenuItem menuItem = menu.findItem(R.id.nav_people);
        menuItem.setOnMenuItemClickListener(state ? item -> openPeople() : null);
        menuItem.setVisible(state);
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
     * Configura la opción paleta de colores
     */
    @Override
    public void onPaletteStateChange(boolean state) {
        MenuItem menuItem = menu.findItem(R.id.nav_palette);
        menuItem.setOnMenuItemClickListener(state ? item -> openPalette() : null);
        menuItem.setVisible(state);
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
     * Configura la opción cronograma
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationSchedule(){
        MenuItem menuItem = menu.findItem(R.id.nav_schedule);
        menuItem.setOnMenuItemClickListener(item -> openSchedule());
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

    /**
     * Configura la opción preferencias
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationSettings(){
        MenuItem menuItem = menu.findItem(R.id.nav_settings);
        menuItem.setOnMenuItemClickListener(item -> openSettings());
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
     * Configura la opción "acerca de "
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationAbout(){
        MenuItem menuItem = menu.findItem(R.id.nav_about);
        menuItem.setOnMenuItemClickListener(item -> openAbout());
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
     * Se ejecuta cuando se abre la barra de búsqueda
     * Se coloca un fragmento para realizar las búsquedas
     * @see #onSearchViewClosed()
     */
    @Override
    public void onSearchViewShown() {
        String tag = "SEARCH_FRAGMENT";
        Fragment frag = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, frag, tag)
                .commit();
    }

    /**
     * Se ejecuta cuando se cierra la barra de búsqueda
     * Se remueve el fragmento para realizar las búsquedas
     * @see #onSearchViewShown()
     */
    @Override
    public void onSearchViewClosed() {
        String tag = "SEARCH_FRAGMENT";
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        if (frag != null) getSupportFragmentManager()
                .beginTransaction()
                .remove(frag).commit();
    }

}
