package imagisoft.rommie;

import java.util.ArrayList;
import imagisoft.edepa.EventType;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

/**
 * Contiene cada una de las actividades del congreso
 */
public class ScheduleView extends ActivityMainFrag {

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
        setupRecyclerView(loadModelEvents());
    }

    /**
     * TODO: Prueba para mostrar como se ve el contenido
     */
    public ArrayList<ScheduleItemView> loadModelEvents(){
        ArrayList<ScheduleItemView> items = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            try {
                ScheduleBlockView block = getTestingObject();
                items.add(block);
                items.addAll(block.events);
            }
            catch (Exception e) { e.printStackTrace();}
        }
        return items;
    }

    // TODO: Borrar esta función al tener actividades registradas
    public ScheduleBlockView getTestingObject() throws Exception{

        ScheduleBlockView block = new ScheduleBlockView(
                UDateConverter.stringToLong("12/12/18 11:00 am"),
                UDateConverter.stringToLong("12/12/18 2:30 pm")
        );


        for(int i = 0; i < 4; i++) {
            Exhibitor first = new Exhibitor("Julian Salinas", "Instituto Tecnológico de Costa Rica");
            Exhibitor second = new Exhibitor("Brandon Dinarte", "Instituto Tecnológico de Costa Rica");

            ScheduleEventView event = new ScheduleEventView(
                    123L,
                    UDateConverter.stringToLong("12/12/18 11:00 am"),
                    UDateConverter.stringToLong("12/12/18 2:30 pm"),
                    "Nombre lo suficientemente largo para cubrir dos líneas",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ullamcorper aliquet dictum. Maecenas in imperdiet dui",
                    EventType.values()[i]
            );

            event.addExhibitor(first);
            event.addExhibitor(second);
            block.events.add(event);
        }

        return block;

    }

    /**
     * Se configura la capa que contiene las actividades (copiado de internet)
     */
    public void setupRecyclerView(ArrayList<ScheduleItemView> items){
        assert getView() != null;
        recyclerView = getView().findViewById(R.id.schedule_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScheduleViewAdapter(this, items));
    }

    interface ScheduleItemView {
        // Necesita esta vacio
    }

    public class ScheduleBlockView extends ScheduleBlock implements ScheduleItemView {

        ArrayList<ScheduleEventView> events;

        ScheduleBlockView(Long start, Long end) {
            super(start, end);
            this.events = new ArrayList<>();
        }

    }

    public class ScheduleEventView extends ScheduleEvent implements ScheduleItemView {

        ScheduleEventView(Long id, Long start, Long end, String header, String brief, EventType eventType) {
            super(id, start, end, header, brief, eventType);
        }

    }

}