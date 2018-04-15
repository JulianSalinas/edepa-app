package imagisoft.rommie;

import java.util.List;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UDateConverter;

import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;
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
     * PagerFragment al que se debe colocar este adaptador
     */
    private MainViewFragment scheduleView;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private List<? extends ScheduleBlock> events;

    /**
     * Constructor de la vista donde se colocan los eventos
     */
    public ScheduleViewAdapter(MainViewFragment scheduleView,
                               List<? extends ScheduleBlock> events){

        this.scheduleView = scheduleView;
        this.events = events;

    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     *  Obtiene si la vista es un bloque de hora o una actividad
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
     */
    public void onBindScheduleEventViewHolder(ScheduleEventViewHolder holder){

        int position = holder.getAdapterPosition();
        ScheduleEvent event = (ScheduleEvent) events.get(position);

        bindInformation(holder, event);
        bindColorEmphasis(holder, event);

        /*
        * Función ejecutada al presionar el botón "readmore" de una actividad
        */
        holder.readmore.setOnClickListener(v ->
             scheduleView.switchFragment(ScheduleDetail.newInstance(event))
        );

        /*
        * Se coloca la estrellita a los eventos que están en favoritos
        */
        FavoriteList favoriteList = FavoriteList.getInstance();
        if(favoriteList.getSortedEvents().contains(event))
            holder.favoriteButton.setFavorite(true);

        /*
        * Función ejecutada al presionar la "estrellita" de una actividad
        */
        holder.favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> {

            int text = favorite ?
                    R.string.text_marked_as_favorite :
                    R.string.text_unmarked_as_favorite;

            if(favorite)
                 favoriteList.addEvent(event);
            else favoriteList.removeEvent(event);

            String msg = scheduleView.getResources().getString(text);
            scheduleView.showStatusMessage(msg);

        });

    }

    /**
     * Coloca la hora en un encabezado de bloque de eventos
     */
    public void onBindScheduleBlockViewHolder(ScheduleBlockViewHolder holder){

        ScheduleBlock block = events.get(holder.getAdapterPosition());
        holder.time.setText(getDatesAsString(block));

    }

    /**
     * Toma la fecha inicio y fin del evento y las concatena y retorna como strings
     * @param block: Evento o bloque donde se toman las fechas
     * @return Fechas como un string que se debe mostrar en la UI
     */
    private String getDatesAsString(ScheduleBlock block){

        Activity activity = scheduleView.getActivity();
        assert activity != null;

        return  activity.getResources().getString(R.string.text_from) + " " +
                UDateConverter.extractTime(block.getStart()) + " " +
                activity.getResources().getString(R.string.text_to) + " " +
                UDateConverter.extractTime(block.getEnd());

    }

    /**
     * Coloca la información de evento en la vista
     */
    private void bindInformation(ScheduleEventViewHolder holder, ScheduleEvent event){

        holder.time.setText(getDatesAsString(event));
        holder.header.setText(event.getTitle());
        holder.eventype.setText(event.getEventype().toString());

    }

    /**
     * Coloca el color acorde con el tipo de actividad
     */
    private void bindColorEmphasis(ScheduleEventViewHolder holder, ScheduleEvent event){

        Activity activity = scheduleView.getActivity();
        assert activity != null;

        int colorResource = event.getEventype().getColor();
        holder.line.setBackgroundResource(colorResource);
        holder.readmore.setTextColor(activity.getResources().getColor(colorResource));

    }

    /**
     * Clase para mostrar los bloques donde inicia cada actividad
     */
    class ScheduleBlockViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        ScheduleBlockViewHolder(View view) {
            super(view);
            this.time = view.findViewById(R.id.schedule_item_time);
        }

    }

    /**
     * Clase para enlzar cada uno de los componentes visuales
     */
    class ScheduleEventViewHolder extends ScheduleBlockViewHolder {

        View line;
        TextView header;
        TextView eventype;
        TextView readmore;
        MaterialFavoriteButton favoriteButton;

        ScheduleEventViewHolder(View view) {

            super(view);
            this.line = view.findViewById(R.id.schedule_item_line);
            this.header = view.findViewById(R.id.schedule_item_header);
            this.eventype = view.findViewById(R.id.schedule_item_eventype);
            this.readmore = view.findViewById(R.id.schedule_item_readmore);
            this.favoriteButton = view.findViewById(R.id.favorite_button);

        }

    }

}
