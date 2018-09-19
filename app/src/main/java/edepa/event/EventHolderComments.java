package edepa.event;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.firebase.database.Query;

import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.app.NavigationActivity;
import edepa.cloud.Cloud;
import edepa.comments.CommentsView;
import edepa.model.Event;
import edepa.modelview.R;


public class EventHolderComments extends RecyclerView.ViewHolder {

    protected Event event;

    public EventHolderComments(View itemView) {
        super(itemView);
        itemView.setOnClickListener(v -> openComments());
    }

    public void bind(Event event){
        this.event = event;
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.event_comments_view)
    public void openComments(){
//        String tag = "comments:" + event.getKey();
//        Fragment frag = EventComments.newInstance(event);
//        if (itemView.getContext() instanceof NavigationActivity) {
//            NavigationActivity activity = (NavigationActivity) itemView.getContext();
//            activity.setFragmentOnScreen(frag, tag);
//        }
    }


}
