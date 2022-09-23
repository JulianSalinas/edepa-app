package edepa.app;

import static androidx.core.view.GravityCompat.START;
import static edepa.model.Preferences.FIRST_USE_KEY;
import static edepa.model.Preferences.USER_KEY;
import static edepa.model.Preferences.USE_PHOTO_KEY;
import static edepa.settings.SettingsThemeFragment.ACCENT_COLOR;
import static edepa.settings.SettingsThemeFragment.PRIMARY_COLOR;
import static edepa.settings.SettingsThemeFragment.PRIMARY_DARK_COLOR;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.BottomNavBgMode;
import com.afollestad.aesthetic.BottomNavIconTextMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jetbrains.annotations.Nullable;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.cloud.Cloud;
import edepa.cloud.CloudUsers;
import edepa.minilibs.RegexSearcher;
import edepa.model.Preferences;
import edepa.modelview.R;
import edepa.pagers.TabbedFragmentDefault;
import edepa.services.FavoritesService;

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

    FirebaseJobDispatcher dispatcher;

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
     *
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        applyCustomTheme();
        super.onCreate(savedInstanceState);
        //TODO: getLifecycle().addObserver(this);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        onCreateActivity(savedInstanceState);
        initDispatcher();
    }

    /**
     * Inicializa el #FavoritesService para chequear cada cierto tiempo
     * si uno de los favoritos del usuario  está apunto de empezar
     */
    public void initDispatcher(){
        if (Preferences.getBooleanPreference(this, Preferences.FAVORITES_KEY)) {
            dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            startDispatcher();
        }
    }

    public void startDispatcher(){
        FavoritesService.scheduleJob(dispatcher);
    }

    public void cancelDispatcher(){
        FavoritesService.cancelJob(dispatcher);
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
                    .colorPrimary(Preferences.getIntegerPreference(this, PRIMARY_COLOR))
                    .colorPrimaryDark(Preferences.getIntegerPreference(this, PRIMARY_DARK_COLOR))
                    .colorAccent(Preferences.getIntegerPreference(this, ACCENT_COLOR))
                    .bottomNavigationBackgroundMode(BottomNavBgMode.PRIMARY_DARK)
                    .bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
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

            // Se susbcribe para recibir notificaciones de noticias y el chat
            FirebaseMessaging.getInstance().subscribeToTopic(Cloud.NEWS);
            FirebaseMessaging.getInstance().subscribeToTopic(Cloud.CHAT);
        }
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
            new ActionBarDrawerToggle(this,
                    getDrawerLayout(), getToolbar(), open, close){
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, 0);
                }
            }.syncState();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void bindProfileToInterface(){
        CloudUsers cloudUsers = new CloudUsers();
        cloudUsers.setUserProfileListener(userProfile -> {
            showWelcomeMessage(userProfile.getUsername());
            showUserToolbarPhoto();
            Preferences.setPreference(this, USER_KEY, userProfile.getUsername());
        });
        cloudUsers.requestCurrentUserInfo();
    }
    /**
     * Debajo del icono de EDEPA en el menú lateral se da un mensaje de
     * bienvenida, para ello se toma el nombre de pila del usuario.
     * @see RegexSearcher#findFirstName(String)
     */
    public void showWelcomeMessage(String username) {
        username = RegexSearcher.findFirstName(username);
        String message = getResources().getString(R.string.text_welcome);

        if (!"".equals(username)) message += " " + username;

        if (getNavigationView().getHeaderView(0) != null) {
            ((TextView) getNavigationView()
                    .getHeaderView(0)
                    .findViewById(R.id.welcome_text_view))
                    .setText((message + "!"));
        }
    }

    public void showUserToolbarPhoto(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean usePhoto = prefs.getBoolean(USE_PHOTO_KEY, true);
        if (usePhoto) showUserPhoto();
    }

    public void showUserPhoto(){
        if(getSupportActionBar() != null && !isFinishing()) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);

            SimpleTarget<Drawable> target = new SimpleTarget<Drawable>() {

                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                    Drawable drawable = new BitmapDrawable(getResources(),
                            Bitmap.createScaledBitmap(bitmap, 50, 50, true));
                    getSupportActionBar().setIcon(drawable);
                }

            };

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getPhotoUrl() != null){
                Glide.with(getApplicationContext())
                        .load(user.getPhotoUrl().toString())
                        .apply(new RequestOptions()
                                .circleCrop()
                                .placeholder(R.drawable.img_user)
                                .error(R.drawable.img_user))
                        .into(target);
            }
        }
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
