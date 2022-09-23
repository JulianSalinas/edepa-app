package edepa.search;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import edepa.cloud.CloudPeople;
import edepa.modelview.R;


public class SearchByPeopleFragment extends SearchBasicFragment
        implements MaterialSearchView.OnQueryTextListener {

    public static final String PEOPLE_FILTER = "people_filter";

    @BindView(R.id.people_text)
    TextView peopleText;

    @BindView(R.id.people_recycler)
    RecyclerView peopleRecycler;

    private CloudPeople cloudPeople;
    private SearchAdapterPeople peopleAdapter;

    @Override
    public int getResource() {
        return R.layout.search_by_people;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cloudPeople = new CloudPeople();
        peopleAdapter = new SearchAdapterPeople();
        cloudPeople.setCallbacks(peopleAdapter);
        cloudPeople.connect();
        setupRecycler(peopleRecycler, peopleAdapter);
    }

    @Override
    public void onDestroyView() {
        cloudPeople.disconnect();
        super.onDestroyView();
    }

    public void search(String query){
        peopleAdapter.getFilter().filter(query);
    }

    @Override
    public void clear() {
        peopleAdapter.getFilter().filter("");
    }

}
