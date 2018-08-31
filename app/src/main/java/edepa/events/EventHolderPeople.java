package edepa.events;


import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edepa.model.Event;
import edepa.model.Person;
import java.util.ArrayList;


public class EventHolderPeople extends RecyclerView.ViewHolder implements ValueEventListener {

    private Event event;

    public EventHolderPeople(View itemView) {
        super(itemView);
    }

    public void bind(Event event){
        this.event = event;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
//        Person person = dataSnapshot.getValue(Person.class);
//        if (person != null & !people.contains(person)) {
//            person.setKey(dataSnapshot.getKey());
//            person.setEventsList(new ArrayList<>());
//            people.add(person);
//            addPersonView(person);
//        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

//    private void bindPeople(){
//
//        boolean available = Preferences.getBooleanPreference(
//                getNavigationActivity(), Preferences.PEOPLE_AVAILABLE_KEY);
//
//        available = available &&
//                event.getPeople() != null &&
//                event.getPeople().size() > 0;
//
//        if(!available) {
//            peopleFront.setVisibility(GONE);
//            peopleFlipView.setVisibility(GONE);
//            peopleContainer.setVisibility(GONE);
//        }
//
//        else for (String personKey : event.getPeople().keySet()) {
//            Cloud.getInstance()
//                    .getReference(Cloud.PEOPLE)
//                    .child(personKey)
//                    .addListenerForSingleValueEvent(personListener);
//        }
//
//        peopleFlipView.setFlipOnTouch(false);
//        peopleFlipView.setOnClickListener(v -> peopleFlipView.flipTheView());
//
//    }
//
//    private void addPersonView(Person person) {
//        @SuppressLint("InflateParams")
//        View view = getLayoutInflater().inflate(R.layout.people_item, null);
//        View line = getLayoutInflater().inflate(R.layout.custom_line, null);
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
//        Resources res = getResources();
//        params.setMarginStart((int) res.getDimension(R.dimen.size_default));
//        params.setMarginEnd((int) res.getDimension(R.dimen.size_default));
//        return params;
//    }

}
