package imagisoft.rommie;

import java.util.ArrayList;
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
     * Se crea la vista que contiene el recyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_view, container, false);
    }

    /**
     * Justo después de crear la vista, se debe cargar el contenido del modelo
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // setupRecyclerView(loadModelEvents());
    }

    /**
     * Se configura la capa que contiene las actividades
     */
    public void setupRecyclerView(ArrayList<ScheduleBlock> items){
        assert getView() != null;
        recyclerView = getView().findViewById(R.id.schedule_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScheduleViewAdapter(this, items));
    }

}