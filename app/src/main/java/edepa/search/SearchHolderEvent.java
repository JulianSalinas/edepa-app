package edepa.search;

import android.content.Context;
import android.content.res.Resources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.app.NavigationActivity;
import edepa.event.EventFragment;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;
import edepa.modelview.R;


public class SearchHolderEvent extends RecyclerView.ViewHolder {

    @BindView(R.id.event_decoration)
    View eventDecoration;

    @BindView(R.id.event_title)
    TextView eventTitle;

    @BindView(R.id.event_type)
    TextView eventType;

    @BindView(R.id.event_datetime)
    TextView eventDateTime;

    @BindView(R.id.event_location)
    TextView eventLocation;

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
        eventType.setBackgroundColor(color);
    }

    public void bindInformation(){
        eventTitle.setText(event.getTitle());
        eventType.setText(event.getEventype().toString());

        String date = TimeConverter.extractDate(
                TimeConverter.atStartOfDay(event.getStart()));

        String description = String.format("%s %s", date, TimeConverter
                        .getBlockString(context, event.getStart(), event.getEnd())
                        .toLowerCase());

        eventLocation.setText(event.getLocation());
        eventDateTime.setText(description);
    }

    public void highlightText(String query) {
        if(query.length() >= 1) {
            highlightText(eventTitle, query);
            highlightText(eventType, query);
            highlightText(eventDateTime, query);
            highlightText(eventLocation, query);
        }
    }

    public void highlightText(TextView textView, String query){
        textView.setText(TextHighlighter.highlightText(query,
                textView.getText().toString(), accent));
    }

}