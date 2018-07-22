package imagisoft.modelview;

import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.misc.DateConverter;
import imagisoft.misc.MaterialGenerator;
import imagisoft.model.Message;
import imagisoft.modelview.chat.ChatFragment;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

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

    private boolean multiSelect = false;
    private ArrayList<Message> selectedItems = new ArrayList<>();

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete").setIcon(android.R.drawable.ic_menu_delete);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (Message msg : selectedItems) {
                msgs.remove(msg);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };


    /**
     * @return Cantidad de vistas por crear
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
        String userUid = chatFragment.getUserid();
        boolean isFromCurrentUser = msg.getUserid().equals(userUid);
        return isFromCurrentUser ? RIGHT : LEFT;
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
        int pos = holder.getAdapterPosition();
        Message msg = msgs.get(pos);
        holder.bindProperties(msg, pos);
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    protected class ChatViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.msg_item)
        View msgItem;

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

        ChatViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindProperties(Message msg, int pos){

            if (msg.getUserid().equals(chatFragment.getUserid()))
                msgUsername.setVisibility(View.GONE);

            else if (pos == 0)
                msgUsername.setVisibility(View.VISIBLE);

            else {
                Message upMsg = msgs.get(pos - 1);
                boolean isEqual = upMsg.getUserid().equals(msg.getUserid());
                msgUsername.setVisibility(isEqual ? View.GONE: View.VISIBLE);
            }

            if(msgUsername.getVisibility() == View.VISIBLE){
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
                timeSeparatorItem.setVisibility(equals ? View.GONE: View.VISIBLE);
            }

            if (timeSeparatorItem.getVisibility() == View.VISIBLE){
                long date = msg.getTime();
                boolean isToday = DateUtils.isToday(date);
                if(isToday) timeSeparatorText.setText(R.string.text_today);
                else timeSeparatorText.setText(DateConverter.extractDate(date));
            }

            if (selectedItems.contains(msg)) {
//                frameLayout.setBackgroundColor(Color.LTGRAY);
                msgItem.setBackgroundColor(Color.BLUE);
            } else {
//                frameLayout.setBackgroundColor(Color.WHITE);
                msgItem.setBackgroundColor(Color.TRANSPARENT);
            }

            itemView.setOnLongClickListener(view -> {
                chatFragment
                        .getActivityCustom()
                        .startSupportActionMode(actionModeCallbacks);
                selectItem(msg);
                return true;
            });

            itemView.setOnClickListener(view -> selectItem(msg));


        }

        void selectItem(Message item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
//                    frameLayout.setBackgroundColor(Color.WHITE);
                    msgItem.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    selectedItems.add(item);
//                    frameLayout.setBackgroundColor(Color.LTGRAY);
                    msgItem.setBackgroundColor(Color.BLUE);
                }
            }
        }

    }

}