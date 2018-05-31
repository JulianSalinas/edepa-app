package imagisoft.modelview;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.model.Message;
import imagisoft.misc.DateConverter;


public abstract class MessagesAdapter
        extends RecyclerView.Adapter<MessagesAdapter.MessageVH> {

    /**
     * Referencia al objeto por el que es utilizado
     */
    protected ActivityFragment view;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<Message> msgs;

    /**
     * Constructor del adaptador
     */
    public MessagesAdapter(ActivityFragment view){
        this.view = view;
        this.msgs = new ArrayList<>();
    }

    /**
     * @return Cantidad de vistas por crear
     */
    @Override
    public int getItemCount() {
        return msgs.size();
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
     * Regresa un mensaeje sin fecha o separador  porque el mensaje
     * de arriba que es del mismo día ya tiene uno
     * @param msg: Message
     */
    protected abstract int getItemWithoutSeparator(Message msg);

    /**
     * El item necesita indicar el dia usando un separador
     * @param msg: Message
     */
    protected abstract int getItemWithSeparator(Message msg);

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo.
     *                 Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(MessageVH holder, int position) {

        Message msg = msgs.get(holder.getAdapterPosition());

        if(holder instanceof MessageVHWS)
            setTimeSeparator(holder, msg.getTime());

        holder.msgUsername.setText(msg.getUsername());
        holder.msgContent.setText(msg.getContent());

        String timedescription = setTimeDescription(msg);
        holder.msgTimeDescription.setText(timedescription);
        Linkify.addLinks(holder.msgContent, Linkify.ALL);

    }

    protected String setTimeDescription(Message msg){
        return DateConverter.extractTime(msg.getTime());
    }

    /**
     * Coloca encima del mensaje una fecha que separa los mensaje\s
     * @param viewHolder: Vista donde donde se debe colocar la fecha
     * @param time: Fecha que se pone el separador
     */
    private void setTimeSeparator(MessageVH viewHolder, long time){
        MessageVHWS holder = (MessageVHWS) viewHolder;
        holder.timeSeparator.setText(DateUtils.isToday(time) ?
                view.getResources().getString(R.string.text_today) :
                DateConverter.extractDate(time));
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    protected class MessageVH extends RecyclerView.ViewHolder {

        @BindView(R.id.msg_username)
        TextView msgUsername;

        @BindView(R.id.msg_content)
        TextView msgContent;

        @BindView(R.id.msg_time_description)
        TextView msgTimeDescription;

        MessageVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    /**
     * Clase para dividir los mensajes por hora.
     * Además, contiene un separador por fecha
     */
    protected class MessageVHWS extends MessageVH {

        @BindView(R.id.chat_separator_time)
        TextView timeSeparator;

        MessageVHWS(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}