package edepa.search;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
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
import edepa.app.MainActivity;
import edepa.app.NavigationActivity;
import edepa.cloud.CloudFavorites;
import edepa.events.EventFragment;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;
import edepa.modelview.R;


public class SearchHolderEvent extends RecyclerView.ViewHolder {

    @BindView(R.id.event_decoration)
    View eventDecoration;

    @BindView(R.id.event_title)
    TextView eventTitle;

    @BindView(R.id.event_location)
    TextView eventLocation;

    @BindView(R.id.event_type_and_time)
    TextView eventTypeAndTime;

    protected int accent;
    protected Event event;
    protected Context context;

    public SearchHolderEvent(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        accent = ContextCompat.getColor(context, R.color.app_accent);
    }

    public void bind(Event event) {
        this.event = event;
        bindDecoration();
        bindInformation();
        itemView.setOnClickListener(v -> openEvent());
    }

    public void openEvent() {
        if(context instanceof NavigationActivity){
            NavigationActivity activity = (NavigationActivity) context;
            activity.getSearchView().closeSearch();
            Fragment fragment = EventFragment.newInstance(event);
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
    }

    public void bindInformation(){
        eventTitle.setText(event.getTitle());
        eventLocation.setText(event.getLocation());
        String description = TimeConverter.getBlockString(context,
                event.getStart(), event.getEnd()).toLowerCase();
        int resource = event.getEventype().getStringResource();
        description = context.getString(resource) + " " + description;
        eventTypeAndTime.setText(description);
    }

    public void highlightText(String query) {
        if(query.length() >= 1) {
            highlightText(eventTitle, query);
            highlightText(eventLocation, query);
            highlightText(eventTypeAndTime, query);
        }
    }

    public void highlightText(TextView textView, String query){
        textView.setText(TextHighlighter.highlightText(query,
                textView.getText().toString(), accent));
    }

}