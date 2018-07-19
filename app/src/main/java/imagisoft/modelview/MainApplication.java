package imagisoft.modelview;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;


public class MainApplication extends Application
        implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

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

    }

}
