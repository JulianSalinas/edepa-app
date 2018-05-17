package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;

import imagisoft.edepa.Message;
import imagisoft.miscellaneous.DateConverter;


public class ChatViewAdapter extends MessagesViewAdapterOnline{

    /**
     * Constantes paras escoger el tipo de vista que se colocará
     */
    private final int CHAT_LEFT_VIEW_TYPE = 0;
    private final int CHAT_RIGHT_VIEW_TYPE = 1;
    private final int CHAT_LEFT_VIEW_TYPE_WITH_SEPARATOR = 2;
    private final int CHAT_RIGHT_VIEW_TYPE_WITH_SEPARATOR = 3;

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
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param item: Message
     */
    @Override
    protected int getItemViewTypeWithoutSeparator(Message item){
        return item.getUserid().equals(user.getUid()) ?
                CHAT_RIGHT_VIEW_TYPE: CHAT_LEFT_VIEW_TYPE;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param item: Message
     */
    protected int getItemViewTypeWithSeparator(Message item){
        return item.getUserid().equals(user.getUid()) ?
                CHAT_RIGHT_VIEW_TYPE_WITH_SEPARATOR :
                CHAT_LEFT_VIEW_TYPE_WITH_SEPARATOR;
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout =

                viewType == CHAT_LEFT_VIEW_TYPE ? R.layout.chat_view_msg_left:
                viewType == CHAT_RIGHT_VIEW_TYPE ? R.layout.chat_view_msg_right:

                viewType == CHAT_LEFT_VIEW_TYPE_WITH_SEPARATOR ?
                        R.layout.chat_view_msg_left_with_separator:
                        R.layout.chat_view_msg_right_with_separator;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return  viewType == CHAT_LEFT_VIEW_TYPE ||
                viewType == CHAT_RIGHT_VIEW_TYPE ?
                new MessageViewHolder(view):
                new MessageWithSeparatorViewHolder(view);

    }

}