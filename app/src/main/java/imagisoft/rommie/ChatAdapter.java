package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import imagisoft.edepa.Message;


public class ChatAdapter extends MessagesAdapterOnline {

    /**
     * Constantes paras escoger el tipo de vista que se colocará
     */
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int LEFT_WITH_SEP = 2;
    private final int RIGHT_WITH_SEP = 3;

    /**
     * Se asocia con firebase para recibir los mensajes
     * @param chatFragment: Vista que hace uso de este adaptador
     */
    public ChatAdapter(ChatFragment chatFragment){
        super(chatFragment);
    }

    /**
     * Se coloca la referencia de donde se extraen los mensajes
     */
    @Override
    protected void setupReference() {
        this.reference = view.activity.getChatReference();
    }

    /**
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param msg: Message
     */
    @Override
    protected int getItemWithoutSeparator(Message msg){
        boolean isFromCurrentUser = msg.getUserid().equals(user.getUid());
        return isFromCurrentUser ? RIGHT : LEFT;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param msg: Message
     */
    protected int getItemWithSeparator(Message msg){
        boolean isFromCurrentUser = msg.getUserid().equals(user.getUid());
        return  isFromCurrentUser ? RIGHT_WITH_SEP : LEFT_WITH_SEP;
    }

    /**
     * Crear la vista del mensaje, ajustando a izq o der según corresponda
     * @param parent: Vista padre
     * @param viewType: Alguna de las constantes definidas en ésta clase
     */
    @Override
    public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout =

                viewType == LEFT ? R.layout.msg_left :
                viewType == RIGHT ? R.layout.msg_right :

                viewType == LEFT_WITH_SEP ?
                        R.layout.msg_left_with_sep:
                        R.layout.msg_right_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return  viewType == LEFT || viewType == RIGHT ?
                new MessageVH(view): new MessageVHWS(view);

    }

}