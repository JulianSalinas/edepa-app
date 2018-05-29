package imagisoft.modelview;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class EdepaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
