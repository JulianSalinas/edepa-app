package edepa.pagers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import edepa.custom.EmptyOngoing;
import edepa.custom.EmptySchedule;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.events.EventsSchedule;


public class PagerSchedule extends PagerFragment {

    /**
     * {@inheritDoc}
     * Es invocado el método {@link #onCreate(Bundle)}
     * @return PagerAdapter
     */
    @Override
    protected PagerAdapter instantiateAdapter() {
        return new PagerAdapter(this) {
        protected Fragment instantiateEventsFragment() {
            return new EventsSchedule();
        }};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void inflateEmptyView() {
        String tag = "EMPTY_SCHEDULE";
        Fragment frag = new EmptySchedule();
        FragmentManager manager = getChildFragmentManager();
        manager .beginTransaction()
                .replace(R.id.events_empty_view, frag, tag)
                .commit();
    }

    /**
     * {@inheritDoc}
     * Se conecta la base de datos para comenzar a
     * obtener los eventos y poder agregar la páginas
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
