package imagisoft.rommie;

import imagisoft.edepa.Message;

import android.os.Bundle;
import android.view.View;
import java.util.Calendar;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChatView extends MainViewFragment {

    /**
     * Se obtiene el usuario actual o que envía
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    /**
     * Es la capa (con su adaptador) donde se coloca cada uno de los mensajes
     */
    private RecyclerView chatView;
    private ChatViewAdapter adapter;

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
        textInputView = view.findViewById(R.id.chat_view_input);
        chatView = view.findViewById(R.id.chat_view_recycler);
        sendCardView = view.findViewById(R.id.chat_view_send_card);
        return view;

    }

    /**
     * Se configuran las clases de las vistas y sus eventos
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupAdapter();
        setupChatView();
        setupSendCardView();
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes
     */
    public void setupAdapter(){

        if(adapter == null) {
            adapter = new ChatViewAdapter(this);
            registerAdapterDataObserver();
        }

    }

    /**
     * Agrega un el evento de actualizar inserción al adaptado
     */
    public void registerAdapterDataObserver(){

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            /**
             * Al insertar un item el scroll se mueve al final
             */
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatView.scrollToPosition(adapter.getItemCount()-1);
            }

        });

    }

    /**
     * Se configura el contenedor de mensajes, chatView
     */
    public void setupChatView(){

        chatView.setAdapter(adapter);
        chatView.setHasFixedSize(true);
        chatView.setItemAnimator(new DefaultItemAnimator());
        chatView.setLayoutManager(new SmoothLayout(this.getActivity()));
        chatView.scrollToPosition(adapter.getItemCount()-1);

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