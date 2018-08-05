package imagisoft.modelview.schedule.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import imagisoft.model.Cloud;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;
import imagisoft.modelview.loaders.EventsLoader;
import imagisoft.modelview.interfaces.IEventsSubject;


public class EventPager extends MainFragment {

    @BindView(R.id.view_pager)
    HorizontalInfiniteCycleViewPager viewPager;

    private DetailsAdapter adapter;

    private List<ScheduleEvent> events;

    @Override
    public int getResource() {
        return R.layout.schedule_details;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        events = new ArrayList<>();
        adapter = new DetailsAdapter(getChildFragmentManager());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        setToolbarVisibility(View.VISIBLE);

        if(!adapter.isConnected())
            adapter.connectListeners();

        if(viewPager.getAdapter() == null)
            viewPager.setAdapter(adapter);

        Bundle args = getArguments();
        if(args != null && args.containsKey("eventKey")){

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter.isConnected())
            adapter.disconnectListener();
    }

    public Query getScheduleQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("start");
    }

    public class DetailsAdapter
            extends FragmentStatePagerAdapter implements IEventsSubject {

        private boolean isConnected;

        public boolean isConnected() {
            return isConnected;
        }

        private EventsLoader eventsLoader;

        public DetailsAdapter(FragmentManager fm) {
            super(fm);
            eventsLoader = new EventsLoader(this);
        }

        public void connectListeners(){
            isConnected = true;
            getScheduleQuery().addChildEventListener(eventsLoader);
        }

        public void disconnectListener(){
            isConnected = false;
            getScheduleQuery().removeEventListener(eventsLoader);
        }

        @Override
        public Fragment getItem(int position) {
            ScheduleEvent event = events.get(position);
            return EventDetail.newInstance(event.getKey());
        }

        @Override
        public int getCount() {
            return events.size();
        }

        public List<ScheduleEvent> getEvents() {
            return events;
        }

        @Override
        public void addEvent(ScheduleEvent event) {

        }

        @Override
        public void changeEvent(ScheduleEvent event) {

        }

        @Override
        public void removeEvent(ScheduleEvent event) {

        }

        @Override
        public void addFavorite(String eventKey) {

        }

        @Override
        public void removeFavorite(String eventKey) {

        }
    }

}
