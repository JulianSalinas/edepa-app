package edepa.services;

import android.util.Log;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Map;
import java.util.Set;

import edepa.cloud.Cloud;
import edepa.model.Preferences;
import edepa.notices.NoticeEditor;


public class UpdateImageService extends UploadImageService {

    /**
     * args[0] = requestId
     * args[1] = objectKey
     * args[2] = Cloud.CHAT || Cloud.NEWS
     */
    private JSONObject args;

    public static final String REQUEST_ID = "requestId";
    public static final String OBJECT_KEY = "objectKey";
    public static final String CLOUD_TYPE = "cloudType";

    /**
     * La subida de la imagen ha iniciado. Se toman los parámetros que
     * fueron colocados desde {@link NoticeEditor#uploadImage()}
     * @param requestId: Id de la solicitud de subida
     */
    @Override
    public void onStart (String requestId){
        super.onStart(requestId);
        String text = Preferences.getStringPreference(this, requestId);
        try {
            args = new JSONObject(text);
        }
        catch (Exception exception){
            args = null;
            Log.i(toString(), "Error parsing json: " + exception.getMessage());
        }
    }

    /**
     * La imagen se ha subido correctamente
     * @param requestId: Id de la solicitud de subida
     * @param resultData: Contiene la url donde se subió la imagen
     */
    @Override
    public void onSuccess (String requestId, Map resultData){
        super.onSuccess(requestId, resultData);

        try {
            String imageUrl = (String) resultData.get("url");
            String objectKey = args.getString(OBJECT_KEY);
            String cloudType = args.getString(CLOUD_TYPE);

            if (objectKey != null){
                Cloud.getInstance()
                        .getReference(cloudType)
                        .child(objectKey)
                        .child("preview")
                        .child("url")
                        .setValue(imageUrl);
                Preferences.removePreference(this, requestId);
            }

        }
        catch (Exception exception){
            String error = "Error updating object preview: ";
            Log.e(toString(), error + exception.getMessage());
        }

    }

}
