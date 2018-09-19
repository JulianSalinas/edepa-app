package edepa.schedule;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import edepa.cloud.CloudEvents;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;
import edepa.model.EventType;
import edepa.modelview.R;

/**
 * Contiene todos los eventos del cronograma, incluidos
 * los favoritos y los no favoritos
 */
public class ScheduleByType extends ScheduleEvents implements IEventsByType {

    public static final String TYPE_KEY = "type";

    protected IPageListenerByType pageListener;

    protected EventType eventype;

    public EventType getEventype() {
        return eventype;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(TYPE_KEY)){
            String type = args.getString(TYPE_KEY);
            eventype = EventType.valueOf(type);
        }

        // Se obtiene el IPageListener que en este caso
        // es un PagerFragment que implementa IPageListener
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof IPageListenerByType)
            pageListener = (IPageListenerByType) fragment;

        super.onCreate(savedInstanceState);
    }

    /**
     * Cada vez que se remueve un evento se revisa si quedan
     * más eventos, de lo contrario se avisa a IPageListener
     * que debe remover este fragmento
     */
    public RecyclerView.AdapterDataObserver getDataObserver() {
        return new RecyclerView.AdapterDataObserver() {

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if(events.size() <= 0 && pageListener != null)
                    pageListener.onPageRemoved(getEventype());
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(pageListener != null && eventsAmount-- <= 0)
                    pageListener.onPageChanged(getEventype());
            }

        };
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de eventos para un fecha
     * en específico (que es pasada al fragmento como arg)
     * @return Query
     */
    public Query getEventsQuery(){
        return CloudEvents.getEventsQueryUsingType(getEventype());
    }

    @Override
    protected ScheduleAdapter instantiateAdapter() {
        return new AdapterSchedule();
    }

    /**
     * Clase que modifica la lista de eventos y de favoritos
     * del fragmento {@link ScheduleEvents}
     */
    public class AdapterSchedule extends ScheduleAdapter {

        public AdapterSchedule() {
            super(ScheduleByType.this.events);
            registerAdapterDataObserver(getDataObserver());
        }

        @Override
        public int getItemViewType(int position) {

            final Event item = getEventAt(position);
            if (position == 0) return WITH_SEPARATOR;

            else {
                Event upItem = getEventAt(position - 1);
                long itemDate = TimeConverter.atStartOfDay(item.getStart());
                long upItemDate = TimeConverter.atStartOfDay(upItem.getStart());
                return itemDate == upItemDate ? SINGLE : WITH_SEPARATOR;
            }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            int layout = viewType == SINGLE?
                    R.layout.event_item:
                    R.layout.event_item_with_time;

            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(layout, parent, false);

            return viewType == SINGLE?
                    new ScheduleItemHolder.Single(view) :
                    new ScheduleItemHolder.WithDay(view);

        }

    }

}
