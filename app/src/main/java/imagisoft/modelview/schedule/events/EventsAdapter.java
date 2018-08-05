package imagisoft.modelview.schedule.events;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;
import java.util.ArrayList;

import imagisoft.misc.DateConverter;
import imagisoft.model.Cloud;
import imagisoft.modelview.R;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.activity.MainNavigation;
import imagisoft.modelview.interfaces.IEventsSubject;
import imagisoft.modelview.views.RecyclerAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public abstract class EventsAdapter
        extends RecyclerAdapter implements IEventsSubject {

    /**
     * Variables par escoger el tipo de vista que se colocará
     * @see #getItemViewType(int)
     */
    protected int SINGLE = 0;
    protected int WITH_SEPARATOR = 1;

    private Context context;

    protected List<String> favorites;

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
    @Override
    public void addEvent(ScheduleEvent event){
        int index = events.indexOf(event);
        if(index == -1){
            String key = event.getKey();
            index = findIndexToAddEvent(event);
            events.add(index, event);
            setFavoriteEvent(key, favorites.contains(key));
            notifyItemInserted(index);
        }
    }

    private int findIndexToAddEvent(ScheduleEvent event){
        int index = 0;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getStart() <= event.getStart() ) index += 1;
            else break;
        }   return index;
    }

    /**
     * Se utiliza cuando la propiedad de algún mensaje ha
     * cambiado de forma externa del adaptador
     * @param event Mensaje por cambiar
     */
    @Override
    public void changeEvent(ScheduleEvent event){
        int index = events.indexOf(event);
        if (index != -1) {
            String key = event.getKey();
            events.set(index, event);
            setFavoriteEvent(key, favorites.contains(key));
            notifyItemChanged(index);
        }
    }

    /**
     * Se remueve un mensaje del adaptador
     * Al removerse se debe actualizar el mensaje siguiente, en caso
     * de que sea necesario agregar una marca de tiempo
     */
    @Override
    public void removeEvent(ScheduleEvent event){
        int index = events.indexOf(event);
        if (index != -1) {
            events.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void addFavorite(String eventKey) {
        if(!favorites.contains(eventKey)) {
            favorites.add(eventKey);
            setFavoriteEvent(eventKey, true);
        }
    }

    @Override
    public void removeFavorite(String eventKey) {
        if(favorites.contains(eventKey)) {
            favorites.remove(eventKey);
            setFavoriteEvent(eventKey, false);
        }
    }

    private void setFavoriteEvent(String eventKey, boolean isFavorite){
        int index = getFavoriteIndex(eventKey);
        if(index != -1) events.get(index).setFavorite(isFavorite);
    }

    private int getFavoriteIndex(String eventKey){
        ScheduleEvent temp = new ScheduleEvent();
        temp.setKey(eventKey);
        return events.indexOf(temp);
    }

    /**
     * Constructor
     */
    public EventsAdapter(Context context) {
        super();
        this.context = context;
        this.events = new ArrayList<>();
        this.favorites = new ArrayList<>();
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

    class EventItem extends RecyclerView.ViewHolder {

        /**
         * Posición del mensaje
         * Se asigna su valor en {@link #bind()}
         */
        int position = -1;

        /**
         * Se asigna su valor en {@link #bind()}
         */
        ScheduleEvent event = null;

        public EventItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Asigna los valores position y msg
         * Enlanza todos los componentes visuales
         */
        public void bind(){
            position = getAdapterPosition();
            event = events.get(position);
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

        public SingleEventItem(View itemView) {
            super(itemView);
        }

        public void bind(){
            super.bind();

            header.setText(event.getTitle());
            eventype.setText(event.getEventype().toString());

            String description = DateConverter.getBlockString(
                    context, event.getStart(), event.getEnd());

            time_description.setText(description);

            readmore.setOnClickListener(v ->
                    ((MainNavigation) context)
                    .openDetails(event.getKey()));

            favoriteButton.setLiked(event.isFavorite());
            favoriteButton.setOnLikeListener(this);

            setEmphasis();
        }

        /**
         * TODO Colocar aquí con base a preferencias
         */
        public void setEmphasis(){
            Resources res = context.getResources();
            int color = res.getColor(event.getEventype().getColor());
            line.setBackgroundColor(color);
            readmore.setTextColor(color);
        }

        @Override
        public void liked(LikeButton likeButton) {
            String uid = Cloud.getInstance().getAuth().getUid();
            if (uid != null) Cloud.getInstance()
                    .getReference(Cloud.FAVORITES)
                    .child(uid).child(event.getKey())
                    .setValue(event.getDate());
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            String uid = Cloud.getInstance().getAuth().getUid();
            if (uid != null) Cloud.getInstance()
                    .getReference(Cloud.FAVORITES)
                    .child(uid).child(event.getKey())
                    .removeValue();
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
            String date = DateConverter
                    .getBlockString(context, event.getStart());
            scheduleSeparator.setText(date);
        }

    }

}
