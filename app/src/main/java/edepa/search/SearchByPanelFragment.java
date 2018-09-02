package edepa.search;

import android.os.Bundle;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import edepa.app.NavigationActivity;
import edepa.modelview.R;
import butterknife.OnClick;
import edepa.model.EventType;
import edepa.custom.CustomFragment;


public class SearchByPanelFragment extends CustomFragment
        implements MaterialSearchView.OnQueryTextListener {

    private MaterialSearchView searchView;

    @Override
    public int getResource() {
        return R.layout.search_by_panel;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView = getNavigationActivity().getSearchView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchView.setOnQueryTextListener(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        openSearchWithFilter(new Bundle());
        return true;
    }

    @OnClick(R.id.conference_filter)
    public void applyConferenceFilter(){
        openSearchWithFilter(EventType.CONFERENCIA);
    }

    @OnClick(R.id.presentation_filter)
    public void applyPresentationFilter(){
        openSearchWithFilter(EventType.PONENCIA);
    }

    @OnClick(R.id.atelier_filter)
    public void applyAtelierFilter(){
        openSearchWithFilter(EventType.TALLER);
    }

    @OnClick(R.id.fair_filter)
    public void applyFairFilter() {
        openSearchWithFilter(EventType.FERIA_EDEPA);
    }

    @OnClick(R.id.people_filter)
    public void applyPeopleFilter() {
        String filter = SearchByPeopleFragment.PEOPLE_FILTER;
        Bundle params = createParamsForFiltering(filter);
        openSearchWithFilter(params);
    }

    public Bundle createParamsForFiltering(String filter) {
        Bundle params = new Bundle();
        params.putString(SearchByEventsFragment.FILTER_KEY, filter);
        return params;
    }

    public void openSearchWithFilter(EventType filter){
        Bundle params = createParamsForFiltering(filter.toString());
        openSearchWithFilter(params);
    }

    public void openSearchWithFilter(Bundle params){

        String filter = params.getString(SearchByEventsFragment.FILTER_KEY);
        String tag = getTagBasedOnFilter(filter);

        NavigationActivity activity = getNavigationActivity();

        if (tag.equals("SEARCH_BY_PEOPLE")) {
            activity.openSearchByPeople();
        }
        else if (tag.equals("SEARCH_BY_EVENTS")) {
            activity.openSearchByEvents(params);
        }

    }

    public String getTagBasedOnFilter(String filter){
        String peopleFilter = SearchByPeopleFragment.PEOPLE_FILTER;
        return filter != null && filter.equals(peopleFilter) ?
                "SEARCH_BY_PEOPLE" : "SEARCH_BY_EVENTS";
    }

}
