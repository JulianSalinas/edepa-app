package imagisoft.rommie;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import imagisoft.edepa.ScheduleBlock;


public class ScheduleView extends MainViewFragment {

    /**
     * Es la capa donde se coloca cada una de las actividades/eventos
     */
    @BindView(R.id.schedule_view)
    RecyclerView eventsView;

    /**
     * Eventos para colocar en la lista, se asume que entran
     * ordenados por fecha
     */
    private List<ScheduleBlock> events;

    /**
     * Adaptador para almacenar administrar las vistas de los eventos
     */
    private ScheduleViewAdapter adapter;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleView newInstance(List<ScheduleBlock> events) {
        ScheduleView fragment = new ScheduleView();
        fragment.events = events;
        return fragment;
    }

    /**
     * Se crea la vista que contiene el exhibitorsRecyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.schedule_view);
    }

    /**
     * Justo después de crear la vista, se debe cargar el contenido del modelo
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        // Se revisa porque al entrar por seguna vez, no es necesario colocar el adaptador
        if(adapter == null)
            adapter = new ScheduleViewAdapter(this, events);

        setupEventsView();

    }

    /**
     * Se configura la capa que contiene las actividades
     */
    public void setupEventsView(){
        eventsView.setHasFixedSize(true);
        eventsView.setAdapter(adapter);
        eventsView.setItemAnimator(new DefaultItemAnimator());
        eventsView.setLayoutManager(new SmoothLayout(this.getActivity()));
    }

}