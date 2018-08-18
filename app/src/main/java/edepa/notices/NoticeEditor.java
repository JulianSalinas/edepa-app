package edepa.notices;

import butterknife.BindView;
import butterknife.OnClick;

import edepa.minilibs.DialogFancy;
import edepa.app.MainFragment;

import edepa.minilibs.OnlineHelper;
import edepa.model.Notice;
import edepa.modelview.R;
import edepa.cloud.Cloud;

import android.support.design.widget.TextInputEditText;
import android.util.Log;


public class NoticeEditor extends MainFragment {

    @BindView(R.id.text_input_title)
    TextInputEditText textInputTitle;

    @BindView(R.id.text_input_content)
    TextInputEditText textInputContent;

    @Override
    public int getResource() {
        return R.layout.news_editor;
    }

    @OnClick(R.id.publish_button)
    public void onPublishButtonClick() {

        Cloud.getInstance()
                .getReference(Cloud.NEWS).push()
                .setValue(buildPublication())
                .addOnSuccessListener(v -> handleSuccess())
                .addOnFailureListener(this::handleError);

        if (!OnlineHelper.isOnline(getNavigationActivity()))
            showWarning();

    }

    public void handleSuccess() {
        if(getLayoutInflater() != null) {
             showSuccess();
             Log.i(toString(), "Publication success");
        }
    }

    public void handleError(Exception exception){
        if(getLayoutInflater() != null)
            showError();
        Log.e(toString(), exception.getMessage());
    }

    public void showWarning(){
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setInflater(getLayoutInflater())
                .setStatus(DialogFancy.WARNING)
                .setTitle(R.string.text_warning_publication)
                .setContent(R.string.text_warning_publication_content);
        builder .build().show();
    }

    public void showSuccess() {
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setInflater(getLayoutInflater())
                .setStatus(DialogFancy.SUCCESS)
                .setTitle(R.string.text_success_publication);
        builder .build().show();
    }

    public void showError() {
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setInflater(getLayoutInflater())
                .setStatus(DialogFancy.ERROR)
                .setTitle(R.string.text_error_publication);
        builder .build().show();
    }

    public Notice buildPublication() {
        return new Notice.Builder()
                .time(System.currentTimeMillis())
                .title(textInputTitle.getText().toString())
                .content(textInputContent.getText().toString()).build();
    }

}
