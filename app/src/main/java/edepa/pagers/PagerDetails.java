package edepa.pagers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.events.DetailFragment;
import edepa.model.Cloud;
import edepa.model.ScheduleEvent;
import edepa.modelview.R;
import edepa.activity.MainFragment;
import edepa.loaders.EventsLoader;
import edepa.interfaces.IEventsSubject;


public class PagerDetails extends MainFragment implements IEventsSubject{

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private DetailsAdapter adapter;

    private EventsLoader eventsLoader;

    private List<String> eventsKeys;

    @Override
    public int getResource() {
        return R.layout.events_details;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsKeys = new ArrayList<>();
        adapter = new DetailsAdapter();
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.GONE);

        adapter.connectListeners();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        Bundle args = getArguments();
        if(args != null && args.containsKey("eventKey")){
            // current
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setToolbarVisibility(View.VISIBLE);
        adapter.disconnectListener();
    }

    public Query getScheduleQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("start");
    }

    @Override
    public void addEvent(ScheduleEvent event) {
        String eventKey = event.getKey();
        if(!eventsKeys.contains(eventKey)){
            eventsKeys.add(eventKey);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void changeEvent(ScheduleEvent event) {
        String eventKey = event.getKey();
        int index = eventsKeys.indexOf(eventKey);
        if(index != -1){
            eventsKeys.set(index, eventKey);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removeEvent(ScheduleEvent event) {
        String eventKey = event.getKey();
        int index = eventsKeys.indexOf(eventKey);
        if(index != -1){
            eventsKeys.remove(index);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addFavorite(String eventKey) {
        // No requerido
    }

    @Override
    public void removeFavorite(String eventKey) {
        // No requerido
    }

    public class DetailsAdapter extends FragmentStatePagerAdapter {

        @Override
        public int getCount() {
            return eventsKeys.size();
        }

        public DetailsAdapter() {
            super(PagerDetails.this.getChildFragmentManager());
            eventsLoader = new EventsLoader(PagerDetails.this);
        }

        public void connectListeners(){
            getScheduleQuery().addChildEventListener(eventsLoader);
        }

        public void disconnectListener(){
            getScheduleQuery().removeEventListener(eventsLoader);
        }

        @Override
        public Fragment getItem(int position) {
            ScheduleEvent event  = new ScheduleEvent();
            event.setKey(eventsKeys.get(position));
            return DetailFragment.newInstance(event);
        }

    }

}
