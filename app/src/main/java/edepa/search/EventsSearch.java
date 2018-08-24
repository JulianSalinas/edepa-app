package edepa.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

import edepa.events.EventHolder;
import edepa.events.EventsAdapter;
import edepa.minilibs.RegexSearcher;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class EventsSearch extends EventsAdapter implements Filterable {


    protected String query;
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

    /**
     * Constructor de {@link EventsSearch}
     * Las subclases deben colocar aquí
     * la lista de eventos y la de favoritos
     */
    public EventsSearch(Context context, List<Event> events) {
        super(context, events);
        this.query = "";
        this.eventsFiltered = events;
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
        EventHolder.Single eventHolder = (EventHolder.Single) holder;
        eventHolder.bind(event);
        eventHolder.highlightText(query);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                query = constraint.toString();
                eventsFiltered = new ArrayList<>();
                if(query.isEmpty()) eventsFiltered.addAll(events);

                else {
                    for (Event event : events){

                        ArrayList<MatchResult> results = RegexSearcher
                                .autoSearch(query, event.getTitle());

                        results.addAll(
                                RegexSearcher.autoSearch(query,
                                TimeConverter.getBlockString(context,
                                        event.getStart(), event.getEnd())));

                        results.addAll(
                                RegexSearcher.autoSearch(query,
                                        context.getString(event.getEventype().getStringResource()))
                        );

                        if(results.size() > 0) eventsFiltered.add(event);

                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = eventsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                eventsFiltered = (ArrayList<Event>) results.values;
                notifyDataSetChanged();
            }

        };

    }

}
