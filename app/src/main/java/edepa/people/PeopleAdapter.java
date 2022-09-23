package edepa.people;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edepa.custom.RecyclerAdapter;
import edepa.model.Person;
import edepa.modelview.R;


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
