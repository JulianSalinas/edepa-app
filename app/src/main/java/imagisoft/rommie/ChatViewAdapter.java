package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;

import imagisoft.edepa.Message;


public class ChatViewAdapter extends MessagesViewAdapterOnline{

    /**
     * Constantes paras escoger el tipo de vista que se colocará
     */
    private final int CHAT_TIMESTAMP = 0;
    private final int CHAT_LEFT_VIEW_TYPE = 1;
    private final int CHAT_RIGHT_VIEW_TYPE = 2;

    /**
     * Se asocia con firebase para recibir los mensajes
     * @param chatView: Vista que hace uso de este adaptador
     */
    public ChatViewAdapter(ChatView chatView){

        super(chatView);

        ChildEventListener listener = new MessageViewAdapterChildEventListener();
        view.activity.getChatReference().addChildEventListener(listener);

    }

    /**
     *  Obtiene si el mensaje va a la izq o der
     *  @param position: Posición del mensaje dentro del arreglo
     */
    @Override
    public int getItemViewType(int position) {

        if(msgs.get(position) instanceof Message) {
            Message item = (Message) msgs.get(position);
            boolean isThisUser = item.getUserid().equals(user.getUid());
            return isThisUser ? CHAT_RIGHT_VIEW_TYPE : CHAT_LEFT_VIEW_TYPE;
        }

        else return CHAT_TIMESTAMP;

    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout =
                viewType == CHAT_TIMESTAMP ? R.layout.date_separator :
                viewType == CHAT_RIGHT_VIEW_TYPE ? R.layout.chat_view_msg_right:
                R.layout.chat_view_msg_left;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == CHAT_TIMESTAMP ?
                new TimestampViewHolder(view) :
                new MessageViewHolder(view);

    }

}