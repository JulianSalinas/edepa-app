package edepa.people;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.graphics.Color;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import edepa.minilibs.TextHighlighter;
import edepa.minilibs.ColorGenerator;
import edepa.modelview.R;
import edepa.model.Person;
import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.minilibs.ColorConverter;

import com.mklimek.circleinitialsview.CircleInitialsView;


public class PersonHolder extends RecyclerView.ViewHolder {

    protected Person person;

    @BindView(R.id.text_title)
    TextView personTitleView;

    @BindView(R.id.name_text)
    TextView personNameView;

    @BindView(R.id.exhibitor_avatar_view)
    CircleInitialsView avatarView;

    public PersonHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Person person){
        this.person = person;
        bindPersonName();
        bindPersonTitle();
        bindPersonAvatar();
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
        ColorGenerator colorGenerator =
                new ColorGenerator(itemView.getContext());
        int color = colorGenerator.getColor(personName);
        return ColorConverter.lighten(color);
    }

    public void highlightText(String query) {
        if(query.length() >= 2) {
            int accentColor = ContextCompat
                    .getColor(itemView.getContext(), R.color.app_accent);
            personNameView.setText(TextHighlighter
                    .highlightText(query, person.getCompleteName(), accentColor));
            personTitleView.setText(TextHighlighter
                    .highlightText(query, person.getPersonalTitle(), accentColor));
        }
    }

}