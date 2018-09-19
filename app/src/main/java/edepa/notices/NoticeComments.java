package edepa.notices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.database.Query;

import butterknife.OnClick;
import edepa.cloud.Cloud;
import edepa.comments.CommentsFragment;
import edepa.model.Event;
import edepa.model.Notice;
import edepa.modelview.R;

import static edepa.event.EventHostFragment.SAVED_EVENT_KEY;


public class NoticeComments extends CommentsFragment {

    private static final String SAVED_NOTICE_KEY = "notice_key";

    protected Notice notice;

    @Override
    public int getResource() {
        return R.layout.comments_screen_news;
    }

    @OnClick(R.id.back_button)
    public void onBackPressed(){
        activity.onBackPressed();
    }

    /**
     * Se obtiene una nueva instancia del fragmento
     * @return EventFragment
     */
    public static NoticeComments newInstance(Notice notice) {
        NoticeComments fragment = new NoticeComments();
        Bundle args = new Bundle();
        args.putParcelable(SAVED_NOTICE_KEY, notice);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null && args.containsKey(SAVED_NOTICE_KEY))
            notice = args.getParcelable(SAVED_NOTICE_KEY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.GONE);
    }

    /**
     * Guarda el evento que fue pasado como argumento
     * @param outState: Bundle donde se guarda el evento
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_NOTICE_KEY, notice);
    }

    /**
     * Se carga el evento que había antes de girar la pantalla
     * @param savedInstanceState: Bundle donde se carga el evento
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            notice = savedInstanceState.getParcelable(SAVED_NOTICE_KEY);
    }

    @Override
    public Query getCommentsQuery() {
        return Cloud.getInstance()
                .getReference("news_comments")
                .child(notice.getKey())
                .orderByChild("time")
                .limitToLast(200);
    }

}