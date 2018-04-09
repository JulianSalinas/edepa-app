package imagisoft.rommie;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.DateFormat;
import java.util.Date;

public class AlarmService extends JobService {

    public static final String NOTIFICATION = "notification";
    public static final String NOTIFICATION_ID = "notification-id";

//    public void onReceive(Context context, Intent intent) {
//        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = intent.getParcelableExtra(NOTIFICATION);
//        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//        notificationManager.notify(id, notification);
//    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "onStartJob", Toast.LENGTH_SHORT);
        toast.show();
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("TAG", "Job cancelled!");
        return false;
    }
}