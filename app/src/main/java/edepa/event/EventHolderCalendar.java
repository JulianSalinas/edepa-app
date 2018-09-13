package edepa.event;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.model.Event;
import edepa.modelview.R;

public class EventHolderCalendar extends RecyclerView.ViewHolder {

    @BindView(R.id.event_agenda_date_rage)
    TextView eventAgendaDateRange;

    private Event event;

    public EventHolderCalendar(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> openCalendar());
    }

    public void bind(Event event){
        this.event = event;
    }

    public void openCalendar(){
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEnd())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStart())
                .putExtra(CalendarContract.Events.TITLE, event.getTitle())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocation())
                .putExtra(CalendarContract.Events.CALENDAR_COLOR, event.getEventype().getColorResource())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        itemView.getContext().startActivity(intent);
    }

}
