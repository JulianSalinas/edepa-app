package edepa.info;

import edepa.cloud.Cloud;
import edepa.comments.CommentEditor;
import edepa.minilibs.DialogFancy;
import edepa.model.Comment;
import edepa.modelview.R;
import edepa.custom.CustomFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.res.Resources;

import mehdi.sakout.aboutpage.Element;
import mehdi.sakout.aboutpage.AboutPage;


public class AboutFragment extends CustomFragment {

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.string.text_about_description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarText(R.string.nav_about);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);
    }

    /**
     * Usa una librería, por lo que no se debe llamar a la función
     * super.onCreateView. Solo se retorna la vista que crea la librería
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        Resources resources = getResources();
        Element versionElement = new Element();
        versionElement.setTitle(resources.getString(R.string.text_version));
        versionElement.setIconDrawable(R.drawable.ic_android);

        Element commentElement = new Element();
        commentElement.setTitle(resources.getString(R.string.text_send_us_a_comment));
        commentElement.setIconDrawable(R.drawable.ic_comment);
        commentElement.setOnClickListener(v -> {
            CommentEditor editor = new CommentEditor();
            editor.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            editor.setCommentSender(comment -> {
                Cloud.getInstance().getCommentsReference().push().setValue(comment);
                editor.dismiss();
                new DialogFancy.Builder()
                        .setContext(getContext())
                        .setStatus(DialogFancy.SUCCESS)
                        .setTitle(R.string.text_success)
                        .build().show();
            });
            editor.show(getChildFragmentManager(), "COMMENT_EDITOR");
        });

        return new AboutPage(activity)
                .isRTL(false)
                .addItem(versionElement)
                .addItem(commentElement)
                .addPlayStore("imagisoft.rommie")
                .setImage(R.drawable.ic_edepa)
                .addGroup(resources.getString(R.string.text_connect_with_us))
                .addEmail("july12sali@gmail.com", "Julian Salinas")
                .addEmail("bdinarte1996@gmail.com", "Brandon Dinarte")
                .setDescription(resources.getString(getResource()))
                .create();
    }

}
