package edepa.app;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.AppBarLayout;
import android.view.inputmethod.InputMethodManager;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.LifecycleObserver;

import android.widget.Toast;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.view.ActionMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;

import java.util.Stack;
import butterknife.BindView;
import butterknife.ButterKnife;

import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.model.Preferences;
import edepa.minilibs.RegexSearcher;
import edepa.pagers.TabbedFragmentDefault;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.BottomNavBgMode;
import com.afollestad.aesthetic.BottomNavIconTextMode;

import org.jetbrains.annotations.Nullable;

import static edepa.model.Preferences.USER_KEY;
import static edepa.model.Preferences.FIRST_USE_KEY;
import static android.support.v4.view.GravityCompat.START;

/**
 * Actividad principal de aplicación. En ésta se colocan cada uno de los
 * fragmentos existentes en el contenedor #R.id.content
 */
public abstract class MainActivity extends AppCompatActivity
        implements LifecycleObserver, NavigationView.OnNavigationItemSelectedListener {

    /**
     * Menú que se extrae de {@link #navigationView} para colocar los eventos
     * a cada una de las opciones del menú.
     * Se instancia en {@link #onCreateActivity(Bundle)}i
     */
    protected Menu menu;

    /**
     * Al presionar el botón de atrás, se avisa al usuario que tien que
     * presionarlo otra vez para salir. Esto para evitar que se salga de la
     * aplicación por error. Cuando la bandera es True se puede salir, esto
     * se aplica en el método {@link #onBackPressed()}
     */
    protected boolean exitFlag;

    /**
     * Necesaria para que los fragmentos la puedan personalizar u ocultar
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Contenedor de la toolbar, se utiliza para poder ocultar completamente
     * la {@link #toolbar} de lo contrario queda una panel en blanco
     */
    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;

    public FrameLayout getToolbarContainer(){
        return toolbarContainer;
    }

    /**
     * Contenedor de {@link #toolbarContainer}. Se utiliza para poder remover
     * la elevación de la {@link #toolbar} en
     * {@link TabbedFragmentDefault#customizeActivity()}
     */
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    /**
     * Es un conjunto que contiene el menú lateral, el encabezado y las
     * opciones de dicho menú. Se utiliza para obtener la instancia de
     * {@link #menu}
     */
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    public NavigationView getNavigationView(){
        return navigationView;
    }

    /**
     * Menú lateral de la aplicación, contiene las opciones información,
     * cronograma, expositores, noticias, chat, configuración, acerca de,
     * personalizar y salir
     */
    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    /**
     * Barra de búsqueda que por defecto está oculta debajo de la toolbar
     * hasta que el icono de búsquedad sea presionado
     */
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    public MaterialSearchView getSearchView(){
        return searchView;
    }

    /**
     * Botón que abre el menú lateral
     */
    private ActionBarDrawerToggle toggle;

    /**
     * Se usa para cambiar los fragmentos usando un hilo diferente para que
     * la animación se vea mas fluida. Lo que hace es correr
     * {@link #pendingRunnable} cuando se cierra el draweLayout.
     * @see #runPendingRunnable()
     */
    protected Handler handler;

    /**
     * Runnable que el handler debe correr, este contiene la función para
     * cambiar el fragmento
     * @see #runPendingRunnable()
     */
    protected Runnable pendingRunnable;

    /**
     * Sirve para restaurar el último fragmento utilizado después de que la
     * aplicación se puso en pausa repentinamente. Se utiliza los tags de los
     * fragmentos no los fragmentos en si mismos
     * @see #restoreFromPendindgList()
     */
    protected Stack<String> pendingFragments = new Stack<>();

    /**
     * Sirve para restablecer el accent después de abrir un Context Action Bar
     */
    protected int lastStatusBarColor;

    /**
     * Listener para el menú lateral. Ejecuta {@link #hideKeyboard()} al
     * abrirse y {@link #runPendingRunnable()} al cerrarse
     */
    private DrawerLayout.SimpleDrawerListener drawerListener =
        new DrawerLayout.SimpleDrawerListener(){

        /**
         * En ocasiones el teclado queda abierto después de abrir el menú,
         * para solucionar esto se utiliza {@link MainActivity#hideKeyboard()}
         * al abrir dicho menú
         */
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            hideKeyboard();
        }

    };

    /**
     * Ejecuta en un segunda plano el cambio de fragmento.
     * Es llamada después de cerrar completamente el menú lateral
     * @see #drawerListener
     */
    protected void runPendingRunnable(){
        if(pendingRunnable != null) {
            handler.post(pendingRunnable);
            pendingRunnable = null;
        }
    }

    /**
     * {@inheritDoc}
     * Hace de uso de ButterKnife, remplazando las funciones
     * {@link AppCompatActivity#findViewById(int)}
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        applyCustomTheme();
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(this);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        onCreateActivity(savedInstanceState);
    }

    /**
     * Aplica un tema personalizado usando la librería Aesthetc en caso
     * de que así haya sido seleccionado en las preferencias
     */
    public void applyCustomTheme(){
        String key = Preferences.THEME_KEY;
        if(Preferences.getBooleanPreference(this, key, false)) {
            Aesthetic.attach(this);
            Aesthetic.get()
                    .bottomNavigationBackgroundMode(BottomNavBgMode.ACCENT)
                    .bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_PRIMARY)
                    .apply();
        }
    }


    /**
     * Se revisa el argumento savedInstanceState y se redirige la aplicación
     * hacia el fragmento correspondiente
     * @see #onCreate(Bundle)
     */
    protected void onCreateActivity(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//
//        Glide.with(this)
//                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        Bitmap bitmap =((BitmapDrawable) resource).getBitmap();
//                        Drawable drawable = new BitmapDrawable(getResources(),
//                                Bitmap.createScaledBitmap(bitmap, 40, 40, true));
//                        getSupportActionBar().setIcon(drawable);
//                    }
//                });

        handler = new Handler();
        menu = navigationView.getMenu();
        if(savedInstanceState == null) onCreateFirstTime();
    }

    /**
     * Se revisa si es la primera vez que se abre la aplicación y se subscribe
     * a las notificaciones de #FirebaseMessaging
     * @see #onCreateActivity(Bundle)
     */
    protected void onCreateFirstTime(){
        if(Preferences.getBooleanPreference(this, FIRST_USE_KEY)){

            // Se coloca que la aplicación ya tuvo su primer uso
            Preferences.setPreference(this, FIRST_USE_KEY, false);
            Preferences.setPreference(this, USER_KEY, getDefaultUsername());

            // Se susbcribe para recibir notificaciones de noticias y el chat
            FirebaseMessaging.getInstance().subscribeToTopic(Cloud.NEWS);
            FirebaseMessaging.getInstance().subscribeToTopic(Cloud.CHAT);
        }
    }

    /**
     * Se introduce el backstack listener para poder cambiar el icono de
     * {@link #toggle} toggle cuando el usuario haya abierto más de dos
     * fragmentos
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void connectBackStackListener(){
        FragmentManager manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this::showToggle);
    }

    /**
     * Se configura el botón que esta en la {@link #toolbar} para abrir el
     * menú lateral
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setupToggle(){
        if(getDrawerLayout() != null) {
            int open = R.string.drawer_open;
            int close = R.string.drawer_close;
            if (toggle == null) toggle =
                    new ActionBarDrawerToggle(this,
                            getDrawerLayout(),
                            getToolbar(), open, close);
            showToggle();
        }
    }

    /**
     * Muestra el icono para volver hacia el fragmento anterior o el que
     * sirve para abrir el menú dependiendo de cuantos fragmentos tenga
     * abiertos el usuario (se coloca en caso de una pantalla secundaria )
     */
    public void showToggle(){
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) showBackButton();
        else showHamburger();
    }

    /**
     * Muestra el icono en la {@link #toolbar} para volver hacia el
     * fragmento anterior
     * @see #setupToggle()
     */
    public void showBackButton(){
        if(getDrawerLayout() != null) {

            toggle.setDrawerIndicatorEnabled(false);

            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(v -> onBackPressed());

            getDrawerLayout()
                    .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    /**
     * Muestra icono en la {@link #toolbar} para abrir el menú lateral
     * @see #setupToggle()
     */
    public void showHamburger() {
        if (getDrawerLayout() != null) {

            toggle.setDrawerIndicatorEnabled(true);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            toggle.syncState();
            toolbar.setNavigationOnClickListener(v ->
                    getDrawerLayout().openDrawer(START));

            getDrawerLayout()
                    .setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    /**
     * Debajo del icono de EDEPA en el menú lateral se da un mensaje de
     * bienvenida, para ello se toma el nombre de pila del usuario.
     * @see RegexSearcher#findFirstName(String)
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void showWelcomeMessage(){

        String username = Preferences.getStringPreference(this, USER_KEY);
        String message = getResources().getString(R.string.text_welcome);

        if(!username.equals("")) message += " " + username;

        if (getNavigationView().getHeaderView(0) != null) {
            ((TextView) getNavigationView()
                    .getHeaderView(0)
                    .findViewById(R.id.welcome_text_view))
                    .setText((message + "!"));
        }
    }

    /**
     * Usada por {@link #onCreateFirstTime()} para obtener el nombre de pila
     * de usuario. Este se extrae de la información que se obtiene con el Login
     * @return Nombre de pila del usuario
     */
    private String getDefaultUsername(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return (user != null && user.getDisplayName() != null) ?
                RegexSearcher.findFirstName(user.getDisplayName()) : "";
    }

    /**
     * Este fragment se utiliza para que, cuando se presiona una opción del
     * menú lateral, este se cierre. Este cierre dispará el evento dentro de
     * {@link #drawerListener}
     * @see #disconnectOnNavigationItemListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void connectOnNavigationItemListener() {
        getNavigationView().setNavigationItemSelectedListener(this);
    }

    /**
     * Se desconecta el evento de presionar opciones del menú lateral
     * @see #connectDrawerListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void disconnectOnNavigationItemListener() {
        getNavigationView().setNavigationItemSelectedListener(null);
    }

    /**
     * Se desconecta el evento de cerrar el menú lateral
     * @see #connectDrawerListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectDrawerListener(){
        if (getDrawerLayout() != null) {
            getDrawerLayout().removeDrawerListener(drawerListener);
        }
    }

    /**
     * Se conecta el evento de que al cerrar el menú se
     * coloque el fragmento según la opción seleccionada
     * @see #disconnectDrawerListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectDrawerListener(){
        if(getDrawerLayout() != null) {
            getDrawerLayout().addDrawerListener(drawerListener);
        }
    }

    /**
     * Al volver de una pausa se coloca el último fragmento
     * que quedo pendiente
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void restoreFromPendindgList(){
        if(!pendingFragments.isEmpty()) {
            String tag = pendingFragments.pop();
            Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
            if(frag != null) switchFragment(frag, tag);
        }
    }

    /**
     * {@inheritDoc}
     * A la vez se aprovecha para enlazar la barra de búsqueda
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(toString(), "onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem searh_item = menu.findItem(R.id.search_item);
        searchView.setMenuItem(searh_item);
        return true;
    }

    /**
     * {@inheritDoc}
     * Solo se utiliza para las opciones home, about y settings
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(toString(), "onOptionsItemSelected()");
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();return true;
            case R.id.about_item: openAbout(); break;
            case R.id.settings_item: openSettings(); break;
        }
        runPendingRunnable();
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     * Se cambia el accent de la statusBar al entrar el modo CAB
     */
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        lastStatusBarColor = getWindow().getStatusBarColor();
    }

    /**
     * {@inheritDoc}
     * Se restablece el accent de la statusBar después de salir del modo CAB
     */
    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        getWindow().setStatusBarColor(lastStatusBarColor);
    }

    /**
     * Abre el "Acerca de"
     * Es implementado en #NavigationActivity
     */
    public abstract boolean openAbout();

    /**
     * Abre el fragmento de configuración
     * Es implementado en #NavigationActivity
     */
    public abstract boolean openSettings();

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Fragmento previamente creado
     * @param tag Tag que pertenece al fragmento
     */
    public void setFragmentOnScreen(Fragment fragment, String tag){

        Lifecycle.State state = getLifecycle().getCurrentState();

        // Si la app está en pausa no se remplaza el fragmento
        if(state.isAtLeast(Lifecycle.State.RESUMED))
            switchFragment(fragment, tag);
        else updatePendingFragments(tag);
    }

    /**
     * Al presionar un item del menu lateral se llama a la función para
     * colocar en la pantalla el fragment relacionado a ese item
     * @see #connectOnNavigationItemListener()
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (getDrawerLayout() != null) {
            getDrawerLayout().closeDrawer(START);
        }
        return true;
    }

    /**
     * Usada por la función {@link #setFragmentOnScreen(Fragment, String)}
     * Crear y ejecuta la transacción para cambiar el fragmento
     * @param fragment: Fragmento a colocat en pantalla
     * @param tag: Tag del fragmento
     */
    protected void switchFragment(Fragment fragment, String tag){
        exitFlag = false;
        if(!fragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_content, fragment, tag)
                    .commit();
        }
    }

    /**
     * Usada por la función #{@link #setFragmentOnScreen(Fragment, String)}
     * Si la aplicación está pausada, todas los fragmentos que se
     * deban colocar en pantalla quedan en la lista y se coloca solo el último
     * @param tag: Tag del fragmento que se iba a colocar
     * @see #restoreFromPendindgList()
     */
    protected void updatePendingFragments(String tag){
        Log.i(toString(), "updatePendingFragments()");
        pendingFragments.clear();
        pendingFragments.add(tag);
    }

    /**
     * Función usada al presionar el botón atrás
     */
    @Override
    public void onBackPressed() {
        hideKeyboard();

        if(searchView.isSearchOpen()) {
            searchView.closeSearch();
        }

        if (drawerLayout != null && drawerLayout.isDrawerOpen(START)) {
            drawerLayout.closeDrawer(START);
        }

        else {

            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 1) {
                manager.popBackStack();
            }

            else if (manager.getBackStackEntryCount() <= 0) {
                if (exitFlag) exit();
                else {
                    exitFlag = true;
                    showMessage(R.string.text_press_again_to_exit);
                }
            }

            else super.onBackPressed();

        }
    }

    /**
     * Oculta el teclado. Se usa cuando el usuario termina de escribi y
     * presiona otro elemento de la vista
     */
    public void hideKeyboard() {
        View focus = getCurrentFocus();
        Object service = getSystemService(INPUT_METHOD_SERVICE);
        InputMethodManager inputM = (InputMethodManager) service;
        if(inputM != null && focus != null)
            inputM.hideSoftInputFromWindow(focus.getWindowToken(), 0);
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
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
        finishAndRemoveTask();
        System.exit(0);
        return true;
    }

    /**
     * Hace lo mismo que {@link #exit()} pero además cierra la sesión
     * del usuario, por lo que al abrirla nuevamente tiene que hacer login
     * @return true si la aplicación cerró correctamente
     */
    public boolean exitAndSignOut(){
        AuthUI.getInstance().signOut(this);
        Preferences.setPreference(this, FIRST_USE_KEY, true);
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
