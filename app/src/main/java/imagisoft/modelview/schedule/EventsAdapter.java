package imagisoft.modelview.schedule;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;
import java.util.ArrayList;

import imagisoft.modelview.R;
import imagisoft.model.FavoriteList;
import imagisoft.model.ScheduleEvent;
import imagisoft.misc.DateConverter;
import imagisoft.modelview.views.RecyclerAdapter;

import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public abstract class EventsAdapter extends RecyclerAdapter {

    /**
     * Variables par escoger el tipo de vista que se colocará
     * @see #getItemViewType(int)
     */
    protected int SINGLE = 0;
    protected int WITH_SEPARATOR = 1;

    /**
     * Fragment al que se debe colocar este adaptador
     */
    protected EventsFragment fragment;

    /**
     * Necesaria para saber donde poner la estrella
     */
    protected FavoriteList favoriteList;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected List<ScheduleEvent> events;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * Se añade un nuevo mensaje en la lista, además de un separador
     * de fechas si es necesario
     * @param event Mensaje por añadir
     */
    public void addEvent(ScheduleEvent event){
        events.add(event);
        notifyItemInserted(events.size()-1);
    }

    /**
     * Se utiliza cuando la propiedad de algún mensaje ha
     * cambiado de forma externa del adaptador
     * @param event Mensaje por cambiar
     */
    public void changeEvent(ScheduleEvent event){
        int index = events.indexOf(event);
        events.set(index, event);
        notifyItemChanged(index);
    }

    /**
     * Se remueve un mensaje del adaptador
     * Al removerse se debe actualizar el mensaje siguiente, en caso
     * de que sea necesario agregar una marca de tiempo
     * @param event Mensaje a eliminar
     */
    public void removeEvent(ScheduleEvent event){
        int index = events.indexOf(event);
        events.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * Constructor
     */
    public EventsAdapter(EventsFragment fragment) {
        super();
        this.fragment = fragment;
        this.events = new ArrayList<>();
        this.favoriteList = FavoriteList.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventItem onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == SINGLE ?
                R.layout.schedule_item :
                R.layout.schedule_item_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == SINGLE ?
                new SingleEventItem(view):
                new TitledEventItem(view);

    }

    /**
     *  Obtiene si la vista es un bloque de hora o una actividad
     */
    @Override
    public int getItemViewType(int position) {

        ScheduleEvent item = events.get(position);
        if(position == 0) return WITH_SEPARATOR;

        else {
            ScheduleEvent upItem = events.get(position - 1);
            long itemStart = item.getStart();
            long upItemStart = upItem.getStart();
            long upItemEnd = upItem.getEnd();

            return  Math.abs(upItemEnd - itemStart) < 20 * 60 * 1000 ||
                    upItemStart <= itemStart && itemStart < upItemEnd ?
                    SINGLE : WITH_SEPARATOR;
        }

    }

    /**
     * {@inheritDoc}
     * Se enlazan los componentes y se agregan funciones a cada uno
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((EventItem) holder).bind();
    }

    /**
     * Toma la fecha inicio y crea un string para identificar el inicio
     * de un bloque de eventos
     * @param start: Inicio del evento que abre el bloque
     * @return Fechas como un string que se debe mostrar en la UI
     */
    protected String getDateAsString(long start){
        Activity activity = fragment.getActivityCustom();
        return  activity.getResources().getString(R.string.text_block) + " " +
                DateConverter.extractTime(start);
    }

    protected String getDateAsString(ScheduleEvent event){
        Activity activity = fragment.getActivityCustom();
        return  activity.getResources().getString(R.string.text_from) + " " +
                DateConverter.extractTime(event.getStart()) + " " +
                activity.getResources().getString(R.string.text_to) + " " +
                DateConverter.extractTime(event.getEnd());
    }

    class EventItem extends RecyclerView.ViewHolder {

        /**
         * Posición del mensaje
         * Se asigna su valor en {@link #bind()}
         */
        int pos = -1;

        /**
         * Se asigna su valor en {@link #bind()}
         */
        ScheduleEvent event = null;

        public EventItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Asigna los valores pos y msg
         * Enlanza todos los componentes visuales
         */
        public void bind(){
            pos = getAdapterPosition();
            event = events.get(pos);
        }

    }

    /**
     * Clase para enlzar cada uno de los componentes visuales
     */
    class SingleEventItem extends EventItem implements OnLikeListener {

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

        public SingleEventItem(View view) {
            super(view);
        }

        public void bind(){
            super.bind();
            header.setText(event.getTitle());
            eventype.setText(event.getEventype().toString());
            time_description.setText(getDateAsString(event));

            int colorResource = event.getEventype().getColor();
            int color = fragment.getResources().getColor(colorResource);
            line.setBackgroundColor(color);
            readmore.setTextColor(color);

            /*
             * Función ejecutada al presionar el botón "readmore" de una actividad
             */
            readmore.setOnClickListener(v ->{}
//             fragment.setFragmentOnScreen(ScheduleDetailPager.newInstance(event))
//                fragment.setFragmentOnScreen(ScheduleDetail.newInstance(event))
            );

            favoriteButton.setLiked(favoriteList.contains(event));
            favoriteButton.setOnLikeListener(this);

        }

        @Override
        public void liked(LikeButton likeButton) {
            favoriteList.addEvent(event.getId());
            Log.i("liked: ", event.getTitle());
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            favoriteList.removeEvent(event.getId());
            Log.i("unLiked", event.getTitle());
        }

    }

    class TitledEventItem extends SingleEventItem {

        @BindView(R.id.schedule_item_time)
        TextView scheduleSeparator;

        public TitledEventItem(View view) {
            super(view);
        }

        @Override
        public void bind(){
            super.bind();
            String date = getDateAsString(event.getStart());
            scheduleSeparator.setText(date);
        }

    }

}
