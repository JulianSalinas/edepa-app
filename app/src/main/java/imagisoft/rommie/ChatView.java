package imagisoft.rommie;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import butterknife.BindView;
import imagisoft.edepa.Message;


public class ChatView extends MessagesView {

    /**
     * Botón e input para enviar los mensajes
     */
    @BindView(R.id.send_card_view)
    CardView sendCardView;

    @BindView(R.id.text_input_view)
    TextInputEditText textInputView;

    /**
     * Se enlazan los componentes visuales con los atributos
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.chat_view);
    }

    /**
     * Función que se llema cada vez que se coloca el fragmento en la actividad
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupSendCardView();
        toolbarText = getToolbar().getTitle();
        getToolbar().setTitle(getResources().getString(R.string.nav_chat));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getToolbar().setTitle(toolbarText);
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes. Si
     * el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    @Override
    public void setupAdapter(){
        if(adapter == null) {
            adapter = new ChatViewAdapter(this);
            registerAdapterDataObserver();
        }
    }

    /**
     * Al presionar el botón se reunen los datos del msg y luego se envía
     */
    private void setupSendCardView() {
        sendCardView.setOnClickListener(v -> sendMessage());
    }

    /**
     * Función para enviar un msg. Toma el contenido y si no está vacío
     * procede a enviarlo
     */
    public void sendMessage(){
        String content = textInputView.getText().toString();
        if (!content.isEmpty()) sendNotEmptyMessage(content);
    }

    /**
     * Función usada por sendMessage
     * Se revisa que el mesaje no este vacío previamente
     * @param content: Contenido del mensaje extraido del input
     */
    private void sendNotEmptyMessage(String content){
        Message msg = createMessage(content);
        getFirebase().getChatReference().push().setValue(msg);
        textInputView.setText("");
    }

    /**
     * Reúne los datos del mensaje; usuario y fecha
     * y crea el objeto.
     * @param content: Contenido del mensaje extraido del input
     */
    public Message createMessage(String content){
        String username = getCurrentUsername();
        Long datetime = Calendar.getInstance().getTimeInMillis();
        return new Message(user.getUid(), username, content, datetime);
    }

}