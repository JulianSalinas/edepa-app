package imagisoft.rommie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import imagisoft.edepa.Preferences;

/**
 * Solo se usa la autenticación, sin embargo el método
 * setPersistenceEnable debe ser llamado antes que cualquier otra
 * función de Firebase, de lo contrario la app se cierra inesperadamente
 */
public class SplashScreen extends AppCompatActivity {

    /**
     * Conexión con Firebase
     */
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private static final int RC_SIGN_IN = 123;

    /**
     * Esconde las propiedades de la pantalla y solo muesrta la imagen de carga,
     * o conocida como splash screen. Luego al presionar muestra el login
     */
    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setTheme(R.style.AppTheme);

        Preferences prefs = Preferences.getInstance();

        boolean isFirstUse = prefs
                .getBooleanPreference(this, Preferences.FIRST_USE_KEY_VALUE);

        if(!isFirstUse || isOnline()) {

            if(isFirstUse && isOnline())
                prefs.setPreference(this, Preferences.FIRST_USE_KEY_VALUE, false);

            setContentView(R.layout.splash_screen);

        }

        else {

            setContentView(R.layout.fragment_blank);
            TextView textView = findViewById(R.id.description_text_view);
            textView.setText(R.string.text_you_need_internet);
            View content = findViewById(R.id.fullscreen_content);

            content.setOnClickListener(v -> {
                finishAndRemoveTask();
                System.exit(0);
            });

        }

        startDatabase();
        startLoginActivity();

    }

    /**
     * Revisa si existe conexión a internet
     */
    private boolean isOnline() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Solo se usa la autenticación, sin embargo el método
     * setPersistenceEnable debe ser llamado antes que cualquier otra
     * función de Firebase, de lo contrario la app se cierra inesperadamente
     */
    private void startDatabase(){

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);

        // No se puede mover arriba de this.database
        this.auth = FirebaseAuth.getInstance();

    }

    /**
     * Se inicia la pantalla de Login.
     * Solo sucede si es la primera vez que se abre la aplicación
     * Luego los datos quedan registrados
     */
    private void startLoginActivity() {

        if (auth.getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(true)
                    .setTheme(R.style.AppTheme_SplashTheme)
                    .setLogo(R.drawable.ic_edepa)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                    .build(), RC_SIGN_IN
            );
        }
        else startApplication();


    }

    /**
     * Si el login es éxitoso se inicia la aplicación
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK)
            startApplication();
    }

    /**
     * Después de haber cargado los datos de la aplicación, se utiliza está función para abrirla
     */
    private void startApplication(){
        Intent intent = new Intent(getApplicationContext(), MainActivityNavigation.class);
        startActivity(intent);
        finish();
    }

}
