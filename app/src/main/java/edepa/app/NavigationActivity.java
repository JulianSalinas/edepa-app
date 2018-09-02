package edepa.app;

import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.model.Preferences;
import edepa.cloud.CloudNavigation;
import edepa.minilibs.PathGenerator;

import edepa.chat.ChatFragment;
import edepa.info.InfoFragment;
import edepa.info.AboutFragment;
import edepa.people.PeopleFragment;
import edepa.notices.NoticesFragment;
import edepa.pagers.TabbedFragmentByType;
import edepa.pagers.TabbedFragmentDefault;
import edepa.search.SearchByPanelFragment;
import edepa.search.SearchByEventsFragment;
import edepa.search.SearchByPeopleFragment;
import edepa.settings.SettingsThemeFragment;
import edepa.settings.SettingsGeneralFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


/**
 * Clase encargada de manejar toda la navegabilidad de la aplicación,
 * incluyendo abrir el fragmento de búsquedad cuando se presione el inicio
 */
public class NavigationActivity extends MainActivity implements
        MaterialSearchView.SearchViewListener,
        CloudNavigation.CloudNavigationListener {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se revisa si la app está siendo abierta por otra, si es así
        // se revisan los datos de entrada que pueden ser una imagen
        // texto o una url
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        boolean isSendAction = Intent.ACTION_SEND.equals(action);
        if (isSendAction && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
            else if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        }

    }

    /**
     * La aplicación ha sido abierta por medio del botón de compartir de
     * alguna otra aplicación. Se coloca el texto compartido en el chat
     * @param intent: Contiene el texto que se quiere compartir
     */
    public void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Bundle params = new Bundle();
            params.putString(ChatFragment.INPUT_IMAGE_KEY, sharedText);
            openChat(params);
        }
    }

    /**
     * La aplicación ha sido abierta por medio del botón de compartir de
     * alguna otra aplicación. Se coloca la imagen compartida en el chat
     * @param intent: Contiene la imagen que se quiere compartir
     */
    public void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Bundle params = new Bundle();
            String imagePath = PathGenerator.getRealPathFromUri(this, imageUri);
            params.putString(ChatFragment.INPUT_IMAGE_KEY, imagePath);
            openChat(params);
        }
    }

    /**
     * La primera vez que se crea la Actividad se debe colocar
     * un fragmento en pantalla, este fragmento es {@link TabbedFragmentDefault}
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
     * es {@link TabbedFragmentDefault}. Este método es utilizado únicamente
     * en {@link #onCreateFirstTime()}
     */
    public void setOnScreenFirstFragment(){
        String tag = "TABBED_FRAGMENT";
        Fragment frag = new TabbedFragmentByType();
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

        // Abre la sección de mensajes (chat)
        else if (args.containsKey(Cloud.CHAT)){
            args.remove(Cloud.CHAT);
            openChat();
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
     * La diferencia con {@link #openChat()} es que esta función es
     * invocada cuando el usuario comparte texto o imagenes desde otra app
     * @param params: Contiene el texto o url de la imagen que se comparte
     */
    public void openChat(Bundle params){
        String tag = "CHAT_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new ChatFragment();
        frag.setArguments(params);
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        runPendingRunnable();
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
        String tag = "THEME_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new SettingsThemeFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
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
        Fragment frag = temp != null ? temp : new TabbedFragmentByType();
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
        TabbedFragmentDefault frag = temp != null ?
                (TabbedFragmentDefault) temp : new TabbedFragmentByType();

        // Si está visible solamente ordena que se mueva a los favoritos
        if (frag.isVisible()) {
            frag.moveToTab(TabbedFragmentDefault.FAVORITES);
            onBackPressed(); // Cierra el drawer
        }

        // Si no está visible se debe pasar como parámetro el número
        // de tab para los favoritos
        else {
            int tab = TabbedFragmentDefault.FAVORITES;
            Bundle args = new Bundle();
            args.putInt(TabbedFragmentDefault.ITEM_KEY, tab);
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
        Fragment frag = temp != null ? temp : new SettingsGeneralFragment();
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
     * Cambia la vista del cronograma y de los favoritos, pues los eventos
     * se deben visualizar tanto por fecha como por tipos
     * @return True si el evento ha sido manejado
     */
    public boolean changeViewMode() {

        String key = Preferences.VIEW_KEY;
        String currentView = Preferences.getStringPreference(this, key);

        if (Preferences.VIEW_DEFAULT.equals(currentView)){
            Preferences.setPreference(this, key, Preferences.VIEW_BY_TYPE);
        }
        else {
            Preferences.setPreference(this, key, Preferences.VIEW_DEFAULT);
        }

        String tag = "TABBED_FRAGMENT";
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);

        // Con esto obliga al pager a recrear todos los fragmentos
        // de lo contrario usa los que tiene en cache y la vista no cambia
        if (frag.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
            setOnScreenFirstFragment();
        }

        showMessage(R.string.text_view_mod_changed);
        return false;
    }

    /**
     * Coloca en pantalla un fragmento que ya ha sido agregado al
     * fragment manager
     * @param fragment Fragmento a visualizar
     */
    public void showAddedFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .show(fragment)
                .commit();
    }

    /**
     * Agrega un fragment al fragment manager y lo muestra en pantalla
     * @param fragment Fragmento por agregar
     * @param tag Identificador del fragmento
     */
    public void addAndShowFragment(Fragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_content, fragment, tag)
                .commit();
    }

    /**
     * Abre el fragmento para realizar búsquedas mediante algún filtro
     */
    public void openSearchByPanel(){
        String tag = "SEARCH_BY_PANEL";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new SearchByPanelFragment();
        if(frag.isAdded()) showAddedFragment(frag);
        else addAndShowFragment(frag, tag);
    }

    /**
     * Abre el fragmento para buscar personas
     */
    public void openSearchByPeople() {
        String tag = "SEARCH_BY_PEOPLE";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new SearchByPeopleFragment();
        if(frag.isAdded()) showAddedFragment(frag);
        else addAndShowFragment(frag, tag);
    }

    public void openSearchByEvents(Bundle args){
        String tag = "SEARCH_BY_EVENTS";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new SearchByEventsFragment();
        frag.setArguments(args);
        if(frag.isAdded()) showAddedFragment(frag);
        else addAndShowFragment(frag, tag);
    }

    /**
     * Se ejecuta cuando se abre la barra de búsqueda
     * Se coloca un fragmento para realizar las búsquedas
     * @see #onSearchViewClosed()
     */
    @Override
    public void onSearchViewShown() {
        Fragment peopleFragment = getSupportFragmentManager()
                .findFragmentByTag("PEOPLE_FRAGMENT");
        if(peopleFragment != null && peopleFragment.isVisible())
            openSearchByPeople();
        else openSearchByPanel();
        exitFlag = false;
    }

    /**
     * Se ejecuta cuando se cierra la barra de búsqueda
     * Se remueve el fragmento para realizar las búsquedas
     * @see #onSearchViewShown()
     */
    @Override
    public void onSearchViewClosed() {

        searchView.setOnQueryTextListener(null);
        searchView.setQuery("", false);

        Fragment frag = getSupportFragmentManager()
                .findFragmentByTag("SEARCH_BY_PEOPLE");

        if (frag == null) {
            frag = getSupportFragmentManager()
                    .findFragmentByTag("SEARCH_BY_EVENTS");
        }

        if (frag != null && frag.isAdded()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(frag)
                    .commit();
        }

        frag = getSupportFragmentManager()
                .findFragmentByTag("SEARCH_BY_PANEL");

        if (frag != null && frag.isAdded()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(frag)
                    .commit();
        }

    }

}
