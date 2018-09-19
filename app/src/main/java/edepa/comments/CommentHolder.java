package edepa.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.cloud.CloudUsers;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.TimeConverter;
import edepa.minilibs.TimeGenerator;
import edepa.model.Comment;
import edepa.model.UserProfile;
import edepa.modelview.R;

public class CommentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comments_item_content)
    TextView itemContent;

    @BindView(R.id.comments_item_time_ago)
    TextView itemTimeAgo;

    @BindView(R.id.comments_item_username)
    TextView itemUsername;

    @BindView(R.id.comments_item_avatar)
    ImageView itemAvatar;

    private TimeGenerator timeGenerator;

    public CommentHolder(View itemView) {
        super(itemView);
        timeGenerator = new TimeGenerator(itemView.getContext());
    }

    public void bind(Comment comment){
        ButterKnife.bind(this, itemView);
        itemContent.setText(TextHighlighter.decodeSpannables(comment.getContent()));
        itemTimeAgo.setText(timeGenerator.getTimeAgo(comment.getTime()));
        CloudUsers cloudUsers = new CloudUsers();
        cloudUsers.setUserProfileListener(userProfile -> {
            itemUsername.setText(userProfile.getUsername());
            Glide.with(itemView)
                    .load(userProfile.getPhotoUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(itemAvatar);
        });
        cloudUsers.requestUserInfo(comment.getUserid());
    }

}
