package imagisoft.rommie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    private Preferences prefs = Preferences.getInstance();

    /**
     * Esconde las propiedades de la pantalla y solo muesrta la imagen de carga,
     * o conocida como splash screen. Luego al presionar muestra el login
     */
    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setTheme(R.style.AppTheme);
        setContentView(R.layout.splash_screen);

        boolean isFirstUse = prefs
                .getBooleanPreference(this, Preferences.FIRST_USE_KEY_VALUE);

        if(isFirstUse && isOnline()) {
            startDatabase();
            prefs.setPreference(this, Preferences.FIRST_USE_KEY_VALUE, false);
            startLoginActivity();
        }


        else if(isFirstUse && !isOnline()){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.text_no_connection)
                    .setMessage(R.string.text_you_need_internet)
                    .setPositiveButton(R.string.text_retry, (dialog, which) -> recreate())
                    .setNegativeButton(R.string.nav_exit, (dialog, which) -> {
                        finishAndRemoveTask();
                        System.exit(0);
                    }).show();
        }

        else startApplication();

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

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.AppTheme)
                .setLogo(R.drawable.ic_edepa)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .build(), RC_SIGN_IN
        );

    }

    /**
     * Si el login es éxitoso se inicia la aplicación
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {

            if(auth.getCurrentUser() != null) {
                prefs.setPreference(this,
                        Preferences.USER_KEY_VALUE,
                        auth.getCurrentUser().getDisplayName());
            }
            else {
                prefs.setPreference(this,
                        Preferences.USER_KEY_VALUE,
                        getResources().getString(R.string.text_anonymous));
            }
            startApplication();
        }
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
