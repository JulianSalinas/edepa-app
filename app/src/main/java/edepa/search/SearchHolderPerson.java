package edepa.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import edepa.app.ActivityNavig;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.ColorGenerator;
import edepa.modelview.R;
import edepa.model.Person;
import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.people.PersonFragment;


public class SearchHolderPerson extends RecyclerView.ViewHolder {

    @BindView(R.id.event_decoration)
    View eventDecoration;

    @BindView(R.id.text_title)
    TextView personTitleView;

    @BindView(R.id.name_text)
    TextView personNameView;

    protected int accent;
    protected Person person;
    protected Context context;
    protected ColorGenerator colorGenerator;

    public SearchHolderPerson(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        colorGenerator = new ColorGenerator(context);
        accent = ContextCompat.getColor(context, R.color.app_accent);
    }

    public void bind(Person person){
        this.person = person;
        bindPersonName();
        bindPersonTitle();
        bindDecoration();
        itemView.setOnClickListener(v -> openPersonFragment());
    }

    public void bindPersonTitle(){
        String title = person.getPersonalTitle();
        personTitleView.setText(title);
    }

    public void bindPersonName(){
        String personName = person.getCompleteName();
        personNameView.setText(personName);
    }

    public void bindDecoration() {
        int color = colorGenerator.getColor(person.getCompleteName());
        eventDecoration.setBackgroundColor(color);
    }

    public void highlightText(String query) {
        if(query.length() >= 1) {
            highlightText(personNameView, query);
            highlightText(personTitleView, query);
        }
    }

    public void highlightText(TextView textView, String query){
        textView.setText(TextHighlighter.highlightText(query,
                textView.getText().toString(), accent));
    }

    public void openPersonFragment(){
        Context context = itemView.getContext();
        if (context instanceof ActivityNavig){
            ActivityNavig activity = (ActivityNavig) context;
            PersonFragment fragment = PersonFragment.newInstance(person);
            activity.setFragmentOnScreen(fragment, person.getKey());
        }
    }

}