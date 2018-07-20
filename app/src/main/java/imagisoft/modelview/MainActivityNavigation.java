package imagisoft.modelview;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class MainActivityNavigation extends MainActivity {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationExit(){
        menu.findItem(R.id.nav_exit)
                .setOnMenuItemClickListener(item -> exit());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationNews(){
        menu.findItem(R.id.nav_news)
                .setOnMenuItemClickListener(item -> openNews());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationChat(){
        menu.findItem(R.id.nav_chat)
                .setOnMenuItemClickListener(item -> openChat());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationAbout(){
        menu.findItem(R.id.nav_about)
                .setOnMenuItemClickListener(item -> openAbout());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationPeople(){
        menu.findItem(R.id.nav_people)
                .setOnMenuItemClickListener(item -> openPeople());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationPallete(){
        menu.findItem(R.id.nav_pallete)
                .setOnMenuItemClickListener(item -> openPallete());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationSchedule(){
        menu.findItem(R.id.nav_schedule)
                .setOnMenuItemClickListener(item -> openSchedule());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationSettings(){
        menu.findItem(R.id.nav_settings)
                .setOnMenuItemClickListener(item -> openSettings());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationInformation(){
        menu.findItem(R.id.nav_infomation)
                .setOnMenuItemClickListener(item -> openInformation());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupNavigationExitAndSignOut(){
        menu.findItem(R.id.nav_exit_and_signout)
                .setOnMenuItemClickListener(item -> exitAndSignOut());
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
        pendingRunnable = () -> setFragmentOnScreen(new ChatFragment());
        Log.i(toString(), "openChat()");
        return false;
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

}
