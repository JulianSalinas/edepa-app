package edepa.people;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import butterknife.BindView;
import edepa.model.Cloud;
import edepa.model.Person;

import edepa.modelview.R;
import edepa.custom.SmoothLayout;
import edepa.loaders.PeopleLoader;
import edepa.activity.MainFragment;
import edepa.interfaces.IPeopleSubject;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class PeopleFragment extends MainFragment implements IPeopleSubject {

    @BindView(R.id.people_recycler_view)
    RecyclerView peopleRV;

    private PeopleAdapter adapter;

    private ArrayList<Person> people;

    public ArrayList<Person> getPeople() {
        return people;
    }

    private PeopleLoader peopleLoader;

    @Override
    public int getResource() {
        return R.layout.people_view;
    }

    public Query getPeopleQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.PEOPLE)
                .orderByChild("completeName");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        people = new ArrayList<>();
        adapter = new PeopleAdapter(this);
        peopleLoader = new PeopleLoader(this);
        getPeopleQuery().addChildEventListener(peopleLoader);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPeopleQuery().removeEventListener(peopleLoader);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setToolbarText(R.string.text_people);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);

        peopleRV.setAdapter(adapter);
        peopleRV.setHasFixedSize(true);
        peopleRV.setItemAnimator(new DefaultItemAnimator());
        peopleRV.setLayoutManager(new SmoothLayout(getActivity()));

        DividerItemDecoration decoration =
                new DividerItemDecoration(getMainActivity(), VERTICAL);

        decoration.setDrawable(
                getResources().getDrawable(R.drawable.util_decorator));

        peopleRV.addItemDecoration(decoration);

    }

    @Override
    public void addPerson(Person person) {
        if(!people.contains(person)) {
             people.add(person);
             adapter.notifyItemInserted(people.size() - 1);
        }
    }

    @Override
    public void changePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.set(index, person);
            adapter.notifyItemChanged(index);
        }
    }

    @Override
    public void removePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.remove(person);
            adapter.notifyItemRemoved(index);
        }
    }

}
