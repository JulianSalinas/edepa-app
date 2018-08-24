package edepa.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

import edepa.custom.RecyclerAdapter;
import edepa.minilibs.RegexSearcher;
import edepa.model.Person;
import edepa.modelview.R;
import edepa.people.PeopleAdapter;
import edepa.people.PersonHolder;


public class PeopleSearch extends PeopleAdapter implements Filterable {

    private String query;
    private List<Person> peopleFiltered;

    @Override
    public int getItemCount() {
        return peopleFiltered.size();
    }

    public PeopleSearch(List<Person> people) {
        super(people);
        this.query = "";
        this.peopleFiltered = people;
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
