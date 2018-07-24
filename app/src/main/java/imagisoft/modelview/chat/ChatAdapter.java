package imagisoft.modelview.chat;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SelectableHolder;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.misc.ColorConverter;
import imagisoft.misc.DateConverter;
import imagisoft.misc.MaterialGenerator;
import imagisoft.model.Message;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.ActivityNavigation;


public abstract class ChatAdapter
        extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    /**
     * Constantes paras escoger el tipo de vista que se colocará
     */
    private final int LEFT = 0;
    private final int RIGHT = 1;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<Message> msgs;

    /**
     * Fragmento que hace uso de este adaptador
     */
    protected ChatFragment chatFragment;

    /**
     * Sirve para colorear el nombre de la persona según sus iniciales
     */
    private MaterialGenerator materialGenerator;


    private MultiSelector multiSelector = new MultiSelector();

    private ActionMode removeMode;

    private ActionMode.Callback removeModeCallback =
            new ModalMultiSelectorCallback(multiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            super.onCreateActionMode(mode, menu);
            menu.add("Delete").setIcon(R.drawable.ic_delete);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for(Integer index : multiSelector.getSelectedPositions())
                removeMessage(msgs.get(index));
            multiSelector.clearSelections();
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            multiSelector.clearSelections();
        }

    };

    public void setSelectedItemsText(){
        if(removeMode != null) {
            Integer nSelected = multiSelector.getSelectedPositions().size();
            String selectedText = chatFragment.getString(nSelected == 1 ?
                    R.string.text_selected_single : R.string.text_selected);
            removeMode.setTitle(nSelected.toString() + " " + selectedText);
        }
    }

    public abstract void removeMessage(Message msg);

//    public abstract void addMessage(Message msg);

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
     *  {@inheritDoc}
     */
    @Override
    public int getItemViewType(int position) {
        Message msg = msgs.get(position);
        return isFromCurrentUser(msg) ? RIGHT : LEFT;
    }

    /**
     * @param msg Mensaje
     * @return True si el mensaje fue enviado por el usuario actual
     */
    private boolean isFromCurrentUser(Message msg){
        String userUid = chatFragment.getUserid();
        return msg.getUserid().equals(userUid);
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == LEFT ?
                R.layout.chat_left_with_sep :
                R.layout.chat_right_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return new ChatViewHolder(view);

    }

    /**
     * {@inheritDoc}
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.bind();
    }

    /**
     * Coloca botón de borrar en la toolbar mediante
     * {@link #removeModeCallback}
     */
    public void startRemoveMode(){
        ActivityNavigation activity = chatFragment.getActivityCustom();
        activity.hideKeyboard();
        removeMode = activity.startSupportActionMode(removeModeCallback);
        int color = activity.getResources().getColor(R.color.app_accent);
        color = ColorConverter.darken(color);
        activity.getWindow().setStatusBarColor(color);
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    protected class ChatViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener, SelectableHolder {

        @BindView(R.id.msg_item)
        FrameLayout msgItem;

        @BindView(R.id.msg_username)
        TextView msgUsername;

        @BindView(R.id.msg_content)
        TextView msgContent;

        @BindView(R.id.msg_time_description)
        TextView msgTimeDescription;

        @BindView(R.id.chat_separator_text)
        TextView timeSeparatorText;

        @BindView(R.id.chat_separator_item)
        View timeSeparatorItem;

        /**
         * Posición del mensaje
         * Se asigna su valor en {@link #bind()}
         */
        int pos = -1;

        /**
         * Se asigna su valor en {@link #bind()}
         */
        Message msg = null;

        boolean isSelectable = false;

        public ChatViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind() {

            this.pos = getAdapterPosition();
            this.msg = msgs.get(pos);
            multiSelector.bindHolder(this, pos, getItemId());

            if (msg.getUserid().equals(chatFragment.getUserid()))
                msgUsername.setVisibility(View.GONE);

            else if (pos == 0)
                msgUsername.setVisibility(View.VISIBLE);

            else {
                Message upMsg = msgs.get(pos - 1);
                boolean isEqual = upMsg.getUserid().equals(msg.getUserid());
                msgUsername.setVisibility(isEqual ? View.GONE : View.VISIBLE);
            }

            if (msgUsername.getVisibility() == View.VISIBLE) {
                int color = materialGenerator.getColor(msg.getUsername());
                msgUsername.setTextColor(color);
                msgUsername.setText(msg.getUsername());
            }

            String time = DateConverter.extractTime(msg.getTime());
            msgTimeDescription.setText(time);

            msgContent.setText(msg.getContent());
            Linkify.addLinks(msgContent, Linkify.ALL);

            if (pos == 0)
                timeSeparatorItem.setVisibility(View.VISIBLE);

            else {
                Message upMsg = msgs.get(pos - 1);
                String upDate = DateConverter.extractDate(upMsg.getTime());
                String currentDate = DateConverter.extractDate(msg.getTime());
                boolean equals = upDate.equals(currentDate);
                timeSeparatorItem.setVisibility(equals ? View.GONE : View.VISIBLE);
            }

            if (timeSeparatorItem.getVisibility() == View.VISIBLE) {
                long date = msg.getTime();
                boolean isToday = DateUtils.isToday(date);
                if (isToday) timeSeparatorText.setText(R.string.text_today);
                else timeSeparatorText.setText(DateConverter.extractDate(date));
            }

            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (msg != null && multiSelector.tapSelection(this)){
                List<Integer> selected = multiSelector.getSelectedPositions();
                if(removeMode != null && selected.isEmpty())
                    removeMode.finish();
                else setSelectedItemsText();
                Log.i(toString(), "onClick()");
            }
        }

        @Override
        public boolean onLongClick(View v) {
            startRemoveMode();
            multiSelector.setSelected(this, true);
            setSelectedItemsText();
            Log.i(toString(), "onLongClick()");
            return true;
        }

        @Override
        public void setSelectable(boolean selectable) {
            // Requerido
        }

        @Override
        public boolean isSelectable() {
            return isSelectable;
        }

        @Override
        public void setActivated(boolean isActivated) {
            itemView.setActivated(isActivated);
        }

        @Override
        public boolean isActivated() {
            return itemView.isActivated();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}