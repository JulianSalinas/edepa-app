package edepa.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;
import edepa.model.Event;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class FavoritesService extends JobService {

    public static final String JOB_TAG = "FAVORITES_JOB_TAG";
    public static final int ALARM_INTERVAL_SECONDS = (int) MINUTES.toSeconds(1);
    public static final int SYNC_FLEXTIME_SECONDS = ALARM_INTERVAL_SECONDS + (int) MINUTES.toSeconds(1);

    @Override
    public boolean onStartJob(JobParameters job) {
//        Log.i(getClass().getSimpleName(), "onStartJob");
//        CloudFavorites cloudFavorites = new CloudFavorites();
//
//        cloudFavorites.setCallbacks(new CloudFavorites.Callbacks() {
//            @Override
//            public void addFavorite(String eventKey) {
//                Log.i("scheduleJob", eventKey);
//                CloudEvents.getSingleEventQuery(eventKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Event notice = dataSnapshot.getValue(Event.class);
//                        if (notice != null) {
//                            notice.setKey(eventKey);
//                            long currentTime = System.currentTimeMillis();
//                            long timeDiff = notice.getStart() - currentTime;
//                            long minutesDiff = MILLISECONDS.toMinutes(timeDiff);
//                            Log.i(getClass().getSimpleName(),
//                                    String.format("Notifying remainder for %s at %d",
//                                            notice.getTitle(), notice.getStart()));
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void removeFavorite(String eventKey) {
//
//            }
//        });
//
//        cloudFavorites.connectLodging();
        Log.i(getClass().getSimpleName(), "miau!");
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i(getClass().getSimpleName(), "onStopJob");
        return false; // Answers the question: "Should this job be retried?"
    }

    public static void scheduleJob(FirebaseJobDispatcher mDispatcher) {
        Job job = mDispatcher.newJobBuilder()
                .setService(FavoritesService.class)
                .setTag(JOB_TAG)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        ALARM_INTERVAL_SECONDS, SYNC_FLEXTIME_SECONDS))
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        mDispatcher.mustSchedule(job);
        Log.i("scheduleJob", "job scheduled!");
    }

    public static void cancelJob(FirebaseJobDispatcher mDispatcher) {
        mDispatcher.cancelAll();
    }

}
