package imagisoft.rommie;

import java.util.List;
import imagisoft.edepa.ScheduleBlock;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

/**
 * Contiene cada una de las actividades del congreso
 */
public class ScheduleView extends MainViewFragment {

    /**
     * Es la capa donde se coloca cada una de las actividades/eventos
     */
    private RecyclerView recyclerView;

    /**
     * Eventos para colocar en la lista, se asume que entran
     * ordenados por fecha
     */
    private List<ScheduleBlock> events;

    public List<ScheduleBlock> getEvents(){
        return events;
    }

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
     * Se crea la vista que contiene el recyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_view, container, false);
    }

    /**
     * Justo después de crear la vista, se debe cargar el contenido del modelo
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupRecyclerView();
    }

    /**
     * Se configura la capa que contiene las actividades
     */
    public void setupRecyclerView(){

        assert getView() != null;
        recyclerView = getView().findViewById(R.id.schedule_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ScheduleViewAdapter(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));

    }

}