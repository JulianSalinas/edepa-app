package imagisoft.rommie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.util.DateConverter;

/**
 * Contiene cada una de las actividades del congreso
 */
public class ScheduleView extends Fragment {

    /**
     * Es la capa donde se coloca cada una de las actividades/eventos
     */
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Actividades de prueba para mostrar como se ve el contenido
        ArrayList<ScheduleItem> items = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            try { items.add(getTestingObject()); }
            catch (Exception e) { e.printStackTrace();}
        }

        setupRecyclerView(items);
    }

    /**
     * Se configura la capa que contiene las actividades (copiado de internet)
     */
    public void setupRecyclerView(ArrayList<ScheduleItem> items){
        recyclerView = getView().findViewById(R.id.schedule_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScheduleViewAdapter(items));
    }

    public class ScheduleItem extends ScheduleEvent {

        ScheduleItem(Long id, Long start, Long end, String eventype, String header, String brief) {
            super(id, start, end, eventype, header, brief);
        }

    }

    // TODO: Borrar esta función al tener actividades registradas
    public ScheduleItem getTestingObject() throws Exception{

        Exhibitor first = new Exhibitor("Julian Salinas", "Instituto Tecnológico de Costa Rica");
        Exhibitor second = new Exhibitor("Brandon Dinarte", "Instituto Tecnológico de Costa Rica");

        ScheduleItem event = new ScheduleItem(
                123L,
                DateConverter.stringToLong("12/12/18 11:00 am"),
                DateConverter.stringToLong("12/12/18 2:30 pm"),
                "Conferencia",
                "Nombre lo suficientemente largo para cubrir dos líneas",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ullamcorper aliquet dictum. Maecenas in imperdiet dui"
        );

        event.addExhibitor(first);
        event.addExhibitor(second);
        return event;

    }

    /**
     * Sirve para enlazar las funciones a una actividad en específico
     */
    public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder> {

        /**
         * Objetos del modelo que serán adaptados visualmente
         */
        private ArrayList<ScheduleItem> items;

        private ScheduleViewAdapter(ArrayList<ScheduleItem> items){
            this.items = items;
        }

        /**
         * Requerida para saber la cantidad vistas que se tiene que crear
         */
        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         * No usar código en ésta función
         */
        @Override
        public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_optional, null);
            return new ScheduleViewHolder(view);
        }

        /**
         * Se enlazan los componentes y se agregan funciones a cada uno
         * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
         */
        @Override
        public void onBindViewHolder(ScheduleViewHolder holder, final int position) {
            ScheduleItem item = items.get(holder.getAdapterPosition());

            // Forma el string para colocar la fecha de la actividad
            String range =
                    getResources().getString(R.string.text_from) + " " +
                    DateConverter.extractTime(item.getStart()) + " " +
                    getResources().getString(R.string.text_to) + " " +
                    DateConverter.extractTime(item.getEnd());

            // Rellana todos los espacios de la actividad
            holder.time.setText(range);
            holder.header.setText(item.getHeader());
            holder.eventype.setText(item.getEventype());

            holder.readmore.setOnClickListener(new View.OnClickListener() {

                /**
                 * Función ejecutada al presionar el botón "readmore" de una actividad
                 * TODO: Pasar "item" a la "ScheduleDetail" para saber que información mostrar
                 */
                @Override
                public void onClick(View v) {
                    ActivityMain activityMain = (ActivityMain) getActivity();
                    activityMain.switchFragment(new ScheduleDetail());
                }

            });

        }

        /**
         * Calse para enlzar cada uno de los componentes visuales de la actividad.
         * Es necesario que esta clase este anidada, asi que, no mover!
         */
        class ScheduleViewHolder extends RecyclerView.ViewHolder {

            TextView time;
            TextView header;
            TextView eventype;
            TextView readmore;

            public ScheduleViewHolder(View view) {
                super(view);
                this.time = view.findViewById(R.id.schedule_item_time);
                this.header = view.findViewById(R.id.schedule_item_header);
                this.eventype = view.findViewById(R.id.schedule_item_eventype);
                this.readmore = view.findViewById(R.id.shedule_item_readmore);
            }

        }

    }

}