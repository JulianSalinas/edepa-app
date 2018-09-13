package edepa.app;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import edepa.modelview.R;
import edepa.minilibs.OnlineHelper;
import edepa.custom.OfflineFragment;


public class SignInActivity extends AppCompatActivity {

    private Bundle args;
    private Intent intent;

    // facebookOpenSSL = "ga0RGNYHvNM5d0SLGQfpQWAPGJ8=";
    // facebookOpenSSLKeystore = "edepa0123";

    /**
     * Conexión con Firebase para poder autenticar
     */
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;

    /**
     * Esconde las propiedades de la pantalla y solo muestra la
     * imagen de carga, o conocida como splash screen. Luego al
     * presionar muestra el login
     */
    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        intent = getIntent();
        if(getIntent().getExtras() != null)
            args = getIntent().getExtras();

        // Mantiene el splash_screen mientras
        // se trata de ejecutar el login
        setTheme(R.style.AppTheme);
        setContentView(R.layout.util_splash_screen);
        this.auth = FirebaseAuth.getInstance();

        // Si el usuario ya tiene una sesión
        // abierta, no es necesario que este online
        if(auth.getCurrentUser() != null)
            startApplication();

        // Si no tiene sesión debe de hacer login
        else if (OnlineHelper.isOnline(this))
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
     */
    private void showOfflineAlert(){
        final String OFFLINE_TAG = "OFFLINE";
        Fragment fragment = new OfflineFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fullscreen_content, fragment, OFFLINE_TAG)
                .commit();
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
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.ic_edepa)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setPrivacyPolicyUrl(getString(R.string.app_privacy_policy))
                .build(), RC_SIGN_IN
        );
    }

    /**
     * Si el login es éxitoso se inicia la aplicación de lo contrario
     * la aplicación no avanza y se queda en el login
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
     * Después de haber cargado los datos de la aplicación, se utiliza este
     * metódo para abrirla, incluso se pasan los parámetros a la siguiente
     * actividad en caso de que la app sea abierta por medio de una notificación
     * u a darle compartir con 'EDEPA' a una imagen
     */
    private void startApplication(){

        Intent intent = new Intent(
                getApplication(),
                NavigationActivity.class);

        // Esto evita que cualquier aplicación intente abrir
        // esta app sin haber pasado por el login, por lo que primero se
        // pasa por aquí y luego se pasan los parámetros que iban dirigidos
        // a NavigationActivity
        if (this.intent != null) {
            intent.setAction(this.intent.getAction());
            intent.setType(this.intent.getType());
        }

        if (args != null) intent.putExtras(args);
        startActivity(intent);
        finish();

    }

}
