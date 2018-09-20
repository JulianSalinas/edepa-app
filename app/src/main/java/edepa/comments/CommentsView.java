package edepa.comments;

import android.view.View;
import com.google.firebase.database.Query;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;

import java.util.List;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import edepa.modelview.R;
import edepa.model.Comment;
import edepa.cloud.CloudComments;
import edepa.minilibs.SmoothLayout;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class CommentsView
        extends RecyclerView.ViewHolder implements CloudComments.Callbacks {

    @BindView(R.id.comments_recycler)
    RecyclerView commentsRecycler;

    @BindView(R.id.comments_empty)
    View commentsEmpty;

    protected List<Comment> comments;

    protected CommentsAdapter commentsAdapter;

    protected ICommentable commentable;

    public interface ICommentable {
        Query getCommentsQuery();
    }

    public CommentsView(View itemView) {
        super(itemView);
        comments = new ArrayList<>();
    }

    public void bind(ICommentable commentable){

        this.comments.clear();
        this.commentable = commentable;
        ButterKnife.bind(this, itemView);

        commentsAdapter = new CommentsAdapter(comments);
        commentsRecycler.setAdapter(commentsAdapter);

        CloudComments cloudComments = new CloudComments();
        cloudComments.setCallbacks(this);
        cloudComments.connect(commentable.getCommentsQuery());

        DividerItemDecoration decoration =
                new DividerItemDecoration(itemView.getContext(), VERTICAL);
        decoration.setDrawable(itemView.getContext()
                .getResources().getDrawable(R.drawable.util_decorator));
        commentsRecycler.addItemDecoration(decoration);

        commentsEmpty.setVisibility(comments.size() <= 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public void addComment(Comment comment) {
        if (!comments.contains(comment)){
            comments.add(comment);
            commentsAdapter.notifyItemInserted(comments.size() - 1);
            commentsEmpty.setVisibility(comments.size() <= 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void removeComment(Comment comment) {
        int index = comments.indexOf(comment);
        if (index != -1){
            comments.remove(index);
            commentsAdapter.notifyItemRemoved(index);
            commentsEmpty.setVisibility(comments.size() <= 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void changeComment(Comment comment) {
        int index = comments.indexOf(comment);
        if (index != -1){
            comments.set(index, comment);
            commentsAdapter.notifyItemChanged(index);
        }
    }

}
