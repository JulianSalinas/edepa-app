package imagisoft.rommie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;

import imagisoft.edepa.EventType;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

/**
 * Contiene cada una de las actividades del congreso
 */
public class ScheduleView extends Fragment {

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
        recyclerView = getView().findViewById(R.id.schedule_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScheduleViewAdapter(items));
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

    /**
     * Sirve para enlazar las funciones a una actividad en específico
     */
    public class ScheduleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        /**
         * Variables par escoger el tipo de vista que se colocará
         */
        private int SCHEDULE_BLOCK_VIEW_TYPE = 1;
        private int SCHEDULE_EVENT_VIEW_TYPE = 2;

        /**
         * Objetos del modelo que serán adaptados visualmente
         */
        private ArrayList<ScheduleItemView> items;

        private ScheduleViewAdapter(ArrayList<ScheduleItemView> items){
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
         *  Obtiene si la vista es un bloque de hora una actividad
         */
        @Override
        public int getItemViewType(int position) {
            ScheduleItemView item = items.get(position);
                return (item instanceof ScheduleBlockView) ?
                        SCHEDULE_BLOCK_VIEW_TYPE:
                        SCHEDULE_EVENT_VIEW_TYPE;
        }

        /**
         * No usar código en ésta función
         */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // Vista para mostrar la hora inicial de un bloque de actividades
            if(viewType == SCHEDULE_BLOCK_VIEW_TYPE){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_block, null);
                return new ScheduleBlockViewHolder(view);
            }

            // Vista para mostrar las actividades como tal
            else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, null);
                return new ScheduleEventViewHolder(view);
            }

        }

        /**
         * Se enlazan los componentes y se agregan funciones a cada uno
         * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
         */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder.getItemViewType() == SCHEDULE_EVENT_VIEW_TYPE)
                onBindScheduleEventViewHolder((ScheduleEventViewHolder) holder);
            else
                onBindScheduleBlockViewHolder((ScheduleBlockViewHolder) holder);
        }

        public void onBindScheduleEventViewHolder(ScheduleEventViewHolder holder){

            // Items para extraer los datos y colocarlos en la vista
            ScheduleEventView item = (ScheduleEventView) items.get(holder.getAdapterPosition());

            // Forma el string para colocar la fecha de la actividad
            String range =
                    getResources().getString(R.string.text_from) + " " +
                            UDateConverter.extractTime(item.getStart()) + " " +
                            getResources().getString(R.string.text_to) + " " +
                            UDateConverter.extractTime(item.getEnd());

            // Rellana todos los espacios de la actividad
            holder.time.setText(range);
            holder.header.setText(item.getHeader());
            holder.eventype.setText(item.getEventype().toString());

            // Coloca el color acorde al tipo de actividad
            int colorResource = mapToColor(item.getEventype());
            holder.line.setBackgroundResource(colorResource);
            holder.readmore.setTextColor(getResources().getColor(colorResource));

            /*
             * Función ejecutada al presionar el botón "readmore" de una actividad
             * TODO: Pasar "item" a la "ScheduleDetail" para saber que información mostrar
             */
            holder.readmore.setOnClickListener(v ->  {
                ActivityMain activityMain = (ActivityMain) getActivity();
                activityMain.switchFragment(new ScheduleDetail());
            });

            /*
             * Función ejecutada al presionar la "estrellita" de una actividad
             * TODO: Aquí va la función de agregar a favoritos
             */
            holder.favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
                ActivityMain activity = (ActivityMain) getActivity();
                int text = favorite ?
                        R.string.text_marked_as_favorite :
                        R.string.text_unmarked_as_favorite;
                activity.showStatusMessage(getResources().getString(text));
            });

        }

        public int mapToColor(EventType eventType){
            switch (eventType){
                case CONFERENCIA: return R.color.material_pink;
                case FERIA_EDEPA: return R.color.material_amber;
                case TALLER: return R.color.material_green;
                case PONENCIA: return R.color.material_blue;
                default: return R.color.app_light_detail;
            }
        }

        public void onBindScheduleBlockViewHolder(ScheduleBlockViewHolder holder){

            // Items para extraer los datos y colocarlos en la vista
            ScheduleBlockView item = (ScheduleBlockView) items.get(holder.getAdapterPosition());

            // Forma el string para colocar la fecha de la actividad
            String range =
                    getResources().getString(R.string.text_from) + " " +
                            UDateConverter.extractTime(item.getStart()) + " " +
                            getResources().getString(R.string.text_to) + " " +
                            UDateConverter.extractTime(item.getEnd());

            // Rellana todos los espacios de la actividad
            holder.time.setText(range);

        }

        /**
         * Clase para enlzar cada uno de los componentes visuales de la actividad.
         * Es necesario que esta clase este anidada, asi que, no mover!
         */
        class ScheduleEventViewHolder extends RecyclerView.ViewHolder {

            View line;
            TextView time;
            TextView header;
            TextView eventype;
            TextView readmore;
            MaterialFavoriteButton favoriteButton;

            ScheduleEventViewHolder(View view) {
                super(view);
                this.line = view.findViewById(R.id.schedule_item_line);
                this.time = view.findViewById(R.id.schedule_item_time);
                this.header = view.findViewById(R.id.schedule_item_header);
                this.eventype = view.findViewById(R.id.schedule_item_eventype);
                this.readmore = view.findViewById(R.id.shedule_item_readmore);
                this.favoriteButton = view.findViewById(R.id.favorite_button);
            }

        }

        /**
         * Clase para mostrar los bloques donde inicia cada actividad
         */
        class ScheduleBlockViewHolder extends RecyclerView.ViewHolder {

            TextView time;

            ScheduleBlockViewHolder(View view) {
                super(view);
                this.time = view.findViewById(R.id.schedule_block_time);
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                int margin = (int) getResources().getDimension(R.dimen.space_big);
                lp.setMargins(margin, margin, margin, 0);
                view.setLayoutParams(lp);
            }

        }


    }

}