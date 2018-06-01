package imagisoft.modelview;

import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;
import com.like.LikeButton;
import com.like.OnLikeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.model.FavoriteList;
import imagisoft.model.ScheduleEvent;
import imagisoft.misc.DateConverter;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class EventsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Variables par escoger el tipo de vista que se colocará
     */
    protected int SINGLE = 0;
    protected int WITH_SEPARATOR = 1;

    /**
     * PagerFragment al que se debe colocar este adaptador
     */
    protected ActivityFragment fragment;

    /**
     * Necesaria para saber a cúal poner la estrella
     */
    protected FavoriteList favoriteList;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected List<ScheduleEvent> events;

    /**
     * Con este constructor se deben poner los eventos posteriormente
     */
    public EventsAdapter(ActivityFragment fragment) {
        super();
        this.fragment = fragment;
        this.events = new ArrayList<>();
        this.favoriteList = FavoriteList.getInstance();
    }

    @Override public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == SINGLE ?
                R.layout.schedule_item :
                R.layout.schedule_item_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == SINGLE ?
                new ScheduleEventVH(view):
                new ScheduleEventWithSepVH(view);

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
     *  TODO: Mejorar
     */
    @Override
    public int getItemViewType(int position) {

        ScheduleEvent item = events.get(position);

        if(position == 0)
            return WITH_SEPARATOR;

        else {
            ScheduleEvent upItem = events.get(position - 1);
            long itemStart = item.getStart();
            long upItemStart = item.getStart();
            long upItemEnd = upItem.getEnd();

            long diff = Math.abs(upItemEnd - itemStart);

            if (upItemStart <= itemStart && itemStart < upItemEnd)
                return SINGLE;

            if(diff >= 20 * 60 * 1000)
                return WITH_SEPARATOR;

            else return SINGLE;
        }

    }

    /**
     * Se enlazan los componentes y se agregan funciones a cada uno
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == SINGLE)
            onBindScheduleEventVH((ScheduleEventVH) holder);
        else
            onBindScheduleEventWithSepVH((ScheduleEventWithSepVH) holder);

    }

    /**
     * Coloca la hora en un encabezado de bloque de eventos
     */
    public void onBindScheduleEventWithSepVH(ScheduleEventWithSepVH holder){
        int position = holder.getAdapterPosition();
        final ScheduleEvent event = events.get(position);
        holder.scheduleSeparator.setText(getDateAsString(event.getStart()));
        onBindScheduleEventVH(holder);
    }

    /**
     * En caso que la vista a crear sea un evento
     */
    public void onBindScheduleEventVH(ScheduleEventVH holder){

        final int position = holder.getAdapterPosition();
        final ScheduleEvent event = events.get(position);

        bindInformation(holder, event);
        bindEmphasisColor(holder, event);

        /*
        * Función ejecutada al presionar el botón "readmore" de una actividad
        */
        holder.readmore.setOnClickListener(v ->
//             fragment.switchFragment(ScheduleDetailPager.newInstance(event))
                fragment.switchFragment(ScheduleDetail.newInstance(event))
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
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                favoriteList.removeEvent(event);
                Log.i("FavoriteRemoved::", event.getTitle());
            }

        });

    }

    /**
     * Toma la fecha inicio y crea un string para identificar el inicio
     * de un bloque de eventos
     * @param start: Inicio del evento que abre el bloque
     * @return Fechas como un string que se debe mostrar en la UI
     */
    protected String getDateAsString(long start){
        Activity activity = fragment.activity;
        return  activity.getResources().getString(R.string.text_block) + " " +
                DateConverter.extractTime(start);
    }

    protected String getDateAsString(ScheduleEvent event){
        Activity activity = fragment.activity;
        return  activity.getResources().getString(R.string.text_from) + " " +
                DateConverter.extractTime(event.getStart()) + " " +
                activity.getResources().getString(R.string.text_to) + " " +
                DateConverter.extractTime(event.getEnd());
    }

    /**
     * Coloca la información de evento en la vista
     */
    private void bindInformation(ScheduleEventVH holder, ScheduleEvent event){
        holder.header.setText(event.getTitle());
        holder.eventype.setText(event.getEventype().toString());
        holder.time_description.setText(this.getDateAsString(event));
    }

    /**
     * Coloca el color acorde con el tipo de actividad
     */
    private void bindEmphasisColor(ScheduleEventVH holder, ScheduleEvent event) {
        Activity activity = fragment.activity;
        int colorResource = event.getEventype().getColor();
        int color = activity.getResources().getColor(colorResource);
        holder.line.setBackgroundColor(color);
        holder.readmore.setTextColor(color);
    }

    /**
     * Clase para mostrar los bloques donde inicia cada actividad
     */
    class ScheduleEventWithSepVH extends ScheduleEventVH {

        @BindView(R.id.schedule_item_time)
        TextView scheduleSeparator;

        ScheduleEventWithSepVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    /**
     * Clase para enlzar cada uno de los componentes visuales
     */
    class ScheduleEventVH extends RecyclerView.ViewHolder {

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

        @BindView(R.id.time_descripcion)
        TextView time_description;

        ScheduleEventVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
