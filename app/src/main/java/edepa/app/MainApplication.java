package edepa.app;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
import edepa.settings.SettingsLanguage;


public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SettingsLanguage.applyLanguage(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
