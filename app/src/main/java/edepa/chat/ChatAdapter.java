package edepa.chat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SelectableHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.app.MainActivity;
import edepa.app.NavigationActivity;
import edepa.cloud.CloudChat;
import edepa.custom.PhotoFragment;
import edepa.custom.RecyclerAdapter;
import edepa.minilibs.ColorConverter;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.RegexSearcher;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.TimeConverter;
import edepa.model.Message;
import edepa.model.Preview;
import edepa.modelview.R;
import edepa.previews.ChatPreview;


public class ChatAdapter
        extends RecyclerAdapter implements CloudChat.Callbacks {

    /**
     * Constantes paras escoger el tipo de vista
     */
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    /**
     * Contexto donde se hace uso de este adaptador
     * Se pasa por párametro en el constructor
     */
    protected Context context;

    /**
     * El modo de borrado es activado en {@link #startRemoveMode()}
     */
    private ActionMode removeMode;
    private ActionMode.Callback removeModeCallback;

    /**
     * Usado para borrar mensajes en {@link #removeModeCallback}
     */
    private MultiSelector multiSelector;

    /**
     * Sirve para colorear el nombre de una persona según sus iniciales
     */
    private ColorGenerator colorGenerator;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<Message> messages;

    /**
     * @return Cantidad de mensajes en el adaptador
     */
    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * Constructor
     */
    public ChatAdapter(Context context){
        this.context = context;
        this.messages = new ArrayList<>();
        this.multiSelector = new MultiSelector();
        this.colorGenerator = new ColorGenerator(context);
        this.removeModeCallback = getRemoveModeCallback();
    }

    /**
     * Se agrega un nuevo mensaje al final de la lista
     * @param msg Mensaje por agregar
     */
    @Override
    public void addMessage(Message msg){
        messages.add(msg);
        notifyItemInserted(messages.size()-1);
    }

    /**
     * Se utiliza cuando la propiedad de algún mensaje ha
     * cambiado de forma externa del adaptador
     * @param msg Mensaje por cambiar
     */
    @Override
    public void changeMessage(Message msg){
        int index = messages.indexOf(msg);
        if (index != -1) {
            messages.set(index, msg);
            notifyItemChanged(index);
        }
    }

    /**
     * Se remueve un mensaje del adaptador
     * Al removerse se debe actualizar el mensaje siguiente, en caso
     * de que sea necesario agregar una marca de tiempo
     * @param msg Mensaje a eliminar
     */
    @Override
    public void removeMessage(Message msg){
        int index = messages.indexOf(msg);
        if (index != -1) {
            messages.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, 2);
        }
    }

    /**
     * @return #RIGHT si el mensaje es del usuario
     */
    @Override
    public int getItemViewType(int position) {
        Message msg = messages.get(position);
        return msg.getFromCurrentUser() ? RIGHT : LEFT;
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public ChatItem onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == LEFT ? R.layout.chat_left : R.layout.chat_right;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return viewType == LEFT ? new ChatLeft(view) : new ChatRight(view);
    }

    /**
     * @param position NO USAR, esta variable no tiene valor fijo.
     * Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(holder.getAdapterPosition());
        ((ChatItem) holder).bind(message);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((ChatItem) holder).unbind();
        super.onViewDetachedFromWindow(holder);
    }

    /**
     * Atiende los eventos generados en el modo {@link #removeMode}
     */
    private ModalMultiSelectorCallback getRemoveModeCallback() {
        return new ModalMultiSelectorCallback(multiSelector) {

            /**
             * Se agrega el icono de borrar
             */
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                super.onCreateActionMode(mode, menu);
                String deleteText = context.getString(R.string.text_delete);
                menu.add(deleteText).setIcon(R.drawable.ic_delete);
                return true;
            }

            /**
             * Se eliminan todos los mensajes seleccionados con #multiSelector
             */
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                for (Integer index : multiSelector.getSelectedPositions())
                    CloudChat.removeMessage(messages.get(index));
                multiSelector.clearSelections();
                mode.finish();
                return true;
            }

            /**
             * Se de-seleccionan todos los mensajes del #multiSelector
             */
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                super.onDestroyActionMode(actionMode);
                multiSelector.clearSelections();
            }

        };

    }


    /**
     * Coloca el mensaje "n seleccionados" en la toolbar
     */
    public void setSelectedItemsText(){
        if(removeMode != null) {
            Integer nSelected = multiSelector.getSelectedPositions().size();
            String selectedText = context.getString(nSelected == 1 ?
                    R.string.text_selected_single : R.string.text_selected);
            removeMode.setTitle(nSelected.toString() + " " + selectedText);
        }
    }

    /**
     * Coloca botón de borrar en la toolbar mediante
     * {@link #removeModeCallback}
     */
    public void startRemoveMode(){
        if (context instanceof NavigationActivity) {
            NavigationActivity activity = (NavigationActivity) context;
            activity.hideKeyboard();
            removeMode = activity.startSupportActionMode(removeModeCallback);
            int color = activity.getResources().getColor(R.color.app_accent);
            color = ColorConverter.darken(color);
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * Unidad que representa un elemento en el recyclerView
     */
    protected abstract class ChatItem extends ChatPreview {

        @BindView(R.id.link_preview)
        View linkPreview;

        /**
         * Posición del mensaje
         * Se asigna su valor en {@link #bind(Message)}
         */
        int position = -1;

        /**
         * Constructor
         * @param itemView Vista previamente creada
         */
        public ChatItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Asigna los valores position y msg
         * Enlanza todos los componentes visuales
         */
        public void bind(Message message){
            this.message = message;
            this.position = getAdapterPosition();
        }

        /**
         * Coloca los eventos correspondientes al tocar
         * la imagen
         */
        @OnClick(R.id.preview_item)
        public void onPreviewItemClick(){
            if (message.getPreview() != null){
                Preview preview = message.getPreview();
                String url = preview.getUrl();
                openUrl(url);
            }
        }

        @OnClick(R.id.preview_image)
        public void onPreviewImageClick(){
            if (message.getPreview() != null){
                Preview preview = message.getPreview();
                openImage(preview.getUrl());
            }
        }

        /**
         * Abre una imagen en un fragmento aparte
         * @param imageUrl: Url de la imagen
         */
        public void openImage(String imageUrl){
            String domain = RegexSearcher.findDomainFromUrl(imageUrl);
            Fragment imageFragment = PhotoFragment.newInstance(domain, imageUrl);
            if(context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
                activity.setFragmentOnScreen(imageFragment, message.getKey());
            }
        }

        /**
         * Abre en el explorador una URL
         * @param url: Url que el navegador debe abrir
         */
        public void openUrl(String url){
            try{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if(context instanceof MainActivity) {
                    MainActivity activity = (MainActivity) context;
                    activity.startActivity(intent);
                }
            }
            catch (Exception e){
                new DialogFancy.Builder()
                        .setContext(context)
                        .setStatus(DialogFancy.ERROR)
                        .setTitle(R.string.text_invalid_link)
                        .setContent(R.string.text_invalid_link_content)
                        .build().show();
            }
        }

    }

    /**
     * Contiene el item que separa los mensajes por fechas
     */
    protected abstract class ChatTimestamp extends ChatItem {

        @BindView(R.id.chat_timestamp)
        View chatTimestamp;

        @BindView(R.id.chat_timestamp_text)
        TextView chatTimestampText;

        public ChatTimestamp(View itemView) {
            super(itemView);
        }

        /**
         * Si el mensaje anterior ya tiene la fecha no es
         * necesario colocarla en el siguiente mensaje si tiene la misma
         */
        @Override
        public void bind(Message message) {
            super.bind(message);
            if (position == 0) chatTimestamp.setVisibility(VISIBLE);
            else bindTimestampVisibility();
            boolean visible = chatTimestamp.getVisibility() == VISIBLE;
            if (visible) bindVisibleTimestamp();
        }

        /**
         * Hace visible marca de tiempo si el mensaje anterior no la tiene
         */
        public void bindTimestampVisibility(){
            Message lastMsg = messages.get(position - 1);
            String msgDate = TimeConverter.extractDate(message.getTime());
            String lastMsgDate = TimeConverter.extractDate(lastMsg.getTime());
            int visibility = msgDate.equals(lastMsgDate) ? GONE : VISIBLE;
            chatTimestamp.setVisibility(visibility);
        }

        /**
         * Coloca la fecha de los mensajes
         */
        public void bindVisibleTimestamp(){
            long date = message.getTime();
            boolean isToday = DateUtils.isToday(date);
            if (isToday) chatTimestampText.setText(R.string.text_today);
            else chatTimestampText.setText(TimeConverter.extractDate(date));
        }

    }

    /**
     * Representa los mensajes de otros usuarios
     */
    protected class ChatLeft extends ChatTimestamp {

        @BindView(R.id.chat_msg_content)
        TextView msgContent;

        @BindView(R.id.chat_msg_username)
        TextView msgUsername;

        @BindView(R.id.chat_msg_time_description)
        TextView msgTimeDescription;

        private URLSpan [] urls;

        public ChatLeft(View itemView) {
            super(itemView);
        }

        /**
         * {@inheritDoc}
         * Se colocan los elementos del mensaje
         */
        @Override
        public void bind(Message message) {
            super.bind(message);
            bindUsername();
            bindContent();
            bindTimeDescription();
            bindLinkPreview();

            // Arreglo de Bug con wrap_content
            msgContent.requestLayout();
        }

        /**
         * Coloca el nombre del usuario y lo pone de accent
         */
        public void bindUsername(){
            int color = colorGenerator.getColor(message.getUsername());
            msgUsername.setTextColor(color);
            msgUsername.setText(message.getUsername());
            msgUsername.setVisibility(VISIBLE);
        }

        /**
         * Coloca el contenido del mensaje
         */
        public void bindContent(){
            String content = message.getContent();
            boolean visible = content != null && !content.isEmpty();
            msgContent.setVisibility(visible ? VISIBLE : GONE);
            if(visible) {
                msgContent.setText(
                        TextHighlighter.decodeSpannables(message.getContent()));
                Linkify.addLinks(msgContent, Linkify.ALL);
                urls = msgContent.getUrls();
            }
        }

        /**
         * Coloca la hora a la que se envió el mensaje
         */
        public void bindTimeDescription(){
            String time = TimeConverter.extractTime(message.getTime());
            msgTimeDescription.setText(time.toUpperCase());
        }

        /**
         * Coloca una preview de la url en caso de que exista url
         * y la noticia no tenga una imagen definida
         */
        public void bindLinkPreview() {
            if (message.getPreview() != null){
                linkPreview.setVisibility(VISIBLE);
                bindPreview(message.getPreview());
            }
            else if (urls != null && urls.length > 0){
                linkPreview.setVisibility(VISIBLE);
                bindUrl(urls[0].getURL());
            }
            else linkPreview.setVisibility(GONE);
        }

    }

    /**
     * Clase para enlazar los mensajes que pertenece al usuario
     */
    protected class ChatRight extends ChatLeft
            implements View.OnClickListener, View.OnLongClickListener, SelectableHolder {

        @BindView(R.id.chat_msg_delivered)
        ImageView msgDelivered;

        boolean isSelectable = false;

        @Override
        public boolean isSelectable() {
            return isSelectable;
        }

        @Override
        public void setSelectable(boolean isSelectable) {
             this.isSelectable = isSelectable;
        }

        @Override
        public boolean isActivated() {
            return itemView.isActivated();
        }

        @Override
        public void setActivated(boolean isActivated) {
            itemView.setActivated(isActivated);
        }

        public ChatRight(View view) {
            super(view);
        }

        /**
         * {@inheritDoc}
         * Los mensajes de la derecha son del usuario actual, por tanto,
         * el usuario los puede eliminar y se deben colocar los listeners
         */
        public void bind(Message message) {
            super.bind(message);
            bindDeliveredIcon();
            bindClickListeners();
            msgUsername.setVisibility(GONE);
            multiSelector.bindHolder(this, position, getItemId());
        }

        /**
         * Coloca los eventos de los clicks
         */
        public void bindClickListeners(){
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        /**
         * Si se envía un mensaje en modo offline, el mensaje
         * queda almacenado localmente y se muestra el icono de espera
         */
        private void bindDeliveredIcon() {
            int resource = message.isDelivered() ?
                    R.drawable.ic_check :
                    R.drawable.ic_waiting_clock;
            msgDelivered.setImageResource(resource);
        }

        /**
         * {@inheritDoc}
         * Al entrar en el modo de selección, al hacer click, el mensaje
         * se agrega a la selección
         */
        @Override
        public void onClick(View v) {
            if (message != null && multiSelector.tapSelection(this)){
                List<Integer> selected = multiSelector.getSelectedPositions();
                if(removeMode != null && selected.isEmpty())
                    removeMode.finish();
                else setSelectedItemsText();
            }
        }

        /**
         * {@inheritDoc}
         * Se entra en modo selección y se marca el mensaje
         */
        @Override
        public boolean onLongClick(View v) {
            if (!multiSelector.isSelectable()) {
                startRemoveMode();
                multiSelector.setSelectable(true);
                multiSelector.setSelected(this, true);
                setSelectedItemsText();
                return true;
            }   return false;
        }
    }

}