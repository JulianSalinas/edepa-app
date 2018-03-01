package imagisoft.rommie;

import android.os.Bundle;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarConfiguration();
        setDrawerConfiguration();
        setToggleConfiguration();
        setNavigationViewConfiguration();
        setFragment(new FragmentTabs());
    }

    private void setToolbarConfiguration(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Yahei font on title bar
        // toolbar.setTitleTextAppearance(this, R.style.YaheiBoldTextAppearance);
    }

    private void setDrawerConfiguration(){
        drawer = findViewById(R.id.drawer_layout);
    }

    private void setToggleConfiguration(){
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();
    }

    private void setNavigationViewConfiguration(){
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        navigateToItem(item);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void navigateToItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_exit:
                finishAffinity(); break;
            case R.id.nav_infomation:
                setFragment(new FragmentInfo()); break;
            case R.id.nav_schedule:
                setFragment(new FragmentTabs()); break;
            case R.id.nav_agenda:
                setFragment(new FragmentTabs()); break;
            default:
                showStatusMessage(getResources().getString(R.string.enter) + " " + item.getTitle()); break;
        }
    }

    public void showStatusMessage(String msg){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();

    }

}
