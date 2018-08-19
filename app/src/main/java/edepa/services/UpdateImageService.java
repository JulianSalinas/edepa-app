package edepa.services;

import android.util.Log;

import java.util.Map;
import edepa.cloud.Cloud;
import edepa.model.Preferences;
import edepa.modelview.R;
import edepa.notices.NoticeEditor;


public class UpdateImageService extends UploadImageService {

    private String noticeId;

    /**
     * La subida de la imagen ha iniciado. Se toman los parámetros que
     * fueron colocados desde {@link NoticeEditor#uploadImage()}
     * @param requestId: Id de la solicitud de subida
     */
    @Override
    public void onStart (String requestId){
        Log.i("Upload", String.format("Request id %s", requestId));
        noticeId = Preferences.getStringPreference(this, requestId);
    }

    /**
     * La imagen se ha sudio correctamente
     * @param requestId: Id de la solicitud de subida
     * @param resultData: Contiene la url donde se subió la imagen
     */
    @Override
    public void onSuccess (String requestId, Map resultData){
        String imageUrl = (String) resultData.get("url");

        notificationBuilder = createNotificationBuilder(
                getString(R.string.text_image_uploaded),
                getString(R.string.text_notice_updated));
        showNotification(notificationBuilder.build());

        Log.i("Upload", String.format("Successful upload %s", imageUrl));
        if (noticeId != null){
            Cloud.getInstance().getReference(Cloud.NEWS)
                    .child(noticeId).child("imageUrl").setValue(imageUrl);
            Preferences.removePreference(this, noticeId);
        }

    }

}
