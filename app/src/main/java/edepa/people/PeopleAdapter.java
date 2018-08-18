package edepa.people;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import edepa.model.Person;
import edepa.minilibs.RegexSearcher;
import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;


public class PeopleAdapter extends RecyclerAdapter implements Filterable {

    private List<Person> people;

    private List<Person> peopleFiltered;

    private String query;

    @Override
    public int getItemCount() {
        return peopleFiltered.size();
    }

    public PeopleAdapter(List<Person> people) {
        this.query = "";
        this.people = people;
        this.peopleFiltered = people;
    }

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_item, parent, false);
        return new PersonHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int truePosition = holder.getAdapterPosition();
        Person person = peopleFiltered.get(truePosition);
        PersonHolder personHolder = (PersonHolder) holder;
        personHolder.bind(person);
        personHolder.highlightText(query);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                query = constraint.toString();
                peopleFiltered = new ArrayList<>();
                if(query.isEmpty()) peopleFiltered.addAll(people);

                else {
                    for (Person person: people){
                        ArrayList<MatchResult> results = RegexSearcher
                                .autoSearch(query, person.getCompleteName());
                        results.addAll(RegexSearcher
                                .autoSearch(query, person.getPersonalTitle()));
                        if(results.size() > 0) peopleFiltered.add(person);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = peopleFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                peopleFiltered = (ArrayList<Person>) results.values;
                notifyDataSetChanged();
            }

        };

    }

}
