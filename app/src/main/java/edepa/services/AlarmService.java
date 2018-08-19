package edepa.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class AlarmService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {

        assert job.getExtras() != null;
        String json = job.getExtras().getString("Event");
//        Event event = Event.fromJson(json);


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("TAG", "Job cancelled!");
        return false;
    }

    //    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        this.context = context;
//        Bundle args = intent.getExtras();
//
//        boolean receive = Preferences.getInstance()
//                .getBooleanPreference(context, Preferences.NOTIFICATIONS_KEY);
//
//        if(args != null && receive) {
//
////            Event event = args.getParcelable("event");
////            String title = context.getResources().getString(R.string.text_remainder);
////            String content = event.getTitle();
////            Notification notification = createNotificationBuilder(title, content);
////            showNotification(notification);
//
//        }
//
////        int type = RingtoneManager.TYPE_ALARM;
////        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type);
////        Ringtone ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri);
////        if (ringtoneSound != null) ringtoneSound.play();
//
//    }
//
//
//    public void showNotification(Notification notification){
//
//        Object service = context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationManager manager = (NotificationManager) service;
//
//        assert manager != null;
//        createNotificationChannel(manager);
//        manager.notify(0, notification);
//
//    }
//
//    public void createNotificationChannel(NotificationManager manager){
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            NotificationChannel channel = new NotificationChannel(
//                    "channel_key", "remainder_key",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            channel.setLightColor(Color.RED);
//            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            manager.createNotificationChannel(channel);
//
//        }
//
//    }


}