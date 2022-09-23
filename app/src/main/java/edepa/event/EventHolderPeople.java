package edepa.event;


import static android.view.View.GONE;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.cloud.Cloud;
import edepa.cloud.CloudPeople;
import edepa.minilibs.SmoothLayout;
import edepa.model.Event;
import edepa.model.Person;
import edepa.model.Preferences;
import edepa.modelview.R;
import edepa.people.PeopleAdapter;


public class EventHolderPeople extends RecyclerView.ViewHolder implements ValueEventListener {

    @BindView(R.id.people_recycler)
    RecyclerView peopleRecycler;

    PeopleAdapter peopleAdapter;

    private Context context;
    private List<Person> people;

    public EventHolderPeople(View itemView) {
        super(itemView);
        people = new ArrayList<>();
        context = itemView.getContext();
    }

    public void bind(Event event){

        this.people.clear();
        ButterKnife.bind(this, itemView);

        peopleAdapter = new PeopleAdapter(people);
        peopleRecycler.setLayoutManager(new SmoothLayout(itemView.getContext()));
        peopleRecycler.setAdapter(peopleAdapter);

        boolean available = Preferences.getBooleanPreference(context, Cloud.PEOPLE);

        if (!available || event.getPeople() == null || event.getPeople().size() <= 0){
            itemView.setVisibility(GONE);
        }
        else {
            itemView.setVisibility(View.VISIBLE);
            for (String personKey : event.getPeople().keySet()){
                Cloud.getInstance().getReference(Cloud.PEOPLE)
                        .child(personKey).addValueEventListener(this);
            }
        }

    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Person person = dataSnapshot.getValue(Person.class);
        if (person != null & !people.contains(person)) {
            person.setKey(dataSnapshot.getKey());
            person.setEventsList(new ArrayList<>());
            CloudPeople.addEventsToPerson(person);
            people.add(person);
            peopleAdapter.notifyItemInserted(people.size() - 1);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

}
