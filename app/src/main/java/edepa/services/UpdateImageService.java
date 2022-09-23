package edepa.services;

import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

import edepa.cloud.Cloud;
import edepa.model.Preferences;
import edepa.notices.NoticeEditor;


public class UpdateImageService extends UploadImageService {

    /**
     * json[0] = requestId
     * json[1] = objectKey
     * json[2] = Cloud.CHAT || Cloud.NEWS
     */
    private JSONObject json;

    public static final String REQUEST_ID = "requestId";
    public static final String OBJECT_KEY = "objectKey";
    public static final String CLOUD_TYPE = "cloudType";

    /**
     * La subida de la imagen ha iniciado. Se toman los parámetros que
     * fueron colocados desde {@link NoticeEditor#uploadImage()}
     * @param requestId Id de la solicitud de subida
     */
    @Override
    public void onStart (String requestId){
        super.onStart(requestId);
        String text = Preferences.getStringPreference(this, requestId);
        try { json = new JSONObject(text); }
        catch (Exception exception){
            String error = "Error parsing json: ";
            Log.e(toString(), error + exception.getMessage());
        }
    }

    /**
     * La imagen se ha subido correctamente
     * @param requestId Id de la solicitud de subida
     * @param resultData Contiene la url donde se subió la imagen
     */
    @Override
    public void onSuccess (String requestId, Map resultData){
        super.onSuccess(requestId, resultData);
        try { tryToUpdatePreview(requestId, resultData); }
        catch (Exception exception){
            String error = "Error updating object preview: ";
            Log.e(toString(), error + exception.getMessage());
        }
    }

    /**
     * Usada por el método {@link #onSuccess(String, Map)}. Se leen los
     * párametros pasados a través del json {@link #json} y se intenta actualizar
     * la base de datos con esa información
     * @param requestId Id de la solicitud de subida
     * @param resultData Contiene la url donde se subió la imagen
     * @throws Exception La lectura del json ha resulta en una excepción
     */
    private void tryToUpdatePreview(String requestId, Map resultData) throws Exception{
        String imageUrl = (String) resultData.get("url");
        String objectKey = json.getString(OBJECT_KEY);
        String cloudType = json.getString(CLOUD_TYPE);
        if (objectKey != null){
            updatePreview(cloudType, objectKey, imageUrl);
            Preferences.removePreference(this, requestId);
        }
    }

    /**
     * Se actualiza la infomarmación de la BD con la información de la imagen
     * que se acaba de subir. Es usada solo por {@link #tryToUpdatePreview(String, Map)}
     * @param cloudType Cloud.CHAT || Cloud.NEWS
     * @param objectKey Key del objeto que contiene la preview que se debe actualizar
     * @param imageUrl Url de la imagen que se debe colocar en la preview
     */
    public void updatePreview(String cloudType, String objectKey, String imageUrl){
        Cloud.getInstance()
                .getReference(cloudType)
                .child(objectKey)
                .child("preview")
                .child("url")
                .setValue(imageUrl);
    }

}
