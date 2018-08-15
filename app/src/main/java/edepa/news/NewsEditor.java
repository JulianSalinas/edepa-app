package edepa.news;

import butterknife.BindView;
import butterknife.OnClick;

import edepa.custom.DialogCustom;
import edepa.activity.MainFragment;

import edepa.misc.OnlineHelper;
import edepa.modelview.R;
import edepa.model.Cloud;
import edepa.model.NewsItem;

import android.support.design.widget.TextInputEditText;
import android.util.Log;


public class NewsEditor extends MainFragment {

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

        if (!OnlineHelper.isOnline(getMainActivity()))
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
        DialogCustom.Builder builder = new DialogCustom.Builder();
        builder .setInflater(getLayoutInflater())
                .setStatus(DialogCustom.WARNING)
                .setTitle(R.string.text_warning_publication)
                .setContent(R.string.text_warning_publication_content);
        builder .build().show();
    }

    public void showSuccess() {
        DialogCustom.Builder builder = new DialogCustom.Builder();
        builder .setInflater(getLayoutInflater())
                .setStatus(DialogCustom.SUCCESS)
                .setTitle(R.string.text_success_publication);
        builder .build().show();
    }

    public void showError() {
        DialogCustom.Builder builder = new DialogCustom.Builder();
        builder .setInflater(getLayoutInflater())
                .setStatus(DialogCustom.ERROR)
                .setTitle(R.string.text_error_publication);
        builder .build().show();
    }

    public NewsItem buildPublication() {
        NewsItem publication = new NewsItem();
        publication.setTime(System.currentTimeMillis());
        publication.setTitle(textInputTitle.getText().toString());
        publication.setContent(textInputContent.getText().toString());
        return publication;
    }

}
