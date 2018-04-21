package imagisoft.rommie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.Message;
import imagisoft.edepa.UDateConverter;

import android.view.View;
import android.widget.TextView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public abstract class MessagesViewAdapter
        extends RecyclerView.Adapter<MessagesViewAdapter.MessageViewHolder> {

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
    protected ArrayList<Message> msgs;

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
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Message msg = msgs.get(holder.getAdapterPosition());
        holder.msgUsername.setText(msg.getUsername());
        holder.msgContent.setText(msg.getContent());
        holder.msgTimeDescription.setText(UDateConverter.extractTime(msg.getTime()));
        Linkify.addLinks(holder.msgContent, Linkify.WEB_URLS);

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