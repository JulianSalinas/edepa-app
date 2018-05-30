package imagisoft.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import imagisoft.model.Preferences;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.ActivityNavigation;
import imagisoft.modelview.R;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Bundle args = intent.getExtras();

        boolean receive = Preferences.getInstance()
                .getBooleanPreference(context, Preferences.ALARM_STATE_KEY_VALUE);

        if(args != null && receive) {

            ScheduleEvent event = args.getParcelable("event");
            String title = context.getResources().getString(R.string.text_remainder);
            String content = event.getTitle();
            Notification notification = createNotification(title, content);
            showNotification(notification);

        }

//        int type = RingtoneManager.TYPE_ALARM;
//        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type);
//        Ringtone ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri);
//        if (ringtoneSound != null) ringtoneSound.play();

    }

    public Notification createNotification(String title, String content){

        Bundle args = new Bundle();
        args.putInt("currentResource", R.id.nav_schedule);

        Intent intent = new Intent(context, ActivityNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtras(args);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(context, "channel_key")
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_edepa_white)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).build();
    }

    public void showNotification(Notification notification){

        Object service = context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;

        assert manager != null;
        createNotificationChannel(manager);
        manager.notify(0, notification);

    }

    public void createNotificationChannel(NotificationManager manager){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "channel_key", "remainder_key",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);

        }

    }


}
