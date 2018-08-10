package edepa.people;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edepa.model.Person;
import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;

import java.util.List;


public class PeopleAdapter extends RecyclerAdapter {

    private List<Person> people;

    @Override
    public int getItemCount() {
        return people.size();
    }

    public PeopleAdapter(PeopleFragment fragment) {
        this.people = fragment.getPeople();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PersonViewHolder) holder).bind(people.get(holder.getAdapterPosition()));
    }

}
