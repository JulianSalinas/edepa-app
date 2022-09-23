package edepa.search;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import edepa.cloud.CloudEvents;
import edepa.model.EventType;
import edepa.modelview.R;


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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventType typeFilter = getTypeFilterFromArguments();
        cloudEvents = new CloudEvents();
        eventsAdapter = new SearchAdapterEvents(typeFilter);
        cloudEvents.setCallbacks(eventsAdapter);
        cloudEvents.connect();
        setupRecycler(eventsRecycler, eventsAdapter);
    }

    @Override
    public void onDestroyView() {
        cloudEvents.disconnect();
        super.onDestroyView();
    }

    public void search(String query){
        eventsAdapter.getFilter().filter(query);
    }

    @Override
    public void clear() {
        eventsAdapter.getFilter().filter("");
    }

}
