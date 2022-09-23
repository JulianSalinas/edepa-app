package edepa.people;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.CloudAdmin;
import edepa.cloud.CloudPeople;
import edepa.custom.CustomFragment;
import edepa.minilibs.SmoothLayout;
import edepa.model.Person;
import edepa.modelview.R;


public class PeopleFragment extends CustomFragment
        implements CloudPeople.Callbacks, CloudAdmin.AdminPermissionListener {

    @BindView(R.id.people_recycler)
    RecyclerView peopleRecycler;

    @BindView(R.id.add_person_button)
    FloatingActionButton addPersonButton;

    private PeopleAdapter peopleAdapter;

    private ArrayList<Person> people;

    public ArrayList<Person> getPeople() {
        return people;
    }

    private CloudAdmin cloudAdmin;

    private CloudPeople cloudPeople;

    @Override
    public int getResource() {
        return R.layout.people_screen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        people = new ArrayList<>();
        peopleAdapter = new PeopleAdapter(people);

        cloudPeople = new CloudPeople();
        cloudPeople.setCallbacks(this);
        cloudPeople.connect();

        cloudAdmin = new CloudAdmin();
        cloudAdmin.setAdminPermissionListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cloudPeople.disconnect();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setToolbarText(R.string.text_exhibitors);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);

        peopleRecycler.setAdapter(peopleAdapter);
        peopleRecycler.setHasFixedSize(true);
        peopleRecycler.setItemAnimator(new DefaultItemAnimator());
        peopleRecycler.setLayoutManager(new SmoothLayout(getActivity()));

        addPersonButton.setVisibility(View.GONE);
        // cloudAdmin.requestAdminPermission();

        DividerItemDecoration decoration =
                new DividerItemDecoration(getNavigationActivity(), VERTICAL);

        decoration.setDrawable(
                getResources().getDrawable(R.drawable.util_decorator));

        peopleRecycler.addItemDecoration(decoration);
    }

    @OnClick(R.id.add_person_button)
    public void openPersonEditor(){
        String tag = "PERSON_EDITOR";
        PersonEditor frag = (PersonEditor) getNavigationActivity()
                .getSupportFragmentManager().findFragmentByTag(tag);
        setFragmentOnScreen(frag != null ? frag: new PersonEditor(), tag);
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

    @Override
    public void onPermissionGranted() {
        addPersonButton.setVisibility(View.GONE);

        peopleRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addPersonButton.getVisibility() == View.VISIBLE) {
                    addPersonButton.hide();
                }
                else if (dy < 0 && addPersonButton.getVisibility() != View.VISIBLE) {
                    addPersonButton.show();
                }
            }
        });

    }

    @Override
    public void onPermissionDenied() {
        addPersonButton.setVisibility(View.GONE);
    }

}
