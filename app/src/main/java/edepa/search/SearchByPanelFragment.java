package edepa.search;

import android.os.Bundle;
import android.view.View;
import android.support.v4.app.Fragment;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import edepa.app.NavigationActivity;
import edepa.events.EventsOngoing;
import edepa.modelview.R;
import butterknife.OnClick;
import edepa.model.EventType;
import edepa.app.MainFragment;


public class SearchByPanelFragment extends MainFragment
        implements MaterialSearchView.OnQueryTextListener {

    @Override
    public int getResource() {
        return R.layout.search_by_panel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarText(R.string.text_search_by);
        setToolbarVisibility(View.VISIBLE);
        getNavigationActivity().getSearchView().setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // openSearchWithFilter(new Bundle());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        openSearchWithFilter(new Bundle());
        return false;
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
        Fragment fragment = getFragmentBasedOnFilter(filter);
        fragment.setArguments(params);
        // getNavigationActivity().getSupportFragmentManager().popBackStack();
        // setFragmentOnScreen(fragment, filter);

        NavigationActivity activity = getNavigationActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }

    public Fragment getFragmentBasedOnFilter(String filter){
        String peopleFilter = SearchByPeopleFragment.PEOPLE_FILTER;
        return filter != null && filter.equals(peopleFilter) ?
                new SearchByPeopleFragment() :
                new SearchByEventsFragment();
    }

}
