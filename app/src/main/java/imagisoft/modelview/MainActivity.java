package imagisoft.modelview;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;

import java.util.Stack;
import butterknife.*;
import imagisoft.misc.*;
import imagisoft.model.Preferences;

import static android.support.v4.view.GravityCompat.START;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Deriva de {@link MainActivityFirebase} para manejar lo relacionado
 * con la conexión a la base de datos
 */
public abstract class MainActivity extends MainActivityFirebase
        implements LifecycleObserver,
        MaterialSearchView.SearchViewListener,
        NavigationView.OnNavigationItemSelectedListener{

    /**
     * Toolbar o barra superior de la aplicación
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * Contiene la toolbar, sirve para que se pueda ocultar
     * la toobar en fragmentos que no la necesitan
     * @see #toolbar
     */
    @BindView(R.id.toolbar_container)
    AppBarLayout toolbar_container;

    /**
     * Menú lateral de la aplicación, contiene las opciones
     * información, cronograma, expositores, noticias, chat,
     * configuración, acerca de, personalizar y salir
     */
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    /**
     * Es un conjunto que contiene el menú lateral, el
     * encabezado y las opciones de dicho menú
     * @see #drawerLayout
     */
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    /**
     * Barra de búsqueda que por defecto está oculta debajo de
     * la toolbar hasta que el icono de búsquedad sea presionado
     * @see #toolbar
     */
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    /**
     * Texto "Búsqueda" que contiene searchView
     * @see #searchView
     */
    @BindString(R.string.text_search)
    String textSearch;

    /**
     * Usado para saludar al iniciar la aplicación
     * @see #showWelcomeMessage()
     */
    @BindString(R.string.text_welcome)
    String textWelcome;

    /**
     * Usado para despedirse al salir de la aplicación
     * @see #exit()
     */
    @BindString(R.string.text_bye)
    String textBye;

    /**
     * Menú que se extrae de navigationView para colocar
     * los eventos a cada una de las opciones del menú
     */
    protected Menu menu;

    /**
     * Se usa para cambiar los fragmentos usando un hilo
     * diferente para que la animación se vea mas fluida.
     * Lo que hace es correr pendingRunnable cuando se cierra
     * el draweLayout. Se instancia en connectDrawerListener()
     * @see #runPendingRunnable()
     */
    private Handler handler;

    /**
     * Runneable que el handler debe correr, este contiene
     * la función para cambiar el fragmento
     * @see #runPendingRunnable()
     */
    protected Runnable pendingRunnable;

    /**
     * Si el fragmento ya está en pantalla no es
     * necesario colocarlo otra vez
     * @see #switchFragment(Fragment)
     */
    protected Fragment currentFragment;

    /**
     * Sirve para restaurar el último fragmento utilizado después
     * de que la aplicación se puso en pausa repentinamente
     * @see #restoreFromPendindgList()
     */
    protected Stack<Fragment> pendingFragments = new Stack<>();

    /**
     * Necesaria para que los fragmentos la puedan personalizar
     * @return Toolbar
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Necesaria para que el fragmento de búsqueda pueda agregar
     * sus eventos
     * @return MaterialSearchView
     */
    public MaterialSearchView getSearchView(){
        return searchView;
    }

    /**
     * Únicamente necesaria para que MainActivityFragment pueda
     * ocultar la Toolbar
     * @return AppBarLayout
     */
    public AppBarLayout getToolbarContainer(){
        return toolbar_container;
    }

    /**
     * Cuando el menú lateral se cierra se corre pendingRunnable(), el cual,
     * se hará cargo de colocar el fragmento correpondiente a la ópcion
     * seleccionada. Esto para dar fluidez al cierre del menú
     * @see #runPendingRunnable()
     */
    private DrawerLayout.SimpleDrawerListener drawerListener =
        new DrawerLayout.SimpleDrawerListener(){
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            runPendingRunnable();
            Log.i(toString(), "onDrawerClosed()");
        }
    };

    /**
     * Ejecuta en un segunda plano el cambio de fragmento. Es llamada
     * después de cerrar completamente el menú lateral
     */
    private void runPendingRunnable(){
        if(pendingRunnable != null) {
            handler.post(pendingRunnable);
            pendingRunnable = null;
            Log.i(toString(), "runPendingRunnable()");
        }
    }

    /**
     * {@inheritDoc}
     * Hace de uso de ButterKnife, remplazando las funciones
     * {@link AppCompatActivity#findViewById(int)}
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(this);
        setContentView(R.layout.main_drawer);
        ButterKnife.bind(this);
        onCreateActivity(savedInstanceState);
        Log.i(toString(), "onCreate()");
    }

    /**
     * Se revisa el argumento savedInstanceState y se redirige la
     * aplicación hacia el fragmento correspondiente
     * @param savedInstanceState
     *        Si la actividad se abre desde una notificación
     *        o se reinicia (ej. por girar la pantalla)
     * @see #onCreate(Bundle)
     */
    private void onCreateActivity(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        if(savedInstanceState == null)
            onCreateFirstCreation();
        else onCreateAlreadyOpen(savedInstanceState);
        Log.i(toString(), "onCreateActivity()");
    }

    /**
     * Se coloca el fragmento por defecto si es la primera vez
     * @see #onCreateActivity(Bundle)
     */
    private void onCreateFirstCreation(){

        Preferences prefs = Preferences.getInstance();

        if(prefs.getBooleanPreference(this, Preferences.FIRST_USE_KEY))
            writeDefaultPreferences();

        Log.i(toString(), "onCreateFirstCreation()");
    }

    /**
     * Se escriben las preferencias por defecto de la aplicación
     * @see #onCreateFirstCreation()
     */
    private void writeDefaultPreferences(){
        Preferences prefs = Preferences.getInstance();
        prefs.setPreference(this, Preferences.FIRST_USE_KEY, false);
        prefs.setPreference(this, Preferences.USER_KEY, getDefaultUsername());
        Log.i(toString(), "writeDefaultPreferences()");
    }

    /**
     * Se coloca el último fragmento usado o lo que contenga
     * el argumento savedInstanceState en sus parámetros
     * @param savedInstanceState Si la actividad se abre desde una notificación
     *                           o se reinicia (ej. por girar la pantalla)
     * @see #onCreateActivity(Bundle)
     */
    private void onCreateAlreadyOpen(Bundle savedInstanceState){
        assert savedInstanceState != null;
        Log.i(toString(), "onCreateAlreadyOpen()");
    }

    /**
     * Se configura el botón que esta en Toolbar que sirve para
     * abrir el {@link #drawerLayout}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setupToggle(){
        int open = R.string.drawer_open;
        int close = R.string.drawer_close;
        new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, open, close).syncState();
        Log.i(toString(), "setupToggle()");
    }

    /**
     * Se enlaza el {@link menu] con la actividad
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void setupNavigationMenu() {
        menu = navigationView.getMenu();
        Log.i(toString(), "setupNavigationMenu()");
    }

    /**
     * Se remueve el listener del botón fav del menú lateral
     * @see #connectFavoriteButtonListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectFavoriteButtonListener(){
        navigationView
                .getHeaderView(0)
                .findViewById(R.id.favorite_button)
                .setOnClickListener(null);
        Log.i(toString(), "disconnectFavoriteButtonListener()");
    }

    /**
     * Se configura el listener del botón fav del menú lateral
     * @see #disconnectFavoriteButtonListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectFavoriteButtonListener(){
        navigationView
                .getHeaderView(0)
                .findViewById(R.id.favorite_button)
                .setOnClickListener(v ->
                showMessage("favoriteButtonPressed"));
        Log.i(toString(), "connectFavoriteButtonListener()");
    }

    /**
     * Este listener se utiliza para que, cuando se presiona
     * una opción del menú lateral, este se cierre. Este cierre dispará
     * el evento dentro de {@link #drawerListener}
     * @see #disconnectOnNavigationItemListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void connectOnNavigationItemListener() {
        navigationView.setNavigationItemSelectedListener(this);
        Log.i(toString(), "connectOnNavigationItemListener()");
    }

    /**
     * Se desconecta el evento de presionar opciones
     * del menú lateral
     * @see #connectDrawerListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void disconnectOnNavigationItemListener() {
        navigationView.setNavigationItemSelectedListener(null);
        Log.i(toString(), "disconnectOnNavigationItemListener()");
    }

    /**
     * Se desconecta el evento de cerrar el menú lateral
     * @see #connectDrawerListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectDrawerListener(){
        drawerLayout.removeDrawerListener(drawerListener);
        Log.i(toString(), "disconnectDrawerListener()");
    }

    /**
     * Se conecta el evento de que al cerrar el menú se
     * coloque el fragmento según la opción seleccionada
     * @see #disconnectDrawerListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectDrawerListener(){
        handler = new Handler();
        drawerLayout.addDrawerListener(drawerListener);
        Log.i(toString(), "connectDrawerListener()");
    }

    /**
     * Al volver de una pausa se coloca el último fragmento
     * que quedo pendiente
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void restoreFromPendindgList(){
        if(!pendingFragments.isEmpty()) {
            switchFragment(pendingFragments.pop());
            Log.i(toString(), "restoreFromPendindgList()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Code here
        super.onSaveInstanceState(savedInstanceState);
        Log.i(toString(), "onSaveInstanceState()");
    }

    /**
     * {@inheritDoc}
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Code here
        Log.i(toString(), "onRestoreInstanceState()");
    }

    /**
     * Debajo del icono de EDEPA en el menú lateral se
     * da un mensaje de bienvenida, para ello se toma el nombre
     * de pila del usuario.
     * @see RegexUtil#findFirstName(String)
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void showWelcomeMessage(){

        String username = getDefaultUsername();
        if(!username.equals("")) textWelcome += " " + username;

        ((TextView) navigationView
                .getHeaderView(0)
                .findViewById(R.id.welcome_text_view))
                .setText((textWelcome + "!"));
    }

    /**
     * Usada por {@link #writeDefaultPreferences()}
     * Si el usuario no tiene nombre, se debe preguntar
     * en el momento de entrar al chat
     * @return Nombre de pila del usuario
     */
    private String getDefaultUsername(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return (user != null && user.getDisplayName() != null) ?
                RegexUtil.findFirstName(user.getDisplayName()) : "";
    }

    /**
     * Se configura la {@link #searchView}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupSearchView(){
        searchView.showVoice(true);
        searchView.setHint(textSearch);
        searchView.setOnSearchViewListener(this);
        searchView.setVoiceSearch(true);
        searchView.setEllipsize(true);
        Log.i(toString(), "setupSearchView()");
    }

    /**
     * {@inheritDoc}
     * Se aprovecha para enlazar la barra de búsqueda con
     * el botón que la abre
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        searchView.setMenuItem(menu.findItem(R.id.search_item));
        Log.i(toString(), "onCreateOptionsMenu()");
        return true;
    }

    /**
     * {@inheritDoc}
     * Solo se utiliza para las opciones de {@link #toolbar}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.about_item: openAbout();
            case R.id.settings_item: openSettings();
        }

        Log.i(toString(), "onOptionsItemSelected()");
        return super.onOptionsItemSelected(item);
    }

    /**
     * Abre el "Acerca de"
     */
    public abstract boolean openAbout();

    /**
     * Abre el fragmento de configuración
     */
    public abstract boolean openSettings();

    /**
     * Se ejecuta cuando se abre la {@link #searchView}
     * Se coloca un fragmento para realizar las búsquedas
     * @see #onSearchViewClosed()
     */
    @Override
    public void onSearchViewShown() {
        Log.i(toString(), "onSearchViewShown()");
    }

    /**
     * Se ejecuta cuando se cierra la {@link #searchView}
     * Se remueve el fragmento para realizar las búsquedas
     * @see #onSearchViewShown()
     */
    @Override
    public void onSearchViewClosed() {
        Log.i(toString(), "onSearchViewClosed()");
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Fragmento previamente creado
     */
    public void setFragmentOnScreen(Fragment fragment){

        Lifecycle.State state = getLifecycle().getCurrentState();

        // Si la app está en pausa no se remplaza el fragmento
        if(state.isAtLeast(Lifecycle.State.RESUMED))
            switchFragment(fragment);

        else updatePendingFragments(fragment);

        Log.i(toString(), "setFragmentOnScreen()");

    }

    /**
     * Al presionar un item del menu lateral se llama a la función para
     * colocar en la pantalla el fragment relacionado a ese item
     * @see #connectOnNavigationItemListener()
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(START);
        Log.i(toString(), "onNavigationItemSelected()");
        return true;
    }

    /**
     * Usada por la función setFragmentOnScreen
     * Crear y ejecuta la transacción para cambiar el fragmento
     * @param fragment: Fragmento a colocat en pantalla
     */
    private void switchFragment(Fragment fragment){
        if(fragment != currentFragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_content, fragment)
                    .setCustomAnimations(
                            R.animator.fade_in,
                            R.animator.fade_out)
                    .commit();
            Log.i(toString(), "switchFragment()");
        }
    }

    /**
     * Usada por la función #{@link #setFragmentOnScreen(Fragment)}
     * Si la aplicación está pausada, todas los fragmentos que se
     * deban colocar en pantalla quedan en la lista y se coloca solo el último
     * @param fragment: Fragmento que se iba a colocar pero la aplicación
     *                  estaba en pausa
     */
    private void updatePendingFragments(Fragment fragment){
        pendingFragments.clear();
        pendingFragments.add(fragment);
        Log.i(toString(), "updatePendingFragments()");
    }

    /**
     * {@inheritDoc}
     * Función usada al presionar el botón hacia atrás
     */
    @Override
    public void onBackPressed() {

        if (searchView.isSearchOpen())
            searchView.closeSearch();

        else if (drawerLayout.isDrawerOpen(START))
            drawerLayout.closeDrawer(START);

        else super.onBackPressed();
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showMessage(String msg){
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Utiliza un string en vez un recurso
     * @param resource: R.string.<resource>
     * @see #showMessage(int)
     */
    public void showMessage(int resource){
        String msg = getResources().getString(resource);
        showMessage(msg);
    }

    /**
     * Cierra la aplicación sin dejarla en segundo plano
     * @return true si la aplicación cerró correctamente
     */
    public boolean exit(){
        showMessage(textBye);
        finishAndRemoveTask();
        System.exit(0);
        Log.i(toString(), "exit()");
        return true;
    }

    /**
     * Hace lo mismo que {@link #exit()} pero además cierra la sesión
     * del usuario, por lo que al abrirla nuevamente tiene que hacer login
     * @return true si la aplicación cerró correctamente
     */
    public boolean exitAndSignOut(){
        AuthUI.getInstance().signOut(this);
        return exit();
    }

    /**
     * {@inheritDoc}
     * Se utiliza como etiqueta para el debugger
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
