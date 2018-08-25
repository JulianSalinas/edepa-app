package edepa.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

import edepa.crawler.Regex;
import edepa.custom.RecyclerAdapter;
import edepa.minilibs.RegexSearcher;
import edepa.minilibs.TimeConverter;
import edepa.model.Event;
import edepa.model.Person;
import edepa.modelview.R;
import edepa.people.PersonHolder;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public abstract class EventsAdapter extends RecyclerAdapter {

    /**
     * Variables par escoger el tipo de vista que se colocará
     *
     * @see #getItemViewType(int)
     */
    protected int SINGLE = 0;
    protected int WITH_SEPARATOR = 1;

    protected List<Event> events;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * Constructor de {@link EventsAdapter}
     * Las subclases deben colocar aquí
     * la lista de eventos y la de favoritos
     */
    public EventsAdapter(List<Event> events) {
        super();
        this.events = events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == SINGLE ?
                R.layout.event_item:
                R.layout.event_item_time;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == SINGLE ?
                new EventHolder.Single(view) :
                new EventHolder.WithTime(view);

    }

    /**
     * Obtiene si la vista es un bloque de hora o una actividad
     */
    @Override
    public int getItemViewType(int position) {

        final Event item = getEventAt(position);
        if (position == 0) return WITH_SEPARATOR;

        else {
            Event upItem = getEventAt(position - 1);
            long itemStart = item.getStart();
            long upItemStart = upItem.getStart();
            long upItemEnd = upItem.getEnd();

            return Math.abs(upItemEnd - itemStart) < 20 * 60 * 1000 ||
                    upItemStart <= itemStart && itemStart < upItemEnd ?
                    SINGLE : WITH_SEPARATOR;
        }

    }

    public Event getEventAt(int position){
        return events.get(position);
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
        final Event event = events.get(holder.getAdapterPosition());
        EventHolder.Single eventHolder = (EventHolder.Single) holder;
        eventHolder.bind(event);
    }

}
