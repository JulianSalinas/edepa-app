package edepa.comments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.custom.CustomFragment;
import edepa.modelview.R;

public abstract class CommentsFragment
        extends CustomFragment implements CommentsView.ICommentable {

    @BindView(R.id.comments_screen)
    View commentsScreen;

    @BindView(R.id.comments_recycler)
    RecyclerView commentsRecycler;

    @BindView(R.id.add_comment_button)
    View addCommentButton;

    @Override
    public int getResource() {
        return R.layout.comments_screen_events;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CommentsView commentsView = new CommentsView(commentsScreen);
        commentsView.bind(this);
    }

    @OnClick(R.id.add_comment_button)
    public void openCommentEditor(){
        String tag = "COMMENT_EDITOR";
        CommentEditor editor = new CommentEditor();
        editor.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        editor.setCommentSender(comment -> {
            getCommentsQuery().getRef().push().setValue(comment);
        });
        editor.show(getChildFragmentManager(), tag);
    }

}
