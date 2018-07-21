package imagisoft.modelview;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.util.Linkify;
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


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH> {

    /**
     * Constantes paras escoger el tipo de vista que se colocará
     */
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int LEFT_WITH_SEP = 2;
    private final int RIGHT_WITH_SEP = 3;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<IChatItem> items;

    /**
     * Fragmento que hace uso de este adaptador
     */
    protected ChatFragment chatFragment;

    /**
     * Sirve para colorear el nombre de la persona según sus iniciales
     * @see MaterialGenerator
     */
    private MaterialGenerator materialGenerator;

    /**
     * @return Cantidad de mensajes
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Constructor
     * @param chatFragment: Vista que hace uso de este adaptador
     */
    public ChatAdapter(ChatFragment chatFragment){
        this.items = new ArrayList<>();
        this.chatFragment = chatFragment;
        this.materialGenerator = new MaterialGenerator(chatFragment.getActivity());
    }

    /**
     *  Obtiene si el mensaje marca de tiempo arriba
     *  @return ViewType: Con separador o sin separador
     */
    @Override
    public int getItemViewType(int position) {

        final IChatItem currentItem = items.get(position);

        if (currentItem instanceof Timestamp)
            return TIMESTAMP;

        else {

            Message currentMsg = (Message) currentItem;
            String userUid = chatFragment.user.getUid();

            if (currentMsg.getUserid().equals(userUid))
                return RIGHT;

            else {

                final IChatItem upItem = items.get(position - 1);

                if (upItem instanceof Timestamp)
                    return LEFT_WITH_NAME;

                else{

                    Message upMsg = (Message) upItem;

                    if (upMsg.getUserid().equals(currentMsg.getUserid()))
                        return LEFT

                }

            }

        }


        if (position == 0)
            return getItemWithSeparator(currentItem);

        else {
            Message upMsg = items.get(position - 1);
            return getItemViewType(currentItem, upMsg);
        }

    }

    /**
     * Auxiliar para la función anterior
     * Extrae la fecha del mensaje del msg anterios, si es la
     * misma no es necesario colocar un separador de fechas
     * @param currentMsg: Mensaje actual
     * @param upMsg: Mensaje anterior
     * @see #getItemViewType(int)
     */
    private int getItemViewType(Message currentMsg, Message upMsg){

        String upDate = DateConverter.extractDate(upMsg.getTime());
        String currentDate = DateConverter.extractDate(currentMsg.getTime());

        return currentDate.equals(upDate) ?
                getItemWithoutSeparator(currentMsg) :
                getItemWithSeparator(currentMsg);
    }

    /**
     * Regresa un item sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param msg: Message
     * @see #getItemViewType(Message, Message)
     */
    protected int getItemWithoutSeparator(Message msg){
        String userUid = chatFragment.user.getUid();
        boolean isFromCurrentUser = msg.getUserid().equals(userUid);
        return isFromCurrentUser ? RIGHT : LEFT;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param msg: Message
     @see #getItemViewType(Message, Message)
     */
    protected int getItemWithSeparator(Message msg){
        String userUid = chatFragment.user.getUid();
        boolean isFromCurrentUser = msg.getUserid().equals(userUid);
        return  isFromCurrentUser ? RIGHT_WITH_SEP: LEFT_WITH_NAME;
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public ChatVH onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout =

                viewType == LEFT ? R.layout.chat_left :
                viewType == RIGHT ? R.layout.chat_right :

                viewType == LEFT_WITH_NAME ?
                        R.layout.chat_left_with_sep :
                        R.layout.chat_right_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return  viewType == LEFT || viewType == RIGHT ?
                new ChatVH(view): new ChatVHWN(view);

    }

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(ChatVH holder, int position) {

        Message msg = items.get(holder.getAdapterPosition());

        if(holder instanceof ChatVHWN)
            setTimeSeparator(holder, msg.getTime());

        int color = materialGenerator.getColor(msg.getUsername());
        holder.msgUsername.setTextColor(color);

        holder.msgUsername.setText(msg.getUsername());
        holder.msgContent.setText(msg.getContent());

        String timedescription = DateConverter.extractTime(msg.getTime());
        holder.msgTimeDescription.setText(timedescription);
        Linkify.addLinks(holder.msgContent, Linkify.ALL);

    }

    /**
     * Coloca encima del mensaje una fecha que separa los mensaje\s
     * @param viewHolder: Vista donde donde se debe colocar la fecha
     * @param time: Fecha que se pone el separador
     */
    private void setTimeSeparator(ChatVH viewHolder, long time){
        ChatVHWN holder = (ChatVHWN) viewHolder;
        holder.timeSeparator.setText(DateUtils.isToday(time) ?
                chatFragment.getResources().getString(R.string.text_today) :
                DateConverter.extractDate(time));
    }

    protected class ChatVH extends RecyclerView.ViewHolder {

        @BindView(R.id.msg_username)
        TextView msgUsername;

        @BindView(R.id.msg_content)
        TextView msgContent;

        @BindView(R.id.msg_time_description)
        TextView msgTimeDescription;

        ChatVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    protected class ChatVHWN extends ChatVH {

        @BindView(R.id.chat_separator_time)
        TextView timeSeparator;

        ChatVHWN(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}