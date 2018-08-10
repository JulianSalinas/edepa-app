package edepa.pagers;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;

import edepa.model.ScheduleEvent;
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
     * Se conecta la base de datos para comenzar a
     * obtener los eventos y poder agregar la páginas
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventsEmptyView.setText(getString(R.string.text_without_events));
        getEventsQuery().addChildEventListener(PagerSchedule.this);
    }

    /**
     * {@inheritDoc}
     * Se desconecta de la base de datos
     */
    @Override
    public void onDestroyView() {
        super.onDestroy();
        getEventsQuery().removeEventListener(this);
    }

    /**
     * {@inheritDoc}
     * Si no existe una página para el nuevo evento, se
     * agrega una nueva
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        super.onChildAdded(dataSnapshot, s);
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null && event.getDate() != null)
            addPageIfNotExists(event);
    }

    /**
     * {@inheritDoc}
     * Un evento ha cambiado de fecha y se necesita actualizar
     * las páginas actuales
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        super.onChildChanged(dataSnapshot, s);
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null && event.getDate() != null)
            addPageIfNotExists(event);
    }

}
