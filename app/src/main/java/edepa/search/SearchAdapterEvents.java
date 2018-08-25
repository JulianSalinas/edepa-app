package edepa.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import edepa.cloud.CloudEvents;
import edepa.events.EventsAdapter;
import edepa.minilibs.RegexSearcher;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;
import edepa.model.EventType;
import edepa.modelview.R;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class SearchAdapterEvents
        extends EventsAdapter implements Filterable, CloudEvents.Callbacks {


    protected Context context;

    protected String query;
    protected EventType typeFilter;
    protected List<Event> eventsFiltered;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return eventsFiltered.size();
    }

    @Override
    public int getItemViewType(int position) {
        return SINGLE;
    }

    @Override
    public void addEvent(Event event) {
        int index = events.indexOf(event);
        if (index == -1 && filterEvent(event)){
            events.add(event);
            notifyItemInserted(events.size() - 1);
        }
    }

    @Override
    public void changeEvent(Event event) {
        int index = events.indexOf(event);
        if (index != -1){
            events.set(index, event);
            notifyItemChanged(index);
        }
    }

    @Override
    public void removeEvent(Event event) {
        int index = events.indexOf(event);
        if (index != -1){
            events.remove(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * Constructor de {@link SearchAdapterEvents}
     */
    public SearchAdapterEvents(EventType typeFilter) {
        super(new ArrayList<>());
        this.query = "";
        this.typeFilter = typeFilter;
        this.eventsFiltered = events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchHolderEvent onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolderEvent(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_event, parent, false));
    }

    /**
     * {@inheritDoc}
     * Se enlazan los componentes y se agregan funciones a cada uno
     *
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Event event = eventsFiltered.get(holder.getAdapterPosition());
        SearchHolderEvent eventHolder = (SearchHolderEvent) holder;
        eventHolder.bind(event);
        eventHolder.highlightText(query);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
    }

    public boolean matchEvent(Event event){
        boolean match = RegexSearcher.match(query, event.getTitle());

        long start = event.getStart();
        long end = event.getEnd();
        String time = TimeConverter.getBlockString(context, start, end);
        match = match || RegexSearcher.match(query, time);

        int resource = event.getEventype().getStringResource();
        match = match || RegexSearcher.match(query, context.getString(resource));
        return match || RegexSearcher.match(query, event.getLocation());
    }

    public boolean filterEvent(Event event) {
        EventType type = event.getEventype();
        return typeFilter == null || typeFilter.equals(type);
    }

    public void addAllBasedOnQuery(String query){
        eventsFiltered = new ArrayList<>();
        if (query == null || query.isEmpty())
             addAllThatFulfillTheFilter();
        else addAllThatMatchBothCriterias();
    }

    public void addAllThatFulfillTheFilter(){
        for (Event event : events) {
            if (filterEvent(event)) eventsFiltered.add(event);
        }
    }

    public void addAllThatMatchBothCriterias(){
        for (Event event : events) {
            if (filterEvent(event) && matchEvent(event))
                eventsFiltered.add(event);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            query = constraint.toString();
            addAllBasedOnQuery(query);
            FilterResults filterResults = new FilterResults();
            filterResults.values = eventsFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            eventsFiltered = (ArrayList<Event>) results.values;
            notifyDataSetChanged();
        }};

    }

}
