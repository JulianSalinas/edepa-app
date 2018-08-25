package edepa.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.cloud.CloudEvents;
import edepa.model.Event;
import edepa.model.EventType;
import edepa.modelview.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


public class SearchByEventsFragment
        extends SearchBasicFragment implements MaterialSearchView.OnQueryTextListener {

    public static final String FILTER_KEY = "filter_key";

    @BindView(R.id.events_text)
    TextView eventsText;

    @BindView(R.id.events_recycler)
    RecyclerView eventsRecycler;

    private CloudEvents cloudEvents;
    private SearchAdapterEvents eventsAdapter;

    @Override
    public int getResource() {
        return R.layout.search_by_events;
    }

    public EventType getTypeFilterFromArguments(){
        Bundle args = getArguments();
        boolean containsFilter = args != null && args.containsKey(FILTER_KEY);
        String filterText = args == null ? null : args.getString(FILTER_KEY);
        return containsFilter ? EventType.valueOf(filterText) : null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventType typeFilter = getTypeFilterFromArguments();
        cloudEvents = new CloudEvents();
        eventsAdapter = new SearchAdapterEvents(typeFilter);
        cloudEvents.setCallbacks(eventsAdapter);
        cloudEvents.connect();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecycler(eventsRecycler, eventsAdapter);
    }

    @Override
    public void onDestroy() {
        cloudEvents.disconnect();
        super.onDestroy();
    }

    public void search(String query){
        eventsAdapter.getFilter().filter(query);
    }

}
