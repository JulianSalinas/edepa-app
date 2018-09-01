package edepa.events;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.cloud.Cloud;
import edepa.cloud.CloudEvents;
import edepa.cloud.CloudPeople;
import edepa.minilibs.SmoothLayout;
import edepa.model.Event;
import edepa.model.Person;
import edepa.model.Preferences;
import edepa.modelview.R;
import edepa.people.PeopleAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class EventHolderPeople extends RecyclerView.ViewHolder implements ValueEventListener {

    @BindView(R.id.expansionLayout)
    ExpansionLayout expansionLayout;

    @BindView(R.id.people_recycler)
    RecyclerView peopleRecycler;

    PeopleAdapter peopleAdapter;

    private Event event;
    private Context context;
    private List<Person> people;

    public EventHolderPeople(View itemView) {
        super(itemView);
        people = new ArrayList<>();
        context = itemView.getContext();
    }

    public void bind(Event event){
        this.event = event;
        this.people.clear();
        ButterKnife.bind(this, itemView);

        peopleAdapter = new PeopleAdapter(people);
        peopleRecycler.setLayoutManager(new SmoothLayout(itemView.getContext()));
        peopleRecycler.setAdapter(peopleAdapter);

        expansionLayout.addListener((expansionLayout, expanded) -> {
            Log.i("Expansion", String.valueOf(expanded));
        });

        boolean available = Preferences.getBooleanPreference(context, Preferences.PEOPLE_AVAILABLE_KEY);

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

//    private void addPersonView(Person person) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        @SuppressLint("InflateParams")
//        View view = inflater.inflate(R.layout.people_item, null);
//        View line = inflater.inflate(R.layout.custom_line, null);
//        new PersonHolder(view).bind(person);
//        peopleContainer.addView(view, getPersonLayoutParams());
//        peopleContainer.addView(line, getLineLayoutParams());
//    }
//
//    private LinearLayout.LayoutParams getLineLayoutParams() {
//        return new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, 1);
//    }
//
//    private LinearLayout.LayoutParams getPersonLayoutParams() {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        Resources res = itemView.getResources();
//        params.setMarginStart((int) res.getDimension(R.dimen.size_default));
//        params.setMarginEnd((int) res.getDimension(R.dimen.size_default));
//        return params;
//    }

}
