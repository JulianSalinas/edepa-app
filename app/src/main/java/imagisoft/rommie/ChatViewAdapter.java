package imagisoft.rommie;

import imagisoft.edepa.Message;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.google.firebase.database.ChildEventListener;


public class ChatViewAdapter extends MessagesViewAdapter{

    /**
     * Constantes paras escoger el tipo de vista que se colocará
     */
    private int CHAT_LEFT_VIEW_TYPE = 1;
    private int CHAT_RIGHT_VIEW_TYPE = 2;

    /**
     * Constructor del adaptador
     */
    public ChatViewAdapter(ChatView chatView){

        super(chatView);

        ChildEventListener listener = new MessageViewAdapterChildEventListener();
        chatView.getFirebase().getChatReference().addChildEventListener(listener);

    }

    /**
     *  Obtiene si el mensaje va a la izq o der
     */
    @Override
    public int getItemViewType(int position) {

        Message item = msgs.get(position);
        return item.getUserid().equals(user.getUid()) ?
                CHAT_RIGHT_VIEW_TYPE:
                CHAT_LEFT_VIEW_TYPE;

    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     */
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == CHAT_RIGHT_VIEW_TYPE ?
                R.layout.chat_view_msg_right:
                R.layout.chat_view_msg_left;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return new MessageViewHolder(view);

    }

}