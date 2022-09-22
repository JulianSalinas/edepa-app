package edepa.pagers;

import android.os.Bundle;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentPagerAdapter;

import edepa.custom.EmptySchedule;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.schedule.ScheduleEvents;


public class PagerSchedule extends PagerFragment {

    /**
     * {@inheritDoc}
     * Es invocado el método {@link #onCreate(Bundle)}
     * @return EventPagerAdapter
     */
    @Override
    protected FragmentPagerAdapter instantiateAdapter() {
        return new PagerAdapter(this, dates) {
        protected Fragment instantiateEventsFragment() {
            return new ScheduleEvents();
        }};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void inflateEmptyView() {
        String tag = "EMPTY_SCHEDULE";
        FragmentManager manager = getChildFragmentManager();
        Fragment temp = manager.findFragmentByTag(tag);
        Fragment frag = temp != null ? temp : new EmptySchedule();
        manager .beginTransaction()
                .replace(R.id.events_empty_view, frag, tag)
                .commit();
    }

    /**
     * {@inheritDoc}
     * Si no existe una página para el nuevo evento, se
     * agrega una nueva
     */
    @Override
    public void addEvent(Event event) {
        if (event.getDate() != 0L)
            addPageIfNotExists(event);
    }

    /**
     * {@inheritDoc}
     * Un evento ha cambiado de fecha y se necesita actualizar
     * las páginas actuales
     */
    @Override
    public void changeEvent(Event event) {
        if (event.getDate() != 0L)
            addPageIfNotExists(event);
    }

    @Override
    public void removeEvent(Event event) {
        // No se requiere
    }

}
