package edepa.people;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mklimek.circleinitialsview.CircleInitialsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.misc.ColorConverter;
import edepa.misc.MaterialGenerator;
import edepa.model.Person;
import edepa.modelview.R;

public class PersonHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.exhibitor_avatar_view)
    CircleInitialsView avatarView;

    @BindView(R.id.name_text_view)
    TextView personNameView;

    @BindView(R.id.text_title)
    TextView personTitleView;

    protected Person person;

    public PersonHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Person person){

        this.person = person;

        String personName = person.getCompleteName();
        avatarView.setText(personName);
        avatarView.setTextColor(Color.WHITE);

        MaterialGenerator materialGenerator =
                new MaterialGenerator(itemView.getContext());

        int color = materialGenerator.getColor(personName);
        color = ColorConverter.lighten(color);
        avatarView.setBackgroundColor(color);

        personNameView.setText(personName);
        personTitleView.setText(person.getPersonalTitle());

    }

}