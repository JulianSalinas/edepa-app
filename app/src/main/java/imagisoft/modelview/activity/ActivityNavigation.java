package imagisoft.modelview.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;

import imagisoft.modelview.about.AboutFragment;
import imagisoft.modelview.schedule.EventsOngoing;
import imagisoft.modelview.R;
import imagisoft.modelview.chat.ChatFragment;
import imagisoft.modelview.schedule.PagerFragment;

public class ActivityNavigation extends ActivityCustom {

    /**
     * {@inheritDoc}
     */
    protected void onCreateFirstCreation(){
        super.onCreateFirstCreation();
        activeTabbedMode();
        String tag = "PAGER_SCHEDULE";
        Fragment frag = new PagerFragment.Schedule();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, frag, tag)
                .commitNow();
    }

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
        String tag = "CHAT_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new ChatFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    public boolean openSettings(){
        Log.i(toString(), "openSettings()");
        return true;
    }

    public boolean openAbout(){
        String tag = "ABOUT_FRAGMENT";
        Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new AboutFragment();
        pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        return false;
    }

    public boolean openPallete(){
        Log.i(toString(), "openPallete()");
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();

        if (pos == 2){
            String tag = "ONGOING_FRAGMENT";
            Fragment temp = getSupportFragmentManager().findFragmentByTag(tag);
            Fragment frag = temp != null ? temp : new EventsOngoing();
            pendingRunnable = () -> setFragmentOnScreen(frag, tag);
        }

        Log.i(toString(), "onTabSelected("+ String.valueOf(pos) +")");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
