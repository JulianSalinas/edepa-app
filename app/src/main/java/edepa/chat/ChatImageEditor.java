package edepa.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.policy.TimeWindow;
import com.fxn.pix.Pix;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.Cloud;
import edepa.cloud.CloudChat;
import edepa.cloud.CloudUsers;
import edepa.custom.CustomFragment;
import edepa.custom.PhotoFragment;
import edepa.minilibs.RegexSearcher;
import edepa.model.Message;
import edepa.model.Preferences;
import edepa.model.Preview;
import edepa.model.UserProfile;
import edepa.modelview.R;
import edepa.services.UpdateImageService;

public class ChatImageEditor extends CustomFragment {

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

    protected String lastMessageKey;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.chat_image;
    }

    /**
     * Carga los mensajes
     */
    protected String imageLocalPath;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.GONE);

        Bundle args = getArguments();
        if (args != null && args.containsKey(INPUT_TEXT_KEY)){
            textInputView.setText(args.getString(INPUT_TEXT_KEY));
        }

        if (args != null && args.containsKey(INPUT_IMAGE_KEY)
                && args.getString(INPUT_IMAGE_KEY) != null){
            imageLocalPath = args.getString(INPUT_IMAGE_KEY);
            updatePreviewImage(imageLocalPath);
        }
        else if (savedInstanceState == null) searchLocalImage();

    }

    /**
     * Abre el selector de imagenes
     */
    @OnClick(R.id.chat_view_image)
    public void searchLocalImage(){
        Pix.start(this, REQUEST_IMAGE_CODE);
    }

    /**
     * Se ha recibido el código de solicitar el permiso
     * para abrir una imagen desde la SD
     * @param requestCode {@link #REQUEST_IMAGE_CODE}
     * @param resultCode Se espera RESULT_OK
     * @param data: Lista donde el primer elemento es la ruta de la imagen
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK){
            ArrayList<String> paths = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if(paths.size() > 0) updatePreviewImage(paths.get(0));
            else activity.onBackPressed();
        }
        else {
            activity.onBackPressed();
        }
    }

    /**
     * Actualiza la previsualización de la imagen que se
     * va a subir junto con la noticia
     * @param imageLocalPath: Ruta de la imagen en la SD
     */
    public void updatePreviewImage(String imageLocalPath){
        this.imageLocalPath = imageLocalPath;
        imageView.setVisibility(View.VISIBLE);
        if (imageLocalPath != null) {
            Glide.with(this)
                    .load(imageLocalPath)
                    .apply(PhotoFragment.getRequestOptions(getContext()))
                    .into(imageView);
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
            updatePreviewImage(imageLocalPath);
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

            lastMessageKey = CloudChat.addMessage(message);
            textInputView.setText("");

            if(imageLocalPath != null) uploadImage();

            if(imageView.getVisibility() == View.VISIBLE) {
                imageView.setImageDrawable(null);
                imageView.setVisibility(View.GONE);
                imageLocalPath = null;
            }

            activity.onBackPressed();

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
            String requestId = uploadRequest.startNow(getNavigationActivity());
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
