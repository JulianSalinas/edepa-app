package edepa.search;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.mklimek.circleinitialsview.CircleInitialsView;

import edepa.app.ActivityNavig;
import edepa.minilibs.ColorConverter;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.ColorGenerator;
import edepa.modelview.R;
import edepa.model.Person;
import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.people.PersonFragment;
import edepa.people.PersonHolder;


public class SearchHolderPerson extends PersonHolder {

    @BindView(R.id.text_title)
    public TextView personTitleView;

    @BindView(R.id.name_text)
    public TextView personNameView;

    @BindView(R.id.exhibitor_avatar_view)
    public CircleInitialsView avatarView;

    private int accent;
    private Person person;
    private ColorGenerator colorGenerator;

    public SearchHolderPerson(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        Context context = itemView.getContext();
        colorGenerator = new ColorGenerator(context);
        accent = ContextCompat.getColor(context, R.color.app_accent);
    }

    public void bind(Person person){
        this.person = person;
        bindPersonName();
        bindPersonTitle();
        bindPersonAvatar();
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

    public void bindPersonAvatar(){
        String personName = person.getCompleteName();
        avatarView.setText(personName);
        avatarView.setTextColor(Color.WHITE);
        int color = getAvatarColor();
        avatarView.setBackgroundColor(color);
    }

    private int getAvatarColor(){
        String personName = person.getCompleteName();
        int color = colorGenerator.getColor(personName);
        return ColorConverter.lighten(color);
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