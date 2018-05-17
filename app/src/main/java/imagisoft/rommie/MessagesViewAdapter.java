package imagisoft.rommie;

import android.view.View;
import android.widget.TextView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.Message;
import imagisoft.edepa.Timestamp;
import imagisoft.miscellaneous.DateConverter;


public abstract class MessagesViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Último mensaje enviado en el congreso
     */
    protected Message lastMessage;

    /**
     * Referencia al objeto que adapta
     */
    protected MainActivityFragment view;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<Message> msgs;

    /**
     * Se obtiene el usuario actual o que envía
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    /**
     * Constructor del adaptador
     */
    public MessagesViewAdapter(MainActivityFragment view){
        this.view = view;
        this.msgs = new ArrayList<>();
    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return msgs.size();
    }

    /**
     *  Obtiene si es una noticia o una marca de tiempo
     */
    @Override
    public int getItemViewType(int position) {

        final Message item = msgs.get(position);

        if (position == 0)
            return getItemViewTypeWithSeparator(item);

        else {

            Message upItem = msgs.get(position - 1);
            String itemDate = DateConverter.extractDate(item.getTime());
            String upItemDate = DateConverter.extractDate(upItem.getTime());

            if (itemDate.equals(upItemDate))
                return getItemViewTypeWithoutSeparator(item);
            else
                return getItemViewTypeWithSeparator(item);

        }

    }

    /**
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param item: Message
     */
    protected abstract int getItemViewTypeWithoutSeparator(Message item);

    /**
     * El item necesita indicar el dia usando un separador
     * @param item: Message
     */
    protected abstract int getItemViewTypeWithSeparator(Message item);

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Message msg = msgs.get(viewHolder.getAdapterPosition());

        if(viewHolder instanceof MessageWithSeparatorViewHolder) {

            MessageWithSeparatorViewHolder holder = (MessageWithSeparatorViewHolder) viewHolder;

            Long currentTime = Calendar.getInstance().getTimeInMillis();
            String currentDate = DateConverter.extractDate(currentTime);
            String lastTimestampDate = DateConverter.extractDate(msg.getTime());

            if (currentDate.equals(lastTimestampDate))
                holder.timeSeparator.setText(view.getResources().getString(R.string.text_today));
            else
                holder.timeSeparator.setText(DateConverter.extractDate(msg.getTime()));

            holder.msgUsername.setText(msg.getUsername());
            holder.msgContent.setText(msg.getContent());
            holder.msgTimeDescription.setText(DateConverter.extractTime(msg.getTime()));
            Linkify.addLinks(holder.msgContent, Linkify.WEB_URLS);

        }

        else {

            MessageViewHolder holder = (MessageViewHolder) viewHolder;

            holder.msgUsername.setText(msg.getUsername());
            holder.msgContent.setText(msg.getContent());
            holder.msgTimeDescription.setText(DateConverter.extractTime(msg.getTime()));
            Linkify.addLinks(holder.msgContent, Linkify.WEB_URLS);

        }

    }

    /**
     * Clase para dividir los mensajes por hora
     */
    protected class MessageWithSeparatorViewHolder extends MessageViewHolder {

        @BindView(R.id.chat_separator_time)
        TextView timeSeparator;

        MessageWithSeparatorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    protected class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.msg_username)
        TextView msgUsername;

        @BindView(R.id.msg_content)
        TextView msgContent;

        @BindView(R.id.msg_time_description)
        TextView msgTimeDescription;

        MessageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}