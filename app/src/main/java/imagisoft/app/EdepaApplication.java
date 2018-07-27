package imagisoft.app;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;


public class EdepaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
