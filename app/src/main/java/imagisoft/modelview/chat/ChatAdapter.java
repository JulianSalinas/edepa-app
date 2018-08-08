package imagisoft.modelview.chat;

import java.util.List;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import imagisoft.misc.DateConverter;
import imagisoft.model.Message;
import imagisoft.misc.ColorConverter;
import imagisoft.misc.MaterialGenerator;

import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainNavigation;
import imagisoft.modelview.custom.RecyclerAdapter;

import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SelectableHolder;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;


public abstract class ChatAdapter extends RecyclerAdapter {

    /**
     * Constantes paras escoger el tipo de vista
     */
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    /**
     * El modo de borrado es activado en
     * {@link #startRemoveMode()}
     */
    private ActionMode removeMode;

    /**
     * Fragmento que hace uso de este adaptador
     * @see ChatFragment
     */
    protected ChatFragment chatFragment;

    /**
     * Objetos del modelo que serán adaptados visualmente
     * Puede ser un mensaje del usuario activo o de otro usuario
     */
    protected ArrayList<Message> msgs;

    /**
     * Sirve para colorear el nombre de una persona según sus
     * iniciales
     */
    private MaterialGenerator materialGenerator;

    /**
     * Usado para borrar mensajes en
     * {@link #removeModeCallback}
     */
    private MultiSelector multiSelector = new MultiSelector();

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return msgs.size();
    }

    /**
     * Constructor
     * @param chatFragment: Vista que hace uso de este adaptador
     */
    public ChatAdapter(ChatFragment chatFragment){
        this.msgs = new ArrayList<>();
        this.chatFragment = chatFragment;
        this.materialGenerator = new MaterialGenerator(chatFragment.getActivity());
    }

    /**
     * Se añade un nuevo mensaje en la lista, además de un separador
     * de fechas si es necesario
     * @param msg Mensaje por añadir
     */
    public void addMessage(Message msg){
        msgs.add(msg);
        notifyItemInserted(msgs.size()-1);
        chatFragment.scrollIfWaitingResponse();
    }

    /**
     * Se utiliza cuando la propiedad de algún mensaje ha
     * cambiado de forma externa del adaptador
     * @param msg Mensaje por cambiar
     */
    public void changeMessage(Message msg){
        int index = msgs.indexOf(msg);
        if (index != -1) {
            msgs.set(index, msg);
            notifyItemChanged(index);
        }
    }

    /**
     * Se remueve un mensaje del adaptador
     * Al removerse se debe actualizar el mensaje siguiente, en caso
     * de que sea necesario agregar una marca de tiempo
     * @param msg Mensaje a eliminar
     */
    public void removeMessage(Message msg){
        int index = msgs.indexOf(msg);
        if (index != -1) {
            msgs.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, 2);
        }
    }

    /**
     * @param msg Mensaje
     * @return True si el mensaje fue enviado por el usuario actual
     */
    private boolean isFromCurrentUser(imagisoft.model.Message msg){
        return chatFragment.isFromCurrentUser(msg);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public int getItemViewType(int position) {
        Message msg = msgs.get(position);
        return isFromCurrentUser(msg) ? RIGHT : LEFT;
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public ChatItem onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == LEFT ?
                R.layout.chat_left :
                R.layout.chat_right;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == LEFT ?
                new ChatLeft(view) :
                new ChatRight(view);

    }

    /**
     * {@inheritDoc}
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChatItem) holder).bind();
    }

    /**
     * Atiende los eventos generados en el modo
     * {@link #removeMode}
     */
    private ActionMode.Callback removeModeCallback =
    new ModalMultiSelectorCallback(multiSelector) {

        /**
         * {@inheritDoc}
         * Agrega el icono de borrar
         */
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            super.onCreateActionMode(mode, menu);
            menu.add("Delete").setIcon(R.drawable.ic_delete);
            return true;
        }

        /**
         * {@inheritDoc}
         * Se eliminan todos los mensajes seleccionados
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for(Integer index : multiSelector.getSelectedPositions())
                removeMessage(msgs.get(index));
            multiSelector.clearSelections();
            mode.finish();
            return true;
        }

        /**
         * {@inheritDoc}
         * Se de-seleccionan todos los mensajes
         */
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            multiSelector.clearSelections();
        }

    };

    /**
     * Coloca el mensaje "n seleccionados" en la toolbar
     */
    public void setSelectedItemsText(){
        if(removeMode != null) {
            Integer nSelected = multiSelector.getSelectedPositions().size();
            String selectedText = chatFragment.getString(nSelected == 1 ?
                    R.string.text_selected_single : R.string.text_selected);
            removeMode.setTitle(nSelected.toString() + " " + selectedText);
        }
    }

    /**
     * Coloca botón de borrar en la toolbar mediante
     * {@link #removeModeCallback}
     */
    public void startRemoveMode(){
        MainNavigation activity = chatFragment.getMainActivity();
        activity.hideKeyboard();
        removeMode = activity.startSupportActionMode(removeModeCallback);
        int color = activity.getResources().getColor(R.color.app_accent);
        color = ColorConverter.darken(color);
        activity.getWindow().setStatusBarColor(color);
    }

    /**
     * Unidad que representa un elemento en el recyclerView
     */
    protected abstract class ChatItem extends RecyclerView.ViewHolder {

        /**
         * Posición del mensaje
         * Se asigna su valor en {@link #bind()}
         */
        int pos = -1;

        /**
         * Se asigna su valor en {@link #bind()}
         */
        Message msg = null;

        public ChatItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Asigna los valores pos y msg
         * Enlanza todos los componentes visuales
         */
        public void bind(){
            pos = getAdapterPosition();
            msg = msgs.get(pos);
        }

    }

    /**
     * Contiene el item que separa los mensajes por fechas
     */
    protected class ChatTimestamp extends ChatItem {

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
        public void bind() {

            super.bind();
            if (pos == 0) chatTimestamp.setVisibility(View.VISIBLE);

            else {
                Message lastMsg = msgs.get(pos - 1);
                String msgDate = DateConverter.extractDate(msg.getTime());
                String lastMsgDate = DateConverter.extractDate(lastMsg.getTime());
                chatTimestamp.setVisibility(msgDate.equals(lastMsgDate) ? View.GONE : View.VISIBLE);
            }

            if (chatTimestamp.getVisibility() == View.VISIBLE){
                long date = msg.getTime();
                boolean isToday = DateUtils.isToday(date);
                if (isToday) chatTimestampText.setText(R.string.text_today);
                else chatTimestampText.setText(DateConverter.extractDate(date));
            }

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

        public ChatLeft(View itemView) {
            super(itemView);
        }

        /**
         * {@inheritDoc}
         * Se colocan los elementos del mensaje
         */
        @Override
        public void bind() {

            super.bind();
            int color = materialGenerator.getColor(msg.getUsername());

            msgUsername.setTextColor(color);
            msgUsername.setText(msg.getUsername());
            msgUsername.setVisibility(View.VISIBLE);

            String time = DateConverter.extractTime(msg.getTime());
            msgTimeDescription.setText(time.toUpperCase());

            msgContent.setText(msg.getContent());
            Linkify.addLinks(msgContent, Linkify.ALL);
        }
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
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
        public void bind() {
            super.bind();
            setDeliveredIcon();
            msgUsername.setVisibility(View.GONE);
            multiSelector.bindHolder(this, pos, getItemId());
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        /**
         * Si se envía un mensaje en modo offline, el mensaje
         * queda almacenado localmente y se muestra el icono de espera
         */
        private void setDeliveredIcon() {
            int res = msg.isDelivered() ?
                    R.drawable.ic_check_circle :
                    R.drawable.ic_waiting;
            msgDelivered.setImageResource(res);
        }

        /**
         * {@inheritDoc}
         * Al entrar en el modo de selección, al hacer click, el mensaje
         * se agrega a la selección
         */
        @Override
        public void onClick(View v) {
            if (msg != null && multiSelector.tapSelection(this)){
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}