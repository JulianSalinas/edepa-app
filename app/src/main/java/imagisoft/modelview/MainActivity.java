package imagisoft.modelview;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.misc.RegexUtil;


public class MainActivity extends AppCompatActivity
        implements LifecycleObserver, MaterialSearchView.SearchViewListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindString(R.string.text_search)
    String textSearch;

//    protected Stack<Fragment> pendingFragments = new Stack<>();

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(this);
        setContentView(R.layout.main_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if(savedInstanceState == null)
            onCreateFirstCreation();

        else onCreateAlreadyOpen(savedInstanceState);

    }


    private void onCreateFirstCreation(){
        Log.i(toString(), "onCreateFirstCreation()");
    }

    private void onCreateAlreadyOpen(Bundle savedInstanceState){
        Log.i(toString(), "onCreateAlreadyOpen()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setupToggle(){

        int open = R.string.drawer_open;
        int close = R.string.drawer_close;

        new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, open, close).syncState();

        Log.i(toString(), "setupToggle()");

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Code here
        super.onSaveInstanceState(savedInstanceState);
        Log.i(toString(), "onSaveInstanceState()");
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Code here

        Log.i(toString(), "onRestoreInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(toString(), "onStart()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setWelcomeMessage(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String welcomeMsg = getResources().getString(R.string.text_welcome);

        if (user != null && user.getDisplayName() != null)
            welcomeMsg += " " + RegexUtil.findFirstName(user.getDisplayName());

        ((TextView) navigationView
                .getHeaderView(0)
                .findViewById(R.id.welcome_text_view))
                .setText((welcomeMsg + "!"));

        Log.i(toString(), "setWelcomeMessage()");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupSearchView(){
        searchView.showVoice(true);
        searchView.setHint(textSearch);
        searchView.setOnSearchViewListener(this);
        searchView.setVoiceSearch(true);
        searchView.setEllipsize(true);
        Log.i(toString(), "setupSearchView()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationView(){

        Menu menu = navigationView.getMenu();

        menu.findItem(R.id.nav_exit)
            .setOnMenuItemClickListener(item -> closeApplication());

        menu.findItem(R.id.nav_news)
            .setOnMenuItemClickListener(item -> openNews());

        menu.findItem(R.id.nav_chat)
            .setOnMenuItemClickListener(item -> openChat());

        menu.findItem(R.id.nav_about)
            .setOnMenuItemClickListener(item -> openAbout());

        menu.findItem(R.id.nav_people)
            .setOnMenuItemClickListener(item -> openPeople());

        menu.findItem(R.id.nav_pallete)
            .setOnMenuItemClickListener(item -> openPallete());

        menu.findItem(R.id.nav_schedule)
            .setOnMenuItemClickListener(item -> openSchedule());

        menu.findItem(R.id.nav_settings)
            .setOnMenuItemClickListener(item -> openSettings());

        menu.findItem(R.id.nav_infomation)
            .setOnMenuItemClickListener(item -> openInformation());

        menu.findItem(R.id.nav_exit_and_signout)
            .setOnMenuItemClickListener(item -> signOutAndCloseApplication());

        Log.i(toString(), "setupNavigationView()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem aboutItem = menu.findItem(R.id.about_item);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        MenuItem settingsItem = menu.findItem(R.id.settings_item);

        searchView.setMenuItem(searchItem);
        aboutItem.setOnMenuItemClickListener(item -> openAbout());
        settingsItem.setOnMenuItemClickListener(item -> openSettings());

        Log.i(toString(), "onCreateOptionsMenu()");
        return true;
    }

    public boolean openInformation(){
        Log.i(toString(), "openInformation()");
        return true;
    }

    public boolean openSchedule(){
        Log.i(toString(), "openSchedule()");
        return true;
    }

    public boolean openFavorites(){
        Log.i(toString(), "openFavorites()");
        return true;
    }

    public boolean openPeople(){
        Log.i(toString(), "openPeople()");
        return true;
    }

    public boolean openNews(){
        Log.i(toString(), "openNews()");
        return true;
    }

    public boolean openChat(){
        Log.i(toString(), "openChat()");
        return true;
    }

    public boolean openSettings(){
        Log.i(toString(), "openSettings()");
        return true;
    }

    public boolean openAbout(){
        Log.i(toString(), "openAbout()");
        return true;
    }

    public boolean openPallete(){
        Log.i(toString(), "openPallete()");
        return true;
    }

    public boolean closeApplication(){
        finishAndRemoveTask();
        System.exit(0);
        Log.i(toString(), "closeApplication()");
        return true;
    }

    public boolean signOutAndCloseApplication(){
        AuthUI.getInstance().signOut(this);
        return closeApplication();
    }

    @Override
    public void onSearchViewShown() {
        Log.i(toString(), "onSearchViewShown()");
    }

    @Override
    public void onSearchViewClosed() {
        Log.i(toString(), "onSearchViewClosed()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(toString(), "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(toString(), "onDestroy()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(toString(), "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(toString(), "onDestroy()");
    }

    /**
     * Función usada al presionar el botón hacia atrás
     */
    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();

        if (searchView.isSearchOpen())
            searchView.closeSearch();

        else if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);

        else if (fm.getBackStackEntryCount() > 0)
            super.onBackPressed();

        else closeApplication();

    }

    @Override
    public String toString() {
        return getClass().getName();
    }

}
