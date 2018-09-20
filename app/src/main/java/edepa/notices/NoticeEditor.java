package edepa.notices;

import butterknife.BindView;
import butterknife.OnClick;

import edepa.cloud.Cloud;
import edepa.custom.PhotoFragment;
import edepa.minilibs.DialogFancy;
import edepa.custom.CustomFragment;

import edepa.minilibs.OnlineHelper;
import edepa.minilibs.RegexSearcher;
import edepa.model.Notice;
import edepa.model.Preferences;
import edepa.model.Preview;
import edepa.modelview.R;
import edepa.services.UpdateImageService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
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


public class NoticeEditor extends CustomFragment {

    private static final int REQUEST_IMAGE_CODE = 100;

    @BindView(R.id.publish_image)
    ImageView publishImage;

    @BindView(R.id.publish_image_upload)
    View publishImageUpload;

    @BindView(R.id.text_input_title)
    TextInputEditText textInputTitle;

    @BindView(R.id.text_input_content)
    TextInputEditText textInputContent;

    private String noticeId;
    private String imageUrl;
    private String imageLocalPath;

    @Override
    public int getResource() {
        return R.layout.notices_editor;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarText(R.string.text_notices_editor);
        setToolbarVisibility(View.VISIBLE);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            imageUrl = savedInstanceState.getString("imageUrl");
            imageLocalPath = savedInstanceState.getString("imageLocalPath");
            updatePreviewImage(imageLocalPath);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imageUrl", imageUrl);
        outState.putString("imageLocalPath", imageLocalPath);
    }

    /**
     * Abre el selector de imagenes
     */
    @OnClick({R.id.publish_image_upload, R.id.publish_image})
    public void searchLocalImage(){
        Pix.start(this, REQUEST_IMAGE_CODE);
    }

    /**
     * Se sube la noticia sin la imagen, luego cuando la imagen
     * se terminar de subir se actualiza la referencia
     */
    @OnClick(R.id.publish_button)
    public void onPublishButtonClick() {
        Cloud cloud = Cloud.getInstance();
        this.noticeId = cloud.getReference(Cloud.NEWS).push().getKey();

        cloud.getReference(Cloud.NEWS).child(noticeId)
                .setValue(buildPublication())
                .addOnSuccessListener(v -> handleSuccess())
                .addOnFailureListener(this::handleError);

        if(imageLocalPath != null) uploadImage();
    }

    /**
     * Recopila la información de la publicación
     * @return Notice
     */
    public Notice buildPublication() {
        Preview preview = new Preview();
        preview.setUrl(imageLocalPath == null ? null : "uploading");

        return new Notice.Builder()
                .time(System.currentTimeMillis())
                .title(getNullable(textInputTitle.getText()))
                .preview(preview)
                .content(getNullable(textInputContent.getText())).build();
    }

    private String getNullable(Editable editable){
        String text = editable.toString();
        return text.isEmpty() ? null : text;
    }

    /**
     * Sube la imagen al servidor Cloudinary por medio del servicio
     * {@link UpdateImageService}
     * Los párametros son pasados por medio de las preferecias usando
     * como key el requestId y como value el {@link #noticeId}. Son leidos en
     * {@link UpdateImageService#onStart(String)}
     */
    public void uploadImage(){
        String name = RegexSearcher.findFilenameFromUrl(imageLocalPath);

        UploadRequest uploadRequest = MediaManager.get().upload(imageLocalPath)
                .unsigned("unsigned_preset")
                .option("public_id", name)
                .constrain(TimeWindow.immediate());

        try {
            JSONObject args = new JSONObject();
            args.put(UpdateImageService.OBJECT_KEY, noticeId);
            String requestId = uploadRequest.startNow(getNavigationActivity());
            args.put(UpdateImageService.REQUEST_ID, requestId);
            args.put(UpdateImageService.CLOUD_TYPE, Cloud.NEWS);
            Preferences.setPreference(getNavigationActivity(), requestId, args.toString());
        }
        catch (Exception exception){
            Log.e(toString(), "Cannot pass args: " + exception.getMessage());
        }

    }

    /**
     * La notica se ha publicado correctamente
     * Puede que se haya publicado solo localmente hasta
     * que el usuario tenga conexión
     */
    public void handleSuccess() {
         showPublishSuccess();
         if (!OnlineHelper.isOnline(getNavigationActivity()))
             showPublishWarning();
         Log.i(toString(), "Publication success");
    }

    /**
     * Ha sucedido un error inesperado al subir la noticia
     * @param exception Contiene detalles del error
     */
    public void handleError(Exception exception){
        showPublishError();
        Log.e(toString(), exception.getMessage());
    }

    /**
     * Se muestra cuando no hay internet
     */
    public void showPublishWarning(){
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setContext(getContext())
                .setStatus(DialogFancy.WARNING)
                .setTitle(R.string.text_warning_publication)
                .setContent(R.string.text_warning_publication_content)
                .build().show();
    }

    /**
     * Se muestra después de que la noticia fue
     * publicada de manera existosa
     */
    public void showPublishSuccess() {
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setContext(getNavigationActivity())
                .setStatus(DialogFancy.SUCCESS)
                .setTitle(R.string.text_success_publication)
                .setOnAcceptClick(v -> getNavigationActivity().onBackPressed())
                .build().show();
    }

    /**
     * Se muestra en caso de que ocurra un error con la BD
     * y no se pueda subir la noticia
     */
    public void showPublishError() {
        DialogFancy.Builder builder = new DialogFancy.Builder();
        builder .setContext(getContext())
                .setStatus(DialogFancy.ERROR)
                .setTitle(R.string.text_error_publication);
        builder .build().show();
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
        }
    }

    /**
     * Actualiza la previsualización de la imagen que se
     * va a subir junto con la noticia
     * @param imageLocalPath: Ruta de la imagen en la SD
     */
    public void updatePreviewImage(String imageLocalPath){
        this.imageLocalPath = imageLocalPath;
        if (imageLocalPath != null) {
            loadThumbnail();
            publishImage.setVisibility(View.VISIBLE);
            publishImageUpload.setVisibility(View.GONE);
        }
        else {
            publishImage.setVisibility(View.GONE);
            publishImageUpload.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Carga la imagen en su vista
     */
    public void loadThumbnail(){
        Glide.with(this)
                .load(imageLocalPath)
                .apply(PhotoFragment.getRequestOptions(getContext()))
                .into(publishImage);
    }

}
