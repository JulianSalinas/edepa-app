package edepa.services;

import java.util.Map;
import edepa.cloud.Cloud;
import edepa.model.Preferences;
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
        super.onStart(requestId);
        noticeId = Preferences.getStringPreference(this, requestId);
    }

    /**
     * La imagen se ha sudio correctamente
     * @param requestId: Id de la solicitud de subida
     * @param resultData: Contiene la url donde se subió la imagen
     */
    @Override
    public void onSuccess (String requestId, Map resultData){
        super.onSuccess(requestId, resultData);
        String imageUrl = (String) resultData.get("url");
        if (noticeId != null){
            Cloud.getInstance().getReference(Cloud.NEWS)
                    .child(noticeId).child("imageUrl").setValue(imageUrl);
            Preferences.removePreference(this, noticeId);
        }

    }

}
