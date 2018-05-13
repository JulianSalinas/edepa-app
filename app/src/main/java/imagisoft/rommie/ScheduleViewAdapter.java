package imagisoft.rommie;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.ScheduleEventType;
import imagisoft.miscellaneous.DateConverter;

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
    private MainActivityFragment scheduleView;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private List<? extends ScheduleBlock> events;

    private List<? extends ScheduleBlock> eventsIndexedMirror;

    private final FavoriteList favoriteList = FavoriteList.getInstance();

    /**
     * Constructor de la vista donde se colocan los eventos
     */
    public ScheduleViewAdapter(MainActivityFragment scheduleView,
                               List<? extends ScheduleBlock> events){

        this.scheduleView = scheduleView;
        this.events = new ArrayList<>(events);
        this.eventsIndexedMirror = new ArrayList<>(events);

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

        final int position = holder.getAdapterPosition();

        final ScheduleEvent event = (ScheduleEvent) events.get(position);

        bindInformation(holder, event);
        bindEmphasisColor(holder, event);

        /*
        * Función ejecutada al presionar el botón "readmore" de una actividad
        */
        holder.readmore.setOnClickListener(v ->
             scheduleView.switchFragment(ScheduleDetailPager.newInstance(event))
        );

        /*
        * Se coloca la estrellita a los eventos que están en favoritos
        */
        holder.favoriteButton.setOnLikeListener(null);
        if(favoriteList.contains(event))
            holder.favoriteButton.setLiked(true);
        else
            holder.favoriteButton.setLiked(false);


        /*
        * Función ejecutada al presionar la "estrellita" de una actividad
        */
        holder.favoriteButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                favoriteList.addEvent(event);
                scheduleView.showStatusMessage(R.string.text_marked_as_favorite);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                favoriteList.removeEvent(event);
                scheduleView.showStatusMessage(R.string.text_unmarked_as_favorite);

                if(scheduleView instanceof ScheduleViewFavorites) {

                    int index = events.indexOf(event);

                    final ScheduleBlock before = events.get(index - 1);

                    final ScheduleBlock after =
                            events.size() - 1 >= index + 1 ? events.get(index + 1) : null;

                    events.remove(index);
                    notifyItemRemoved(index);

                    if(after == null && !(before instanceof ScheduleEvent)){
                        index = events.indexOf(before);
                        events.remove(index);
                        notifyItemRemoved(index);
                    }

                    else if (!(after instanceof ScheduleEvent) && !(before instanceof ScheduleEvent)){
                        index = events.indexOf(before);
                        events.remove(index);
                        notifyItemRemoved(index);
                    }

                    if(events.size() == 1){
                        events.remove(0);
                        notifyItemRemoved(0);
                    }

                }

            }

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
                DateConverter.extractTime(block.getStart()) + " " +
                activity.getResources().getString(R.string.text_to) + " " +
                DateConverter.extractTime(block.getEnd());

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
    private void bindEmphasisColor(ScheduleEventViewHolder holder, ScheduleEvent event){

        Activity activity = scheduleView.getActivity();
        assert activity != null;

        int colorResource = event.getEventype().getColor();
        holder.line.setBackgroundResource(colorResource);
        holder.readmore.setTextColor(activity.getResources().getColor(colorResource));

    }

    public void filter(ScheduleEventType type){

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

        @BindView(R.id.line)
        View line;

        @BindView(R.id.header)
        TextView header;

        @BindView(R.id.eventype)
        TextView eventype;

        @BindView(R.id.readmore)
        TextView readmore;

        @BindView(R.id.favorite_button)
        LikeButton favoriteButton;

        ScheduleEventViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
