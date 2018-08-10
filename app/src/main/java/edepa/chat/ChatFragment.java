package edepa.chat;

import java.util.Calendar;
import butterknife.BindView;

import edepa.model.Cloud;
import edepa.model.Message;
import edepa.model.Preferences;

import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;
import edepa.custom.RecyclerFragment;
import edepa.custom.SmoothLayout;

import android.util.Log;
import android.os.Bundle;
import android.view.View;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.design.widget.TextInputEditText;


public class ChatFragment extends RecyclerFragment {

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
     * Es donde se colocan cada uno de los mensajes
     * de forma VISUAL
     */
    @BindView(R.id.chat_rv)
    RecyclerView chatRV;

    @Override
    protected RecyclerView getRecyclerView() {
        return chatRV;
    }

    /**
     * Contiene todos los mensajes del chat y ejecuta
     * los evento de inserción, deleción y modificación
     */
    protected ChatAdapter chatVA;

    @Override
    protected RecyclerAdapter getViewAdapter() {
        return chatVA;
    }

    /**
     * Key utilizado con la función
     * {@link #onSaveInstanceState(Bundle)}
     */
    private final String INPUT_TEXT_KEY = "input_text";

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.chat_view;
    }

    /**
     * Propiedad para saber si se está esperando
     * un respuesta del servidor después de enviar
     * un mensaje, es decir, se espera recibir el mismo
     * mensaje que se envió
     */
    protected boolean waitingResponse;

    public void setWaitingResponse(boolean waitingResponse) {
        this.waitingResponse = waitingResponse;
    }

    /**
     * @param msg Mensaje
     * @return True si el mensaje fue enviado por el usuario actual
     */
    public boolean isFromCurrentUser(Message msg){
        String userUid = Cloud.getInstance().getAuth().getUid();
        return msg.getUserid().equals(userUid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setToolbarText(R.string.nav_chat);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);

        if(savedInstanceState != null){
            String text = savedInstanceState.getString(INPUT_TEXT_KEY);
            textInputView.setText(text);
            Log.i(toString(), "onActivityCreated(Bundle)");
        }

    }

    /**
     * Mueve el scroll hasta apuntar al último elemento
     * insertado
     */
    public void scrollToLastPosition(){
        chatRV.smoothScrollToPosition(chatVA.getItemCount()-1);
    }

    /**
     * Hace scroll hasta la última posición en el momento
     * de agregar un nuevo mensaje
     * @see ChatAdapter#addMessage(Message)
     */
    public void scrollIfWaitingResponse(){
        if(waitingResponse){
            waitingResponse = false;
            scrollToLastPosition();
        }
    }

    /**
     * {@inheritDoc}
     * Guarda lo que el usuario ha escrito en el chat para cuando regrese
     * al mismo fragmento o se gire la pantalla
     * @param outState: Bundle con el contenido del chat
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String text = textInputView.getEditableText().toString();
        outState.putString(INPUT_TEXT_KEY, text);
        Log.i(toString(), "onSaveInstanceState()");
    }

    /**
     * Restaura en lo que el usuario habia escrito en el chat después
     * de salirse de la pantalla o al girarla
     * @param savedInstanceState: Bundle con el contenido del chat
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            String text = savedInstanceState.getString(INPUT_TEXT_KEY);
            textInputView.setText(text);
            Log.i(toString(), "onViewStateRestored()");
        }
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes.
     * Si el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setupAdapter(){
        if(chatVA == null) {
            chatVA = new ChatFirebase(this);
            Log.i(toString(), "setupAdapter()");
        }
    }

    /**
     * Se configura el contenedor de mensajes {@link #chatRV}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void setupRecyclerView(){
        if (chatRV.getAdapter() == null){

            LinearLayoutManager layoutManager =
                    new SmoothLayout(getActivity());
            layoutManager.setStackFromEnd(true);

            chatRV.setAdapter(chatVA);
            chatRV.setHasFixedSize(true);
            chatRV.setItemAnimator(new DefaultItemAnimator());
            chatRV.setLayoutManager(layoutManager);

            Log.i(toString(), "setupRecyclerView()");
        }
    }

    /**
     * Cuando el input pierde el foco el teclado se debe cerrar
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void setupTextInput(){
        textInputView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) activity.hideKeyboard();
        });
        Log.i(toString(), "setupTextInput()");
    }

    /**
     * Al presionar el botón se reunen los datos del msg y luego se envía
     * @see #disconnectSendListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void connectSendListener() {
        sendCardView.setOnClickListener(v -> sendMessage());
        Log.i(toString(), "connectSendListener()");
    }

    /**
     * Desconecta el fragment al no usarse
     * @see #connectSendListener()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void disconnectSendListener() {
        sendCardView.setOnClickListener(null);
        Log.i(toString(), "disconnectSendListener()");
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
        setWaitingResponse(true);
        Cloud.getInstance()
                .getReference(Cloud.CHAT)
                .push().setValue(msg);
        textInputView.setText("");
        Log.i(toString(), "sendNotEmptyMessage(content)");
    }

    /**
     * Reúne los datos del mensaje; usuario y fecha
     * y crea el objeto.
     * @param content: Contenido del mensaje extraido del input
     */
    public Message createMessage(String content){
        String username = getUsername();
        Long datetime = Calendar.getInstance().getTimeInMillis();
        String userid = Cloud.getInstance().getAuth().getUid();
        return new Message(userid, username, content, datetime);
    }

    /**
     * Obtiene el nombre de usuario desde las preferencias
     * Usada para crear los mensajes {@link #createMessage(String)}
     * @return String: nombre de usuario
     */
    public String getUsername(){
        String key = Preferences.USER_KEY;
        return Preferences.getInstance().getStringPreference(activity, key);
    }

}