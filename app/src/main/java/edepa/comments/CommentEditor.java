package edepa.comments;

import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.Cloud;
import edepa.custom.DefaultDialog;
import edepa.model.Comment;
import edepa.modelview.R;


public class CommentEditor extends DefaultDialog {

    @BindView(R.id.comment_editor_content)
    TextInputEditText editorContent;

    @Override
    public int getResource() {
        return R.layout.comments_editor;
    }

    private CommentSender commentSender;

    public interface CommentSender {
        void sendComment(Comment comment);
    }

    public void setCommentSender(CommentSender commentSender) {
        this.commentSender = commentSender;
    }

    @OnClick(R.id.comment_editor_send)
    public void sendComment(){
        boolean hasContent = !editorContent.getText().toString().isEmpty();
        if (commentSender != null && hasContent){
            commentSender.sendComment(buildComment());
        }
        dismiss();
    }

    private Comment buildComment() {
        return new Comment.Builder()
                .time(System.currentTimeMillis())
                .userid(Cloud.getInstance().getUserId())
                .content(editorContent.getText().toString())
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}
