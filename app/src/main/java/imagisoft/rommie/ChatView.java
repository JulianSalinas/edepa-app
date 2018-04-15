package imagisoft.rommie;

import java.util.Calendar;
import butterknife.BindView;
import imagisoft.edepa.Message;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.CardView;
import android.support.v7.widget.AppCompatEditText;


public class ChatView extends MessagesView {

    /**
     * Botón e input para enviar los mensajes
     */
    @BindView(R.id.chat_view_send_card) CardView sendCardView;
    @BindView(R.id.chat_view_input) AppCompatEditText textInputView;

    /**
     * Se enlazan las clases con sus vistas
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.chat_view);
    }

    /**
     * Al terminar de asociar las vistas se coloca el adaptador
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupSendCardView();
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes
     */
    @Override
    public void setupAdapter(){
        if(adapter == null) {
            adapter = new ChatViewAdapter(this);
            registerAdapterDataObserver();
        }
    }

    /**
     * Al presionar el botón se llama al controlador para enviar el mensaje
     */
    private void setupSendCardView() {
        sendCardView.setOnClickListener(v -> sendMessage());
    }

    /**
     * Función para enviar un msg, si se envía se agrega a la vista
     */
    public void sendMessage(){
        String content = textInputView.getText().toString();
        if (!content.isEmpty()) sendNotEmptyMessage(content);
    }

    /**
     * Función usada por sendMessage
     * Se revisa que el mesaje no este vacío previamente
     */
    private void sendNotEmptyMessage(String content){
        Message msg = createMessage(content);
        getFirebase().getChatReference().push().setValue(msg);
        textInputView.setText("");
    }

    /**
     * Reúne los datos (fecha y usuario) y crea un objeto Message
     */
    public Message createMessage(String content){
        String username = getCurrentUsername();
        Long datetime = Calendar.getInstance().getTimeInMillis();
        return new Message(user.getUid(), username, content, datetime);
    }

}