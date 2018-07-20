package imagisoft.modelview;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.widget.CardView;
import android.support.design.widget.TextInputEditText;

import java.util.Calendar;
import butterknife.BindView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import imagisoft.model.Message;
import imagisoft.model.Preferences;


public class ChatFragment extends MainActivityFragment {

    /**
     * Es donde se colocan cada uno de los mensajes
     */
    @BindView(R.id.chat_rv)
    RecyclerView chatRV;

    /**
     * Es el botón para enviar el mensaje
     */
    @BindView(R.id.send_card_view)
    CardView sendCardView;

    /**
     * Es donde se escribe el mensaje por enviar
     */
    @BindView(R.id.text_input_view)
    TextInputEditText textInputView;

    /**
     * Contiene todos los mensajes del chat
     */
    protected ChatAdapter chatVA;

    /**
     * Key utilizado con la función onSaveInstanceState(Bundle)
     */
    private final String INPUT_TEXT_KEY = "input_text";

    /**
     * Se obtiene el usuario que está usando la aplicación
     * para saber de que lado colocar los mensajes
     */
    protected FirebaseAuth auth = FirebaseAuth.getInstance();
    protected FirebaseUser user = auth.getCurrentUser();

    /**
     * Contructor del fragmento
     * @return ChatFragment
     */
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    /**
     * Se define cúal es el layout por utilizar
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.chat_view;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param savedInstanceState: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSendCardView();
        setupAdapter();
        setupRecyclerView();
    }

    /**
     * Se personaliza el contenido base de la aplicación
     */
    @Override
    public void setupActivityView() {
        setToolbarVisibility(View.VISIBLE);
        setToolbarText(R.string.nav_chat);
        setStatusBarColor(R.color.app_primary_dark);
    }

    /**
     * Guarda lo que el usuario ha escrito en el chat para cuando regrese
     * al mismo fragmento o se gire la pantalla
     * @param outState: Bundle con el contenido del chat
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String text = textInputView.getEditableText().toString();
        outState.putString(INPUT_TEXT_KEY, text);
    }

    /**
     * Restaura en lo que el usuario habia escrito en el chat después
     * de salirse de la panralla o al girarla
     * @param savedInstanceState: Bundle con el contenido del chat
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            String text = savedInstanceState.getString(INPUT_TEXT_KEY);
            textInputView.setText(text);
            chatRV.scrollToPosition(chatVA.getItemCount() - 1);
        }
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes. Si
     * el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    public void setupAdapter(){
        if(chatVA == null) {
            chatVA = new ChatFirebase(this);
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
        activity.getChatReference().push().setValue(msg);
        textInputView.setText("");
    }

    /**
     * Reúne los datos del mensaje; usuario y fecha
     * y crea el objeto.
     * @param content: Contenido del mensaje extraido del input
     */
    public Message createMessage(String content){
        String username = getUsername();
        Long datetime = Calendar.getInstance().getTimeInMillis();
        return new Message(user.getUid(), username, content, datetime);
    }

    /**
     * Obtiene el nombre de usuario desde las preferencias
     * @return String: nombre de usuario
     */
    public String getUsername(){
        String key = Preferences.USER_KEY;
        return prefs.getStringPreference(activity, key);
    }

    /**
     * Al insertar un item el scroll se mueve al final
     */
    protected Object observer = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            chatRV.scrollToPosition(chatVA.getItemCount()-1);
        }

    };

    /**
     * Coloca la vista hasta el último elemento
     */
    public void registerAdapterDataObserver(){
        chatVA.registerAdapterDataObserver(
                (RecyclerView.AdapterDataObserver) observer);
    }

    /**
     * Se configura el contenedor de mensajes, chatRV
     */
    public void setupRecyclerView(){
        chatRV.setAdapter(chatVA);
        chatRV.setHasFixedSize(true);
        chatRV.setItemAnimator(new DefaultItemAnimator());
        chatRV.setLayoutManager(new SmoothLayout(this.getActivity()));
        chatRV.scrollToPosition(chatVA.getItemCount()-1);
    }

}