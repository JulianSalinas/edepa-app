package edepa.app;

import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.cloud.CloudNavigation;

import edepa.info.InfoFragment;
import edepa.chat.ChatFragment;
import edepa.info.AboutFragment;
import edepa.people.PeopleFragment;
import edepa.pagers.TabbedFragment;
import edepa.search.SearchFragment;
import edepa.notices.NoticesFragment;
import edepa.settings.SettingsFragment;

import android.util.Log;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;


import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Objects;

/**
 * Clase encargada de manejar toda la navegabilidad de la aplicación,
 * incluyendo abrir el fragmento de búsquedad cuando se presione el inicio
 */
public class NavigationActivity extends MainActivity implements
        MaterialSearchView.SearchViewListener,
        CloudNavigation.CloudNavigationListener {

    /**
     * La primera vez que se crea la Actividad se debe colocar
     * un fragmento en pantalla, este fragmento es {@link TabbedFragment}
     */
    @Override
    protected void onCreateFirstTime(){
        super.onCreateFirstTime();
        Bundle args = getIntent().getExtras();
        if(args == null || !moveToScreen(args))
            setOnScreenFirstFragment();
    }

    /**
     * Se coloca el fragmento por defecto en pantalla, este fragmento
     * es {@link TabbedFragment}. Este método es utilizado únicamente
     * en {@link #onCreateFirstTime()}
     */
    public void setOnScreenFirstFragment(){
        String tag = "TABBED_FRAGMENT";
        Fragment frag = new TabbedFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, frag, tag)
                .commitNow();
    }

    /**
     * Al tocar una notificación mientras la aplicación está abierta
     * hace que ésta funcion sea llamada para colocar la nueva pantalla
     * @param intent: Contiene que pantalla debe abrir la aplicación
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle args = intent.getExtras();
        if(args != null) moveToScreen(args);
    }

    /**
     * Se mueve hacia una pantalla o fragmento específicado por la ubicación
     * que contiene el Bundle. Es usada por {@link #onNewIntent(Intent)}
     * cuando el usuario presiona una notificación
     * @param args Bundle con la descripción de qué fragmento abrir
     * @return True si lográ abrir algun fragmento con los argumentos
     */
    private boolean moveToScreen(Bundle args){

        // Abre la sección de noticias
        if(args.containsKey(Cloud.NEWS)) {
            args.remove(Cloud.NEWS);
            openNews();
            runPendingRunnable();
            return true;
        }

        // Abre la sección de configuración
        else if (args.containsKey(Cloud.CONFIG)){
            args.remove(Cloud.CONFIG);
            openSettings();
            runPendingRunnable();
            return true;
        }

        // No se pudo abrir ningún fragmento
        return false;

    }

    /**
     * Conecta la configuración online con las preferencias locales
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void connectPreferencesListener(){
        CloudNavigation cloudNavigation = new CloudNavigation();
        cloudNavigation.setNavigationListener(this);
        cloudNavigation.requestNavigationSections();
    }

    /**
     * Se configura la barra de búsqueda
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupSearchView(){
        String text = getString(R.string.text_search);
        searchView.showVoice(true);
        searchView.setHint(text);
        searchView.setOnSearchViewListener(this);
        searchView.setEllipsize(true);
    }

    /**
     * Se configura el fragment del botón fav del menú lateral
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectFavoriteButtonListener(){
        getNavigationView()
                .getHeaderView(0)
                .findViewById(R.id.event_favorite_button)
                .setOnClickListener(v -> openFavorites());
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
        String tag = "TABBED_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new TabbedFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Hace los mismo que {@link #openSchedule()} con la diferencia de
     * que abre por defecto la pestaña de favoritos. Usada por botón
     * de favoritos
     */
    public void openFavorites(){

        String tag = "TABBED_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        TabbedFragment frag = temp != null ? (TabbedFragment) temp : new TabbedFragment();

        // Si está visible solamente ordena que se mueva a los favoritos
        if (frag.isVisible()) {
            frag.moveToTab(TabbedFragment.FAVORITES);
            onBackPressed(); // Cierra el drawer
        }

        // Si no está visible se debe pasar como parámetro el número
        // de tab para los favoritos
        else {
            Bundle args = new Bundle();
            args.putInt(TabbedFragment.ITEM_KEY, TabbedFragment.FAVORITES);
            frag.setArguments(args);
            pendingRunnable = () -> setFragmentOnScreen(frag, tag);
            runPendingRunnable();
        }

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
