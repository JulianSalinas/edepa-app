package imagisoft.rommie;

import java.util.Calendar;
import imagisoft.edepa.Message;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.AppCompatEditText;


public class ChatView extends MessagesView {

    /**
     * Botón e input para enviar los mensajes
     */
    private CardView sendCardView;
    private AppCompatEditText textInputView;

    /**
     * Se enlazan las clases con sus vistas
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.chat_view, container, false);
        mainView = view.findViewById(R.id.chat_view_recycler);
        sendCardView = view.findViewById(R.id.chat_view_send_card);
        textInputView = view.findViewById(R.id.chat_view_input);
        return view;

    }

    /**
     * Se configuran las clases de las vistas y sus eventos
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
     * Al presionar el boton se llama al controlador para enviar el mensaje
     * Luego se debe actualizar la vista
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
     * Función para enviar un msg, si se envía se agrega a la vista
     * se revisa que el mesaje no este vacío previamente
     */
    private void sendNotEmptyMessage(String content){

        Message msg = createMessage(content);
        getFirebase().getChatReference().push().setValue(msg);
        textInputView.setText("");

    }

    /**
     * Reune los datos y crea un objeto Message
     */
    public Message createMessage(String content){

        Long datetime = Calendar.getInstance().getTimeInMillis();
        return new Message(user.getUid(), user.getDisplayName(), content, datetime);

    }

}