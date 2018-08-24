package edepa.search;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.app.MainFragment;
import edepa.cloud.CloudEvents;
import edepa.cloud.CloudPeople;
import edepa.minilibs.SmoothLayout;
import edepa.model.Event;
import edepa.model.Person;
import edepa.modelview.R;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SearchFragment extends MainFragment
        implements CloudPeople.Callbacks, CloudEvents.Callbacks {

    @BindView(R.id.people_section_title)
    TextView peopleSectionTitle;

    @BindView(R.id.events_section_title)
    TextView eventsSectionTitle;

    @BindView(R.id.people_recycler_view)
    RecyclerView peopleRecycler;

    @BindView(R.id.events_recycler_view)
    RecyclerView eventsRecycler;

    private CloudPeople cloudPeople;
    private CloudEvents cloudEvents;

    private PeopleSearch peopleSearch;
    private EventsSearch eventsSearch;

    private List<Person> people;
    private List<Event> events;

    private MaterialSearchView searchView;

    @Override
    public int getResource() {
        return R.layout.search_panel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.people = new ArrayList<>();
        this.events = new ArrayList<>();

        this.cloudPeople = new CloudPeople();
        this.cloudEvents = new CloudEvents();

        this.peopleSearch = new PeopleSearch(people);
        this.eventsSearch = new EventsSearch(getContext(), events);

        cloudPeople.setCallbacks(this);
        cloudEvents.setCallbacks(this);

        cloudPeople.connect();
        cloudEvents.connect();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.searchView = getNavigationActivity().getSearchView();

        this.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                peopleSearch.getFilter().filter(query);
                eventsSearch.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                peopleSearch.getFilter().filter(newText);
                eventsSearch.getFilter().filter(newText);
                return false;
            }
        });

        setupRecycler(peopleRecycler, peopleSearch);
        setupRecycler(eventsRecycler, eventsSearch);

    }

    public void setupRecycler(RecyclerView recycler, RecyclerView.Adapter adapter){
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new SmoothLayout(getActivity()));
        recycler.addItemDecoration(getDecoration());
        recycler.setNestedScrollingEnabled(false);
    }

    public DividerItemDecoration getDecoration(){
        DividerItemDecoration decoration =
                new DividerItemDecoration(getNavigationActivity(), VERTICAL);
        decoration.setDrawable(
                getResources().getDrawable(R.drawable.util_decorator));
        return decoration;
    }

    @Override
    public void addPerson(Person person) {
        if(!people.contains(person)) {
            people.add(person);
            peopleSearch.notifyItemInserted(people.size() - 1);
        }
    }

    @Override
    public void changePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.set(index, person);
            peopleSearch.notifyItemChanged(index);
        }
    }

    @Override
    public void removePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.remove(person);
            peopleSearch.notifyItemRemoved(index);
        }
    }

    @Override
    public void addEvent(Event event) {
        int index = events.indexOf(event);
        if (index == -1){
            events.add(event);
            eventsSearch.notifyItemInserted(events.size() - 1);
        }
    }

    @Override
    public void changeEvent(Event event) {
        int index = events.indexOf(event);
        if (index != -1){
            events.set(index, event);
            eventsSearch.notifyItemChanged(index);
        }
    }

    @Override
    public void removeEvent(Event event) {
        int index = events.indexOf(event);
        if (index != -1){
            events.remove(index);
            eventsSearch.notifyItemRemoved(index);
        }
    }

}
