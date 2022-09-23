package edepa.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.ListenerService;

import java.util.Map;

import edepa.cloud.Cloud;
import edepa.modelview.R;

public abstract class UploadImageService extends ListenerService {

    protected NotificationCompat.Builder notificationBuilder;

    @Override
    public IBinder onBind(Intent intent) {
        // Es necesario que este
        return null;
    }

    /**
     * Obtiene del sistema el servicio de notificaciones y
     * muestra la noticación en la parte superior de la pantalla
     * @param notification: Notificación
     * @see #createNotificationChannel(NotificationManager)
     */
    protected void showNotification(Notification notification){
        Object service = this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;
        if(manager != null) {
            createNotificationChannel(manager);
            manager.notify(0, notification);
        }
    }

    /**
     * Crea la notificación con el contenido que se debe mostrar
     * @param title: Título del mensaje
     * @param body: Contenido del mensaje
     * @return Notificación
     */
    public NotificationCompat.Builder createNotificationBuilder(String title, String body){
        return new NotificationCompat
                .Builder(this, Cloud.NEWS)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.edepa_primary))
                .setSmallIcon(R.drawable.img_notification)
                .setAutoCancel(true);
    }

    /**
     * Se crear un canal de notificaciones de tal forma que
     * el usuario puede habilitar o deshabilitar ese canal para
     * prohibir la recepción de notificaciones
     * @param manager: Manejador de notificaciones
     */
    private void createNotificationChannel(NotificationManager manager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ImageUpload", "ImageUpload",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * La subida de la imagen ha iniciado
     * @param requestId: Id de la solicitud de subida
     */
    @Override
    public void onStart (String requestId){
        Log.i("Upload", String.format("Request id %s", requestId));
    }

    /**
     * Se va mostrando el proceso para subir la imagen
     * @param requestId: Id de la solicitud de subida
     */
    @Override
    public void onProgress (String requestId,long bytes, long totalBytes){

        Double progress = (double) bytes / totalBytes;
        int readableProgress = (int) Math.round(progress * 100);

        if (notificationBuilder == null) {
            String waiting = getString(R.string.text_wait_please);
            String uploading = getString(R.string.text_uploading_image);
            notificationBuilder = createNotificationBuilder(uploading, waiting);
        }

        notificationBuilder.setProgress(100, readableProgress, false);
        showNotification(notificationBuilder.build());
        Log.i("Upload", String.format("Progress for %s is %d", requestId, readableProgress));
    }

    /**
     * La imagen se ha sudio correctamente
     * @param requestId: Id de la solicitud de subida
     * @param resultData: Contiene la url donde se subió la imagen
     */
    @Override
    public void onSuccess (String requestId, Map resultData){
        String imageUrl = (String) resultData.get("url");
        Log.i("Upload", String.format("Successful upload %s", imageUrl));
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) notificationManager.cancelAll();
        stopSelf();
    }

    /**
     * La imagen excede el tamaño máximo y por ello no se pudo
     * subir al servidor. También se puede dar porque el servidor no tiene
     * el espacio suficiente o se excedió el plan
     * @param requestId: Id de la solicitud de subida
     * @param error: Contiene el código del error para dar el mensaje respectivo
     */
    @Override
    public void onError (String requestId, ErrorInfo error){
        Log.e("Upload", String.format("Error while uploading %s", error.getDescription()));
    }

    /**
     * La imagen no se ha podido subir pero se reintenta una segunda vez
     * @param requestId: Id de la solicitud de subida
     * @param error: Contiene el código del error para dar el mensaje respectivo
     */
    @Override
    public void onReschedule (String requestId, ErrorInfo error){
        // TODO: No soportado
    }

    /**
     * El proceso ha terminado y se destruye el servicio
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Upload", "Destroying service");
    }

}
