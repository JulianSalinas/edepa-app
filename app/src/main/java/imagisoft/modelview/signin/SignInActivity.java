package imagisoft.modelview.signin;

import android.os.Bundle;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import imagisoft.model.Preferences;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainNavigation;
import static imagisoft.model.Preferences.USER_ID_KEY;

/**
 * Solo se usa la autenticación, sin embargo el método
 * setPersistenceEnable debe ser llamado antes que
 * cualquier otra función de Firebase, de lo contrario
 * la app se cierra inesperadamente
 */
public class SignInActivity extends AppCompatActivity {

    /**
     * Conexión con Firebase
     */
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    private Preferences prefs = Preferences.getInstance();

    /**
     * Esconde las propiedades de la pantalla y solo muesrta la
     * imagen de carga, o conocida como splash screen. Luego al
     * presionar muestra el login
     */
    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setTheme(R.style.AppTheme);
        setContentView(R.layout.splash_screen);
        this.auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null)
            startApplication();
        else if (isOnline())
            startLoginActivity();
        else showOfflineAlert();
    }

    /**
     * Si es la primera vez en abrirse la aplicación
     * después de instalarla o hacer signout es necesario
     * tener internet
     */
    private void showOfflineAlert(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_no_connection)
                .setMessage(R.string.text_you_need_internet)
                .setPositiveButton(R.string.text_retry, (dialog, which) -> recreate())
                .setNegativeButton(R.string.nav_exit, (dialog, which) -> {
                    finishAndRemoveTask();
                    System.exit(0);
                }).show();
    }

    /**
     * Revisa si existe conexión a internet
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm == null ? null : cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
            FirebaseUser user = auth.getCurrentUser();
            if(user != null) {
                prefs.setPreference(this, USER_ID_KEY, user.getUid());
                startApplication();
            }
            else showOfflineAlert();
        }
    }

    /**
     * Después de haber cargado los datos de la aplicación,
     * se utiliza está función para abrirla
     */
    private void startApplication(){
        Intent intent = new Intent(
                getApplicationContext(),
                MainNavigation.class);
        startActivity(intent);
        finish();
    }

}
