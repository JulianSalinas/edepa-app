package imagisoft.modelview;

import android.view.View;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


public class EventsFragmentSearch extends EventsFragmentOngoing
        implements MaterialSearchView.OnQueryTextListener {

    public static EventsFragmentSearch newInstance() {
        return new EventsFragmentSearch();
    }

    @Override
    public void setupActivityView() {
        super.setupActivityView();
        setTabLayoutVisibility(View.GONE);
    }

    @Override
    public void setupResource() {
        this.resource = R.layout.schedule_view_search;
    }

    protected void setupAdapter() {
        if(eventsVA == null)
            eventsVA = new EventsAdapterSearch(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return ((EventsAdapterSearch) eventsVA).onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return ((EventsAdapterSearch) eventsVA).onQueryTextChange(newText);
    }

}
