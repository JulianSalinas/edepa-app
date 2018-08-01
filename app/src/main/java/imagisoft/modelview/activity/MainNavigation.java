package imagisoft.modelview.activity;

import imagisoft.modelview.R;
import imagisoft.model.Cloud;
import imagisoft.model.Preferences;
import imagisoft.modelview.about.InfoFragment;
import imagisoft.modelview.chat.ChatFragment;
import imagisoft.modelview.about.AboutFragment;
import imagisoft.modelview.news.NewsFragment;
import imagisoft.modelview.schedule.EventsOngoing;
import imagisoft.modelview.schedule.PagerFragment;
import imagisoft.modelview.settings.SettingsFragment;

import android.util.Log;
import android.view.MenuItem;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static imagisoft.model.Preferences.CHAT_AVAILABLE_KEY;
import static imagisoft.model.Preferences.INFO_AVAILABLE_KEY;
import static imagisoft.model.Preferences.NEWS_AVAILABLE_KEY;
import static imagisoft.model.Preferences.PALETTE_AVAILABLE_KEY;
import static imagisoft.model.Preferences.PEOPLE_AVAILABLE_KEY;

/**
 * Clase encargada de manejar toda la navegabilidad
 * de la aplicación
 */
public class MainNavigation extends MainActivity
        implements TabLayout.OnTabSelectedListener,
        ValueEventListener, ChildEventListener {

    /**
     * {@inheritDoc}
     */
    protected void onCreateFirstCreation(){
        super.onCreateFirstCreation();
        String tag = "SETTINGS_FRAGMENT";
        Fragment frag = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, frag, tag)
                .commitNow();
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
                .findViewById(imagisoft.modelview.R.id.favorite_button)
                .setOnClickListener(v -> activeTabbedMode());
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
                .findViewById(imagisoft.modelview.R.id.favorite_button)
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
        Fragment frag = temp != null ? temp : new EventsOngoing();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el fragmento de Personas en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openPeople(){
        Log.i(toString(), "openPeople()");
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
    public boolean openAbout(){
        String tag = "ABOUT_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new AboutFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    /**
     * Coloca el tab de Ongoing en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public void openOngoingTab(){
        String tag = "ONGOING_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new EventsOngoing();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
    }

    /**
     * Coloca el tab de Schedule en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public void openScheduleTab(){
        String tag = "SCHEDULE_FRAGMENT_TAB";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new PagerFragment.Schedule();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
    }

    /**
     * Coloca el tab de Favoritos en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public void openFavoritesTab(){
        String tag = "FAVORITES_FRAGMENT_TAB";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new PagerFragment.Favorites();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
    }

    /**
     * Colca el fragmento según el tab elegido
     * @param tab: Tab que contiene la posición
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        int pos = tab.getPosition();
//        switch (pos){
//            case 0: openScheduleTab(); break;
//            case 1: openFavoritesTab(); break;
//            case 2: openOngoingTab(); break;
//        }   runPendingRunnable();
//        Log.i(toString(), "onTabSelected("+ String.valueOf(pos) +")");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Requerido
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Requerido
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
     * Coloca los tabs y remueve la elevación de la toolbar
     * @see #deactiveTabbedMode()
     */
    public void activeTabbedMode(){
        getToolbarContainer().setElevation(0);
        getToolbarTabs().setVisibility(View.VISIBLE);
//        getToolbarTabsLayout().setOnTabSelectedListener(this);
    }

    /**
     * Pone invisibles los tabs y coloca la elevación
     * @see #activeTabbedMode()
     */
    public void deactiveTabbedMode() {
        getToolbarContainer().setElevation(4);
        getToolbarTabs().setVisibility(View.GONE);
//        getToolbarTabsLayout().setOnTabSelectedListener(null);
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
