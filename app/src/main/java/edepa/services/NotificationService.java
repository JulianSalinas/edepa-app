package edepa.services;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Map;

import edepa.app.NavigationActivity;
import edepa.app.SignInActivity;
import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.model.Preferences;

/**
 * Servicion necesario para recibir notificaciones que provienen de la sección
 * de noticias. Las notificaciones son enviadas por Cloud Functions
 */
public class NotificationService extends FirebaseMessagingService {

    /**
     * Para pasa como parametros
     */
    public static int NOTIF_FOR_NEW = 0;
    public static int NOTIF_FOR_MESSAGE = 1;

    /**
     * Recibe las notificaciones enviadas por CloudFunctions
     * @param remoteMessage: Contiene la notificación
     * @see #handleNotification
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        // Se registra la notificación obtenida
        Log.i("Notifications", "New notification received");
        for (String key : data.keySet()){
            Log.i("Notification params", data.get(key));
        }

        // Se obtiene si la notificación es de una noticia o del chat
        int type = data.containsKey(Cloud.NEWS) ? NOTIF_FOR_NEW : NOTIF_FOR_MESSAGE;

        // La notificación es de la seccion de noticias
        if (type == NOTIF_FOR_NEW){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (notification != null) {
                String title = notification.getTitle();
                String body = notification.getBody();
                handleNotification(title, body, type);
            }
        }

        // La notificación es de la sección del chat
        else {

            // Se evita recibir notificaciones de sí mismo
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && !data.get("userid").equals(user.getUid())) {
                String text = "Tienes %d mensajes sin leer";
                String title = String.format(text, increaseUnreadMessages());
                String body = data.get("content");
                if (body == null || body.isEmpty()) {
                    body = String.format("Nueva imagen de %s", data.get("username"));
                }
                handleNotification(title, body, type);
            }

        }
    }

    /**
     * Cuando se recibe la notificación se revisa que el usuario
     * tenga el permiso para mostrarla (según las preferencias )
     * @param title: Título de la notificación
     * @param body: Contenido de la notificación
     * @see #showNotification(Notification, int)
     */
    private void handleNotification(String title, String body, int type){
        String key = Preferences.NOTIFICATIONS_KEY;
        if(Preferences.getBooleanPreference(this, key)) {
            Notification notification = createNotification(title, body, type);
            showNotification(notification, type);
        }
    }

    /**
     * Obtiene del sistema el servicio de notificaciones y
     * muestra la noticación en la parte superior de la pantalla
     * @param notification: Notificación
     * @see #createNotificationChannel(NotificationManager, String)
     */
    private void showNotification(Notification notification, int type){
        Object service = this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;
        if(manager != null) {
            String name = type == NOTIF_FOR_NEW ? Cloud.NEWS : Cloud.CHAT;
            createNotificationChannel(manager, name);
            manager.notify(type, notification);
        }
    }

    /**
     * Crea el evento que se ejecuta cuando se hace click
     * en la notificación
     * @return Intent
     * @see #createPendingIntent(Bundle)
     */
    private Intent createIntent(Bundle args){
        return new Intent(getApplicationContext(), NavigationActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                      Intent.FLAG_ACTIVITY_NO_HISTORY)
            .setAction(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .putExtras(args);
    }

    /**
     * @return PendingIntent
     * @see #createIntent(Bundle)
     */
    private PendingIntent createPendingIntent(Bundle args){
        return PendingIntent.getActivity(
                this, 0, createIntent(args),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Crea la notificación con el contenido que se debe mostrar
     * @param title: Título del mensaje
     * @param body: Contenido del mensaje
     * @return Notificación
     * @see #createPendingIntent(Bundle)
     */
    public Notification createNotification(String title, String body, int type){
        Bundle args = new Bundle();
        if (type == NOTIF_FOR_NEW) {
            args.putBoolean(Cloud.NEWS, true);
            return new NotificationCompat
                    .Builder(this, Cloud.NEWS)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.edepa_primary))
                    .setSmallIcon(R.drawable.img_notification)
                    .setContentIntent(createPendingIntent(args))
                    .setAutoCancel(true).build();
        }
        else {
            args.putBoolean(Cloud.CHAT, true);
            return new NotificationCompat
                    .Builder(this, Cloud.CHAT)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setNumber(getUnreadMessages())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.edepa_primary))
                    .setSmallIcon(R.drawable.img_notification)
                    .setContentIntent(createPendingIntent(args))
                    .setAutoCancel(true).build();
        }
    }

    public int getUnreadMessages(){
        String key = Preferences.UNREAD_MESSAGES_KEY;
        return Preferences.getIntegerPreference(this, key);
    }

    public int increaseUnreadMessages(){
        String key = Preferences.UNREAD_MESSAGES_KEY;
        int current = getUnreadMessages();
        Preferences.setPreference(this, key, current + 1);
        return current + 1;
    }

    /**
     * Se crear un canal de notificaciones de tal forma que
     * el usuario puede habilitar o deshabilitar ese canal para
     * prohibir la recepción de notificaciones
     * @param manager: Manejador de notificaciones
     */
    private void createNotificationChannel(NotificationManager manager, String name){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    name, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
        }
    }

}
