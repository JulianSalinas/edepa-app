package edepa.chat;

import butterknife.BindView;

import butterknife.OnClick;
import butterknife.OnFocusChange;

import edepa.cloud.Cloud;
import edepa.cloud.CloudChat;

import edepa.cloud.CloudUsers;
import edepa.custom.RecyclerFragment;
import edepa.minilibs.RegexSearcher;
import edepa.model.Message;
import edepa.model.Preferences;

import edepa.model.Preview;
import edepa.model.UserProfile;
import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;
import edepa.minilibs.SmoothLayout;
import edepa.services.UpdateImageService;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.design.widget.TextInputEditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.policy.TimeWindow;
import com.fxn.pix.Pix;

import org.json.JSONObject;


public class ChatFragment extends RecyclerFragment {

    public static final int REQUEST_IMAGE_CODE = 100;

    /**
     * Key utilizado con la función
     * {@link #onSaveInstanceState(Bundle)}
     */
    public static final String INPUT_TEXT_KEY = "input_text";

    public static final String INPUT_IMAGE_KEY = "image_key";

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

    @BindView(R.id.chat_view_image)
    ImageView imageView;

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
        return R.layout.chat_screen;
    }

    /**
     * Carga los mensajes
     */
    protected CloudChat cloudChat;
    protected String imageLocalPath;
    protected String lastMessageKey;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(INPUT_TEXT_KEY)){
            textInputView.setText(args.getString(INPUT_TEXT_KEY));
        }

        if (args != null && args.containsKey(INPUT_IMAGE_KEY)){
            imageLocalPath = args.getString(INPUT_IMAGE_KEY);
            openChatImageEditor();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cloudChat.disconnect();
    }

    @OnClick(R.id.chat_view_camera)
    public void openChatImageEditor(){
        Bundle args = new Bundle();
        args.putString(ChatImageEditor.INPUT_IMAGE_KEY, imageLocalPath);
        ChatImageEditor fragment = new ChatImageEditor();
        fragment.setArguments(args);
        setFragmentOnScreen(fragment, "CHAT_IMAGE_EDITOR");
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
        outState.putString(INPUT_IMAGE_KEY, imageLocalPath);
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
            imageLocalPath = savedInstanceState.getString(INPUT_IMAGE_KEY);
            textInputView.setText(text);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void updateUnreadMessagesToZero(){
        String key = Preferences.UNREAD_MESSAGES_KEY;
        Preferences.setPreference(getNavigationActivity(), key, 0);
        getNavigationActivity().updateChatBadge();
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
        if (!content.isEmpty() || imageLocalPath != null)
            sendNotEmptyMessage(content);
    }

    /**
     * Función usada por sendMessage
     * Se revisa que el mesaje no este vacío previamente
     * @param content: Contenido del mensaje extraido del input
     */
    private void sendNotEmptyMessage(String content){

        CloudUsers cloudUsers = new CloudUsers();
        cloudUsers.setUserProfileListener(userProfile -> {

            Message message = buildMessage(userProfile, content);
            setWaitingResponse(true);
            lastMessageKey = CloudChat.addMessage(message);
            textInputView.setText("");

            if(imageLocalPath != null) uploadImage();

            if(imageView.getVisibility() == View.VISIBLE) {
                imageView.setImageDrawable(null);
                imageView.setVisibility(View.GONE);
                imageLocalPath = null;
            }

        });
        cloudUsers.requestCurrentUserInfo();

    }

    /**
     * Sube la imagen al servidor Cloudinary por medio del servicio
     * {@link UpdateImageService}
     * Los párametros son pasados por medio de las preferecias usando
     * como key el requestId y como value el {@link #lastMessageKey}
     * Son leidos en {@link UpdateImageService#onStart(String)}
     */
    public void uploadImage(){

        String name = RegexSearcher.findFilenameFromUrl(imageLocalPath);

        UploadRequest uploadRequest = MediaManager.get().upload(imageLocalPath)
                .unsigned("unsigned_preset")
                .option("public_id", name)
                .constrain(TimeWindow.immediate());

        try {
            JSONObject args = new JSONObject();
            args.put(UpdateImageService.OBJECT_KEY, lastMessageKey);
            String requestId = uploadRequest.dispatch();
            args.put(UpdateImageService.REQUEST_ID, requestId);
            args.put(UpdateImageService.CLOUD_TYPE, Cloud.CHAT);
            Preferences.setPreference(getNavigationActivity(), requestId, args.toString());
        }
        catch (Exception exception){
            Log.e(toString(), "Cannot pass args: " + exception.getMessage());
        }

    }


    /**
     * Reúne los datos del mensaje; usuario y fecha
     * y crea el objeto.
     * @param content: Contenido del mensaje extraido del input
     */
    public Message buildMessage(UserProfile userProfile, String content){

        Preview preview = new Preview();
        preview.setUrl(imageLocalPath == null ? null : "uploading");

        return new Message.Builder()
                .preview(preview)
                .username(userProfile.getUsername())
                .userid(userProfile.getUserid())
                .content(content).time(System.currentTimeMillis()).build();
    }

}