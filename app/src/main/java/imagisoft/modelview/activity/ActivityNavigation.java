package imagisoft.modelview.activity;

import imagisoft.modelview.R;
import imagisoft.listeners.ChildListener;
import imagisoft.modelview.chat.ChatFragment;
import imagisoft.modelview.about.AboutFragment;
import imagisoft.modelview.schedule.PagerFragment;
import imagisoft.modelview.schedule.EventsOngoing;
import static imagisoft.model.Preferences.*;

import android.util.Log;
import android.view.MenuItem;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;


public class ActivityNavigation extends ActivityCustom  {

    /**
     * Conecta la configuración online con las preferencias
     * locales
     * @see #disconnectPreferencesListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectPreferencesListener(){
        getConfigReference().addChildEventListener(prefsListener);
    }

    /**
     * Desconecta la configuración online cuando la
     * aplicación se pausa
     * @see #connectPreferencesListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectPreferencesListener(){
        getConfigReference().removeEventListener(prefsListener);
    }

    /**
     * {@inheritDoc}
     */
    protected void onCreateFirstCreation(){
        super.onCreateFirstCreation();
        openChat();
        handler.post(pendingRunnable);
    }

    /**
     * Configura la opción información
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationInformation(){
        MenuItem menuItem = menu.findItem(R.id.nav_infomation);
        Boolean isAvailable = isAvailable(INFO_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> isAvailable ? openInformation(): null);
    }

    /**
     * Configura la opción de noticias
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationNews(){
        MenuItem menuItem = menu.findItem(R.id.nav_news);
        Boolean isAvailable = isAvailable(NEWS_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> isAvailable ? openNews(): null);
    }

    /**
     * Configura la opción de chat
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationChat(){
        MenuItem menuItem = menu.findItem(R.id.nav_chat);
        Boolean isAvailable = isAvailable(CHAT_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> isAvailable ? openChat(): null);
    }

    /**
     * Configura la opción expositores o personas
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationPeople(){
        MenuItem menuItem = menu.findItem(R.id.nav_people);
        Boolean isAvailable = isAvailable(PEOPLE_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> isAvailable ? openPeople(): null);
    }

    /**
     * Configura la opción paleta de colores
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationPalette(){
        MenuItem menuItem = menu.findItem(R.id.nav_palette);
        Boolean isAvailable = isAvailable(PALETTE_AVAILABLE_KEY);
        menuItem.setVisible(isAvailable);
        menuItem.setOnMenuItemClickListener(item -> isAvailable ? openPalette(): null);
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
        Log.i(toString(), "openInformation()");
        return false;
    }

    /**
     * Coloca el fragmento de Cronograma en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openSchedule(){
        Log.i(toString(), "openSchedule()");
        return false;
    }

    /**
     * Coloca el fragmento de Favoritos en {@link #pendingRunnable}
     * para que al momento de cerrar el menú lateral el fragmento
     * sea colocado en pantalla
     */
    public boolean openFavorites(){
        Log.i(toString(), "openFavorites()");
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
        Log.i(toString(), "openNews()");
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
        Log.i(toString(), "openSettings()");
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();

        if (pos == 0){
            String tag = "SCHEDULE_FRAGMENT";
            Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
            Fragment frag = temp != null ? temp : new PagerFragment.Schedule();
            pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        }

        else if (pos == 1){
            String tag = "FAVORITES_FRAGMENT";
            Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
            Fragment frag = temp != null ? temp : new PagerFragment.Favorites();
            pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        }

        else if (pos == 2){
            String tag = "ONGOING_FRAGMENT";
            Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
            Fragment frag = temp != null ? temp : new EventsOngoing();
            pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        }

        runPendingRunnable();
        Log.i(toString(), "onTabSelected("+ String.valueOf(pos) +")");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Requerido
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Requerido
    }

    /**
     * Es activada cuando cambia alguna de las preferencias
     * disponibles en Firebase
     * @see #connectPreferencesListener()
     */
    private ChildEventListener prefsListener = new ChildListener(){
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Boolean value = dataSnapshot.getValue(Boolean.class);
            Log.i(toString(), "onChildChanged("+ key +", " + value.toString() + ")");
            setAvailable(key, value);
            updateMenu(key);
        }
    };

    /**
     * Actualiza la interfaz una vez que las
     * preferencias que están disponibles online
     * han sido cargadas localmente
     * @see #onDataChange(DataSnapshot)
     */
    @Override
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
     * Es usada por {@link #prefsListener}
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

}
