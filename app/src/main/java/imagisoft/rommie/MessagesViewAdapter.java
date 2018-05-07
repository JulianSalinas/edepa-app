package imagisoft.rommie;

import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.Message;
import imagisoft.edepa.Timestamp;
import imagisoft.edepa.UDateConverter;


public abstract class MessagesViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Se obtiene el usuario actual o que envía
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    /**
     * Referencia al objeto que adapta
     */
    protected MainViewFragment view;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<Timestamp> msgs;

    protected Message lastMessage;

    /**
     * Constructor del adaptador
     */
    public MessagesViewAdapter(MainViewFragment view){

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
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Timestamp timestamp = msgs.get(viewHolder.getAdapterPosition());

        if(timestamp instanceof Message) {

            Message msg = (Message) timestamp;
            MessageViewHolder holder = (MessageViewHolder) viewHolder;
            holder.msgUsername.setText(msg.getUsername());
            holder.msgContent.setText(msg.getContent());
            holder.msgTimeDescription.setText(UDateConverter.extractTime(msg.getTime()));
            Linkify.addLinks(holder.msgContent, Linkify.WEB_URLS);

        }

        else {

            TimestampViewHolder holder = (TimestampViewHolder) viewHolder;
            holder.chatSeparatorTime.setText(UDateConverter.extractDate(timestamp.getTime()));

        }

    }

    /**
     * Clase para dividir los mensajes por hora
     */
    protected class TimestampViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat_separator_time)
        TextView chatSeparatorTime;

        TimestampViewHolder(View view) {
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