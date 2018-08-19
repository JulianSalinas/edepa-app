package edepa.services;

import android.os.Build;
import android.os.Bundle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;

import edepa.app.NavigationActivity;
import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.model.Preferences;

/**
 * Servicion necesario para recibir notificaciones
 * que provienen de la sección de noticias
 * Las notificaciones son enviadas por cloudFunctions
 */
public class NotificationService extends FirebaseMessagingService {

    /**
     * Recibe las notificaciones enviadas por CloudFunctions
     * @param remoteMessage: Contiene la notificación
     * @see #handleNotification(String, String)
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            String title = notification.getTitle();
            String body = notification.getBody();
            handleNotification(title, body);
        }
    }

    /**
     * Cuando se recibe la notificación se revisa que el usuario
     * tenga el permiso para mostrarla (según las preferencias )
     * @param title: Título de la notificación
     * @param body: Contenido de la notificación
     * @see #showNotification(Notification)
     */
    private void handleNotification(String title, String body){
        String key = Preferences.NOTIFICATIONS_KEY;
        if(Preferences.getBooleanPreference(this, key)) {
            Notification notification = createNotification(title, body);
            showNotification(notification);
        }
    }

    /**
     * Obtiene del sistema el servicio de notificaciones y
     * muestra la noticación en la parte superior de la pantalla
     * @param notification: Notificación
     * @see #createNotificationChannel(NotificationManager)
     */
    private void showNotification(Notification notification){
        Object service = this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;
        if(manager != null) {
            createNotificationChannel(manager);
            manager.notify(0, notification);
        }
    }

    /**
     * Crea el evento que se ejecuta cuando se hace click
     * en la notificación
     * @return Intent
     * @see #createPendingIntent()
     */
    private Intent createIntent(){
        Bundle args = new Bundle();
        args.putBoolean(Cloud.NEWS, true);
        return new Intent(getApplicationContext(), NavigationActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                      Intent.FLAG_ACTIVITY_NO_HISTORY)
            .setAction(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .putExtras(args);
    }

    /**
     * @return PendingIntent
     * @see #createIntent()
     */
    private PendingIntent createPendingIntent(){
        return PendingIntent.getActivity(
                this, 0, createIntent(),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Crea la notificación con el contenido que se debe mostrar
     * @param title: Título del mensaje
     * @param body: Contenido del mensaje
     * @return Notificación
     * @see #createPendingIntent()
     */
    public Notification createNotification(String title, String body){
        return new NotificationCompat
                .Builder(this, Cloud.NEWS)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.edepa_primary))
                .setSmallIcon(R.drawable.img_notification)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true).build();
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
                    Cloud.NEWS, Cloud.NEWS,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
    }

}
