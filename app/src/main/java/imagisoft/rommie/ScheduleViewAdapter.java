package imagisoft.rommie;

import java.util.ArrayList;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

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
    private ArrayList<ScheduleBlock> events;

    /**
     * Se necesita para obtener algunos strings
     */
    private Context context;

    /**
     * Se necesita para cambiar el fragmento
     */
    private ActivityMainNav activity;

    /**
     * Constructor de la vista donde se colocan los eventos
     */
    public ScheduleViewAdapter(ScheduleView scheduleView, ArrayList<ScheduleBlock> events){
        this.events = events;
        this.context = scheduleView.getContext();
        this.activity = (ActivityMainNav) scheduleView.getActivity();
    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     *  Obtiene si la vista es un bloque de hora una actividad
     */
    @Override
    public int getItemViewType(int position) {
        ScheduleBlock item = events.get(position);
        return (item instanceof ScheduleEvent) ?
                SCHEDULE_EVENT_VIEW_TYPE:
                SCHEDULE_BLOCK_VIEW_TYPE;
    }

    /**
     * Retorna la vista de bloque o evento según se necesite
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Vista para mostrar la hora inicial de un bloque de actividades
        if(viewType == SCHEDULE_BLOCK_VIEW_TYPE){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_block, parent, false);
            return new ScheduleBlockViewHolder(view);
        }

        // Vista para mostrar las actividades como tal
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_item, parent, false);
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

    /**
     * En caso que la vista a crear sea un evento
     * @param holder ScheduleEventViewHolder
     */
    public void onBindScheduleEventViewHolder(ScheduleEventViewHolder holder){

        // Items para extraer los datos y colocarlos en la vista
        ScheduleEvent event = (ScheduleEvent) events.get(holder.getAdapterPosition());

        // Forma el string para colocar la fecha de la actividad
        String range =  context.getResources().getString(R.string.text_from) + " " +
                        UDateConverter.extractTime(event.getStart()) + " " +
                        context.getResources().getString(R.string.text_to) + " " +
                        UDateConverter.extractTime(event.getEnd());

        // Rellana todos los espacios de la actividad
        holder.time.setText(range);
        holder.header.setText(event.getTitle());
        holder.eventype.setText(event.getEventype().toString());

        // Coloca el color acorde al tipo de actividad
        int colorResource = event.getEventype().getColor();
        holder.line.setBackgroundResource(colorResource);
        holder.readmore.setTextColor(context.getResources().getColor(colorResource));

        /*
        * Función ejecutada al presionar el botón "readmore" de una actividad
        * TODO: Pasar "item" a la "ScheduleDetail" para saber que información mostrar
        */
        holder.readmore.setOnClickListener(v ->  {
            activity.switchFragment(new ScheduleDetail());
        });

        /*
        * Función ejecutada al presionar la "estrellita" de una actividad
        * TODO: Aquí va la función de agregar a favoritos
        */
        holder.favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
            int text = favorite ?
                    R.string.text_marked_as_favorite :
                    R.string.text_unmarked_as_favorite;
            activity.showStatusMessage(context.getResources().getString(text));
        });

    }

    public void onBindScheduleBlockViewHolder(ScheduleBlockViewHolder holder){

        // Items para extraer los datos y colocarlos en la vista
        ScheduleBlock block = events.get(holder.getAdapterPosition());

        // Forma el string para colocar la fecha de la actividad
        String range =  context.getResources().getString(R.string.text_from) + " " +
                        UDateConverter.extractTime(block.getStart()) + " " +
                        context.getResources().getString(R.string.text_to) + " " +
                        UDateConverter.extractTime(block.getEnd());

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
        }

    }

}
