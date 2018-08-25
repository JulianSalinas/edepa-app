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


public class PeopleAdapter extends RecyclerAdapter {

    protected List<Person> people;

    @Override
    public int getItemCount() {
        return people.size();
    }

    public PeopleAdapter(List<Person> people) {
        this.people = people;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_item, parent, false);
        return new PersonHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int truePosition = holder.getAdapterPosition();
        Person person = people.get(truePosition);
        PersonHolder personHolder = (PersonHolder) holder;
        personHolder.bind(person);
    }

}
