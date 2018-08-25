package edepa.search;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import edepa.cloud.CloudPeople;
import edepa.minilibs.RegexSearcher;
import edepa.model.Person;
import edepa.modelview.R;
import edepa.people.PeopleAdapter;

public class SearchAdapterPeople
        extends PeopleAdapter implements Filterable, CloudPeople.Callbacks {

    private String query;
    private List<Person> peopleFiltered;

    @Override
    public int getItemCount() {
        return peopleFiltered.size();
    }

    public SearchAdapterPeople() {
        super(new ArrayList<>());
        this.query = "";
        this.peopleFiltered = people;
    }

    @Override
    public void addPerson(Person person) {
        if(!people.contains(person)) {
            people.add(person);
            notifyItemInserted(people.size() - 1);
        }
    }

    @Override
    public void changePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.set(index, person);
            notifyItemChanged(index);
        }
    }

    @Override
    public void removePerson(Person person) {
        int index = people.indexOf(person);
        if (index != -1) {
            people.remove(person);
            notifyItemRemoved(index);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolderPerson(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_person, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int truePosition = holder.getAdapterPosition();
        Person person = peopleFiltered.get(truePosition);
        SearchHolderPerson personHolder = (SearchHolderPerson) holder;
        personHolder.bind(person);
        personHolder.highlightText(query);
    }

    public boolean matchPerson(Person person){
        boolean match = RegexSearcher.match(query, person.getCompleteName());
        return match || RegexSearcher.match(query, person.getPersonalTitle());
    }

    public void addAllBasedOnQuery(String query){
        peopleFiltered = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            peopleFiltered.addAll(people);
        }
        else for (Person person: people) {
            if (matchPerson(person)) peopleFiltered.add(person);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            query = constraint.toString();
            addAllBasedOnQuery(query);
            FilterResults filterResults = new FilterResults();
            filterResults.values = peopleFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            peopleFiltered = (ArrayList<Person>) results.values;
            notifyDataSetChanged();
        }};

    }

}
