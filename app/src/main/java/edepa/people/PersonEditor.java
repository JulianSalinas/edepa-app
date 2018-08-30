package edepa.people;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.TextView;

import com.mklimek.circleinitialsview.CircleInitialsView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import edepa.app.CustomFragment;
import edepa.cloud.Cloud;
import edepa.minilibs.ColorConverter;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.DialogFancy;
import edepa.model.Person;
import edepa.modelview.R;

public class PersonEditor extends CustomFragment {

    @BindView(R.id.text_input_complete_name)
    TextView inputCompleteName;

    @BindView(R.id.text_input_title)
    TextView inputTitle;

    @BindView(R.id.text_title)
    TextView textTitle;

    @BindView(R.id.name_text)
    TextView nameText;

    @BindView(R.id.text_input_about)
    TextView inputAbout;

    @BindView(R.id.exhibitor_avatar_view)
    CircleInitialsView avatarView;

    @OnTextChanged(R.id.text_input_complete_name)
    public void changePersonName(Editable editable){
        String personName = editable.toString();
        avatarView.setText(personName);
        avatarView.setTextColor(Color.WHITE);
        ColorGenerator colorGenerator = new ColorGenerator(getContext());
        int color = colorGenerator.getColor(personName);
        color = ColorConverter.lighten(color);
        avatarView.setBackgroundColor(color);
        nameText.setText(personName);
    }

    @OnTextChanged(R.id.text_input_title)
    public void changePersonTitle(Editable editable){
        textTitle.setText(editable);
    }

    @Override
    public int getResource() {
        return R.layout.person_editor;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarText(R.string.text_add_person);
    }

    @OnClick(R.id.add_person_button)
    public void addPerson(){
        Cloud.getInstance()
                .getReference(Cloud.PEOPLE).push()
                .setValue(buildPerson())
                .addOnSuccessListener(v -> handleSuccess())
                .addOnFailureListener(this::handleError);
    }

    private void handleError(Exception e) {
        showError();
        Log.e(toString(), e.getMessage());
    }

    private void handleSuccess() {
        showSuccess();
        Log.i(toString(), "person added");
    }

    private Person buildPerson() {
        return new Person.Builder()
                .completeName(inputCompleteName.getText().toString())
                .personalTitle(inputTitle.getText().toString())
                .about(inputAbout.getText().toString()).build();
    }

    public void showSuccess() {
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setContext(getContext())
                .setStatus(DialogFancy.SUCCESS)
                .setTitle(R.string.text_success_add_person);
        builder .build().show();
    }

    public void showError() {
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setContext(getContext())
                .setStatus(DialogFancy.ERROR)
                .setTitle(R.string.text_error_add_person);
        builder .build().show();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
