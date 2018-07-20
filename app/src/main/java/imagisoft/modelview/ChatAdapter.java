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
    protected ArrayList<Message> msgs;

    /**
     * Fragmento que hace uso de este adaptador
     */
    protected ChatFragment chatFragment;

    /**
     * Sirve para colorear el nombre de la persona según sus iniciales
     */
    private MaterialGenerator materialGenerator;

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
        this.materialGenerator = new MaterialGenerator(chatFragment.activity);
    }

    /**
     *  Obtiene si la noticia tiene un separador o marca de tiempo arriba
     *  @return ViewType: Con separador o sin separador
     */
    @Override
    public int getItemViewType(int position) {

        final Message currentMsg = msgs.get(position);

        if (position == 0)
            return getItemWithSeparator(currentMsg);

        else {
            Message upMsg = msgs.get(position - 1);
            return getItemViewType(currentMsg, upMsg);
        }

    }

    /**
     * Auxiliar para la función anterior.
     * Extrae la fecha del mensaje de arriba, si es la misma no es
     * necesario colocar un separador de fechas.
     * @param currentMsg: Mensaje actual
     * @param upMsg: Mensaje anterior
     */
    private int getItemViewType(Message currentMsg, Message upMsg){

        String currentDate = DateConverter.extractDate(currentMsg.getTime());
        String upDate = DateConverter.extractDate(upMsg.getTime());

        return currentDate.equals(upDate) ?
                getItemWithoutSeparator(currentMsg) :
                getItemWithSeparator(currentMsg);
    }

    /**
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param msg: Message
     */
    protected int getItemWithoutSeparator(Message msg){
        String userUid = chatFragment.user.getUid();
        boolean isFromCurrentUser = msg.getUserid().equals(userUid);
        return isFromCurrentUser ? RIGHT : LEFT;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param msg: Message
     */
    protected int getItemWithSeparator(Message msg){
        String userUid = chatFragment.user.getUid();
        boolean isFromCurrentUser = msg.getUserid().equals(userUid);
        return  isFromCurrentUser ? RIGHT_WITH_SEP: LEFT_WITH_SEP;
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

                viewType == LEFT_WITH_SEP ?
                        R.layout.chat_left_with_sep :
                        R.layout.chat_right_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return  viewType == LEFT || viewType == RIGHT ?
                new ChatVH(view): new ChatVHWS(view);

    }

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(ChatVH holder, int position) {

        Message msg = msgs.get(holder.getAdapterPosition());

        if(holder instanceof ChatVHWS)
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
        ChatVHWS holder = (ChatVHWS) viewHolder;
        holder.timeSeparator.setText(DateUtils.isToday(time) ?
                chatFragment.getResources().getString(R.string.text_today) :
                DateConverter.extractDate(time));
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
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

    /**
     * Clase para dividir los mensajes por hora.
     * Además, contiene un separador por fecha
     */
    protected class ChatVHWS extends ChatVH {

        @BindView(R.id.chat_separator_time)
        TextView timeSeparator;

        ChatVHWS(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}