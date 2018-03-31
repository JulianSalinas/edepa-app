package imagisoft.rommie;

import imagisoft.edepa.Message;

import android.os.Bundle;
import android.view.View;
import java.util.Calendar;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.design.widget.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChatView extends MainViewFragment {

    // Se obtiene el usuario que envía
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    /**
     * Boton para enviar los mensajes
     */
    private CardView sendCardView;

    /**
     * Es la capa donde se coloca cada uno de los mensajes
     */
    private RecyclerView recyclerView;

    /**
     * Input donde se escribe el msg que se desea enviar
     */
    private TextInputEditText textInputView;

    /**
     * Es necesario el adaptador para colocar nuevos mensajes
     */
    private ChatViewAdapter adapter;

    /**
     * Se crea el contenedor de los atributos que son vistas
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.chat_view, container, false);
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        bindViews();
        setupAdapter();
        setupRecyclerView();
        setupSendCardView();
    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     */
    private void bindViews(){
        assert getView() != null;
        textInputView = getView().findViewById(R.id.chat_view_input);
        recyclerView = getView().findViewById(R.id.chat_view_recycler);
        sendCardView = getView().findViewById(R.id.chat_view_send_card);
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes
     */
    public void setupAdapter(){
        adapter = new ChatViewAdapter(this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            /**
             * Al insertar un item el scroll se mueve al final
             */
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }

        });
    }

    /**
     * Se configura el contenedor de mensajes, recyclerView
     */
    public void setupRecyclerView(){
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
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

        // Se obtiene el usuario que envía el mensaje
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String content = textInputView.getText().toString();

        if (!content.isEmpty()) {

            Long datetime = Calendar.getInstance().getTimeInMillis();
            Message msg = new Message(user.getUid(), user.getDisplayName(), content, datetime);

            getFirebase().getChatReference().push().setValue(msg);
            textInputView.setText("");

        }

    }

}