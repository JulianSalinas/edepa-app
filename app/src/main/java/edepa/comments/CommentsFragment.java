package edepa.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.custom.BaseFragment;
import edepa.model.Event;
import edepa.modelview.R;

public abstract class CommentsFragment
        extends BaseFragment implements CommentsView.ICommentable {

    @BindView(R.id.comments_screen)
    View commentsScreen;

    @BindView(R.id.comments_recycler)
    RecyclerView commentsRecycler;

    @BindView(R.id.add_comment_button)
    FloatingActionButton addCommentButton;

    @Override
    public int getResource() {
        return R.layout.comments_screen;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CommentsView commentsView = new CommentsView(commentsScreen);
        commentsView.bind(this);
        addCommentButton.setVisibility(View.VISIBLE);
        commentsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addCommentButton.getVisibility() == View.VISIBLE)
                    addCommentButton.hide();
                else if (dy < 0 && addCommentButton.getVisibility() != View.VISIBLE) {
                    addCommentButton.show();
                }
            }
        });

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
