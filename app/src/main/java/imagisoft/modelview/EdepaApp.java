package imagisoft.modelview;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.FirebaseDatabase;

import imagisoft.model.FavoriteList;
import imagisoft.model.ViewedList;

public class EdepaApp extends Application
        implements Application.ActivityLifecycleCallbacks {

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        handler = new Handler(getMainLooper());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        FavoriteList.getInstance().load(activity);
        ViewedList.getInstance().load(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        handler.post(() -> {
            FavoriteList.getInstance().save(activity);
            ViewedList.getInstance().save(activity);
        });
    }

}
