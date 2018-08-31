package edepa.events;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.app.ActivityMain;
import edepa.cloud.CloudFavorites;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;
import edepa.modelview.R;


public class EventHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.event_timestamp)
    TextView eventTimestamp;

    @BindView(R.id.event_decoration)
    View eventDecoration;

    @BindView(R.id.event_title)
    TextView eventTitle;

    @BindView(R.id.event_type)
    TextView eventType;

    @BindView(R.id.event_location)
    TextView eventLocation;

    @BindView(R.id.event_readmore)
    TextView eventReadmore;

    @BindView(R.id.event_favorite_button)
    LikeButton eventFavoriteButton;

    @BindView(R.id.event_time_description)
    TextView eventTimeDescription;

    protected int accent;
    protected Event event;
    protected Context context;

    public EventHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        accent = ContextCompat.getColor(context, R.color.app_accent);
    }

    public void bind(Event event) {
        this.event = event;
    }

    public static class Single extends EventHolder implements OnLikeListener {

        public Single(View itemView) {
            super(itemView);
        }

        public void bind(Event event) {
            super.bind(event);
            bindDecoration();
            bindInformation();
            bindFavoriteButton();
        }

        @OnClick(R.id.event_readmore_container)
        public void openEvent(){
            if(context instanceof ActivityMain){
                ActivityMain activity = (ActivityMain) context;
                Fragment fragment= EventFragment.newInstance(event);
                activity.setFragmentOnScreen(fragment, event.getKey());
            }
        }

        /**
         * TODO Colocar aquí con base a preferencias
         */
        public void bindDecoration() {
            Resources res = context.getResources();
            int color = res.getColor(event.getEventype().getColorResource());
            eventDecoration.setBackgroundColor(color);
            eventReadmore.setTextColor(color);
            eventType.setBackgroundColor(color);
        }

        public void bindInformation(){
            eventTitle.setText(event.getTitle());
            eventType.setText(event.getEventype().toString());
            String description = TimeConverter.getBlockString(
                    context, event.getStart(), event.getEnd());
            eventTimeDescription.setText(description);
            eventLocation.setText(event.getLocation());
        }

        public void bindFavoriteButton(){
            eventFavoriteButton.setOnLikeListener(this);
            eventFavoriteButton.setLiked(event.isFavorite());
        }

        @Override
        public void liked(LikeButton likeButton) {
            CloudFavorites.markAsFavorite(event);
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            CloudFavorites.unmarkAsFavorite(event);
        }

    }

    public static class WithTime extends Single {

        public WithTime(View view) {
            super(view);
        }

        @Override
        public void bind(Event event) {
            super.bind(event);
            if(eventTimestamp != null) {
                long start = event.getStart();
                String date = TimeConverter.getBlockString(context, start);
                eventTimestamp.setText(date);
            }
        }

    }

}