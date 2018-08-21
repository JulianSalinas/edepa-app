package edepa.search;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.app.MainFragment;
import edepa.cloud.CloudPeople;
import edepa.minilibs.SmoothLayout;
import edepa.cloud.Cloud;
import edepa.model.Person;
import edepa.modelview.R;
import edepa.people.PeopleAdapter;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SearchFragment extends MainFragment implements CloudPeople.Callbacks {

    @BindView(R.id.people_section_title)
    TextView peopleSectionTitle;

    @BindView(R.id.events_section_title)
    TextView eventsSectionTitle;

    @BindView(R.id.people_recycler_view)
    RecyclerView peopleRecyclerView;

    @BindView(R.id.events_recycler_view)
    RecyclerView eventsRecyclerView;

    private CloudPeople cloudPeople;
    private PeopleAdapter peopleAdapter;

    private List<Person> people;

    private MaterialSearchView searchView;

    @Override
    public int getResource() {
        return R.layout.search_panel;
    }

    public Query getPeopleReference(){
        return Cloud.getInstance()
                .getReference(Cloud.PEOPLE)
                .orderByChild("completeName");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.people = new ArrayList<>();
        this.cloudPeople = new CloudPeople();
        this.peopleAdapter = new PeopleAdapter(people);
        cloudPeople.setCallbacks(this);
        cloudPeople.connect();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.searchView = getNavigationActivity().getSearchView();

        this.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                peopleAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                peopleAdapter.getFilter().filter(newText);
                return false;
            }
        });

        DividerItemDecoration decoration =
                new DividerItemDecoration(getNavigationActivity(), VERTICAL);

        decoration.setDrawable(
                getResources().getDrawable(R.drawable.util_decorator));

        peopleRecyclerView.setAdapter(peopleAdapter);
        peopleRecyclerView.setHasFixedSize(true);
        peopleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        peopleRecyclerView.setLayoutManager(new SmoothLayout(getActivity()));
        peopleRecyclerView.addItemDecoration(decoration);

        eventsRecyclerView.setAdapter(peopleAdapter);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsRecyclerView.setLayoutManager(new SmoothLayout(getActivity()));
        eventsRecyclerView.addItemDecoration(decoration);

        eventsRecyclerView.setNestedScrollingEnabled(false);
        peopleRecyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void addPerson(Person person) {
        if(!people.contains(person)) {
            people.add(person);
            peopleAdapter.notifyItemInserted(people.size() - 1);
        }
    }

    @Override
    public void changePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.set(index, person);
            peopleAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void removePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.remove(person);
            peopleAdapter.notifyItemRemoved(index);
        }
    }

}
