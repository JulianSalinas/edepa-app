package edepa.search;

import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import edepa.modelview.R;
import butterknife.BindView;
import edepa.cloud.CloudPeople;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


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

    public void search(String query){
        peopleAdapter.getFilter().filter(query);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cloudPeople = new CloudPeople();
        peopleAdapter = new SearchAdapterPeople();
        cloudPeople.setCallbacks(peopleAdapter);
        cloudPeople.connect();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecycler(peopleRecycler, peopleAdapter);
    }

    @Override
    public void onDestroy() {
        cloudPeople.disconnect();
        super.onDestroy();
    }

}
