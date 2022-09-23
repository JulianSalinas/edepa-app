package edepa.app;

import com.cloudinary.android.MediaManager;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import edepa.settings.SettingsLanguage;


public class MainApplication extends android.app.Application {

    /**
     * {@inheritDoc}
     * Se aplica el idioma desde las preferencias y se configurar las bases
     * de datos
     */
    @Override
    public void onCreate() {
        super.onCreate();
        SettingsLanguage.applyLanguage(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        initCloudinary();
    }

    /**
     * Se inicializa la base de datos de imagenes Cloudinary
     */
    public void initCloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "edepa");
        MediaManager.init(this, config);
    }

}
