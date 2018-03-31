package imagisoft.rommie;

import java.util.ArrayList;
import imagisoft.edepa.Message;
import imagisoft.edepa.Controller;
import imagisoft.edepa.Schedule;
import imagisoft.edepa.UDateConverter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class ChatViewAdapter
        extends RecyclerView.Adapter<ChatViewAdapter.ChatMessageViewHolder> {

    /**
     * Variables par escoger el tipo de vista que se colocará
     */
    private int CHAT_LEFT_VIEW_TYPE = 1;
    private int CHAT_RIGHT_VIEW_TYPE = 2;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private ArrayList<Message> msgs;

    /**
     * Referencia al objeto que adapta
     */
    private ChatView chatView;

    /**
     * Constructor del adaptador
     */
    ChatViewAdapter(ChatView chatView){

        this.chatView = chatView;
        this.msgs = new ArrayList<>();

        ValueEventListener listener = new ChatViewAdapterValueEventListener();
        chatView.getFirebase().getChatReference().addValueEventListener(listener);

    }

    /**
     * Agrega un nuevo mensaje a la vista
     */
    public void addMsg(Message msg){
        msgs.add(msg);
        notifyItemInserted(msgs.size()-1);
    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return msgs.size();
    }

    /**
     *  Obtiene si el mensaje va a la izq o der
     */
    @Override
    public int getItemViewType(int position) {
        Message item = msgs.get(position);
        return item.getUserid() == Controller.getInstance().getUserid() ?
                CHAT_RIGHT_VIEW_TYPE:
                CHAT_LEFT_VIEW_TYPE;
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     */
    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == CHAT_RIGHT_VIEW_TYPE ?
                R.layout.chat_view_msg_right:
                R.layout.chat_view_msg_left;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return new ChatMessageViewHolder(view);

    }

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        Message msg = msgs.get(holder.getAdapterPosition());
        holder.username.setText(msg.getUsername());
        holder.messageContent.setText(msg.getContent());
        holder.timeDescription.setText(UDateConverter.extractTime(msg.getTime()));
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView timeDescription;
        TextView messageContent;

        ChatMessageViewHolder(View view) {
            super(view);
            this.username = view.findViewById(R.id.chat_view_msg_username);
            this.timeDescription = view.findViewById(R.id.chat_view_msg_time_description);
            this.messageContent = view.findViewById(R.id.chat_view_msg_content);
        }

    }

    /**
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class ChatViewAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            Schedule schedule = schedulePager.getFirebase().getSchedule();
//            eventsByDay = schedule.getEventsByDay();
//            dates.addAll(eventsByDay.keySet());
//            createScheduleViews();
            notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
//            Context context = schedulePager.getContext();
//            dates.add(context.getResources().getString(R.string.text_no_connection));
            notifyDataSetChanged();
        }

    }

}