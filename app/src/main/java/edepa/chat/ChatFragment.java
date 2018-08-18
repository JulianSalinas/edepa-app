package edepa.chat;

import butterknife.BindView;

import butterknife.OnClick;
import butterknife.OnFocusChange;

import edepa.app.MainFragment;
import edepa.cloud.Cloud;
import edepa.cloud.CloudChat;

import edepa.custom.RecyclerFragment;
import edepa.model.Message;
import edepa.model.Preferences;

import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;
import edepa.minilibs.SmoothLayout;

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
     * Key utilizado con la función
     * {@link #onSaveInstanceState(Bundle)}
     */
    private static final String INPUT_TEXT_KEY = "input_text";

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
    @BindView(R.id.chat_recycler)
    RecyclerView chatRecycler;

    @Override
    protected RecyclerView getRecyclerView() {
        return chatRecycler;
    }

    /**
     * Contiene todos los mensajes del chat y ejecuta
     * los evento de inserción, deleción y modificación
     */
    protected ChatAdapter chatAdapter;

    @Override
    protected RecyclerAdapter getViewAdapter() {
        return chatAdapter;
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
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.chat_view;
    }

    /**
     * Carga los mensajes
     */
    protected CloudChat cloudChat;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatAdapter = new ChatAdapter(getNavigationActivity());
        chatAdapter.registerAdapterDataObserver(getDataObserver());
        cloudChat = new CloudChat();
        cloudChat.setCallbacks(chatAdapter);
        cloudChat.connect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cloudChat.disconnect();
    }

    /**
     * Se obtiene un data observer para que en el momento que
     * el usuario ingrese un mensaje, se realice scroll hasta este
     * @return AdapterDataObserver
     */
    private RecyclerView.AdapterDataObserver getDataObserver(){
        return new RecyclerView.AdapterDataObserver() {
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            scrollIfWaitingResponse();
        }};
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
        }
    }

    /**
     * Personaliza la actividad
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void customizeActivity(){
        setToolbarText(R.string.nav_chat);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);
    }

    /**
     * Se configura el contenedor de mensajes {@link #chatRecycler}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void setupRecyclerView(){
        LinearLayoutManager layoutManager =
                new SmoothLayout(getActivity());
        layoutManager.setStackFromEnd(true);
        chatRecycler.setAdapter(chatAdapter);
        chatRecycler.setHasFixedSize(true);
        chatRecycler.setItemAnimator(new DefaultItemAnimator());
        chatRecycler.setLayoutManager(layoutManager);
    }

    /**
     * Cuando el input pierde el foco el teclado se debe cerrar
     */
    @OnFocusChange(R.id.text_input_view)
    public void controlInputFocus(boolean hasFocus){
        if (!hasFocus) activity.hideKeyboard();
    }

    /**
     * Mueve el scroll hasta apuntar al último elemento
     * insertado
     */
    public void scrollToLastPosition(){
        int index = chatAdapter.getItemCount()-1;
        chatRecycler.smoothScrollToPosition(index);
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
     * Función para enviar un msg. Toma el contenido y si no está vacío
     * procede a enviarlo
     */
    @OnClick(R.id.send_card_view)
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
        Message message = createMessage(content);
        setWaitingResponse(true);
        CloudChat.addMessage(message);
        textInputView.setText("");
    }

    /**
     * Reúne los datos del mensaje; usuario y fecha
     * y crea el objeto.
     * @param content: Contenido del mensaje extraido del input
     */
    public Message createMessage(String content){
        return new Message.Builder()
                .username(getUsername())
                .userid(Cloud.getInstance().getUserId())
                .content(content).time(System.currentTimeMillis()).build();
    }

    /**
     * Obtiene el nombre de usuario desde las preferencias
     * Usada para crear los mensajes {@link #createMessage(String)}
     * @return String: nombre de usuario
     */
    public String getUsername(){
        String key = Preferences.USER_KEY;
        return Preferences.getStringPreference(activity, key);
    }

}