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
import imagisoft.modelview.R;
import imagisoft.model.Preferences;
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
     * Conexión con Firebase para poder autenticar
     */
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    private Preferences prefs = Preferences.getInstance();

    /**
     * Esconde las propiedades de la pantalla y solo muestra la
     * imagen de carga, o conocida como splash screen. Luego al
     * presionar muestra el login
     */
    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        // Mantiene el splash_screen mientras
        // se trata de ejecutar el login
        setTheme(R.style.AppTheme);
        setContentView(R.layout.splash_screen);
        this.auth = FirebaseAuth.getInstance();

        // Si el usuario ya se ha tiene una sesión
        // abierta, no es necesario que este online
        if(auth.getCurrentUser() != null)
            startApplication();

        // Si no tiene sesión debe de hacer login
        else if (isOnline())
            startLoginActivity();

        // Si no tiene sesión ni internet se muestra
        // una alerta y no se inicia la aplicación
        else showOfflineAlert();

    }

    /**
     * Si es la primera vez en abrirse la aplicación
     * después de instalarla o hacer signout es necesario
     * tener internet. De lo contrario se utiliza ésta
     * función para mostrar la alerta correspondiente
     * TODO: Se puede cambiar por un fragment personalizado
     */
    private void showOfflineAlert(){
        new AlertDialog.Builder(this)
            .setTitle(R.string.text_no_connection)
            .setMessage(R.string.text_you_need_internet)
            .setPositiveButton(R.string.text_retry,
                    (dialog, which) -> recreate())
            .setNegativeButton(R.string.nav_exit,
                    (dialog, which) -> exit())
            .show();
    }

    /**
     * Cierra la aplicación por completo
     * ni siquiera la deja en segundo plano
     */
    private void exit(){
        finishAndRemoveTask();
        System.exit(0);
    }

    /**
     * Revisa si existe conexión a internet
     * @return True si el teléfono está conectado
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm == null ? null : cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Se inicia la pantalla de Login
     * Solo sucede si es la primera vez que se abre la aplicación
     * Luego los datos quedan registrados y no es necesario
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
     * De lo contrario, la aplicación no avanza
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            FirebaseUser user = auth.getCurrentUser();
            if(user != null) startApplication();
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
