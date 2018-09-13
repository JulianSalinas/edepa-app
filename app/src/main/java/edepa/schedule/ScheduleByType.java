package edepa.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.Query;

import edepa.cloud.CloudEvents;
import edepa.model.EventType;

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

}
