package edepa.app;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import edepa.settings.SettingsLanguage;


public class EdepaApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        SettingsLanguage.applyLanguage(this);
        super.onCreate();
    }

}
