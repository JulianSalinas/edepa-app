package edepa.app;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import edepa.minilibs.DialogFancy;
import edepa.minilibs.OnlineHelper;
import edepa.modelview.R;

/**
 * Solo se usa la autenticación, sin embargo el método
 * setPersistenceEnable debe ser llamado antes que
 * cualquier otra función de Firebase, de lo contrario
 * la app se cierra inesperadamente
 */
public class SignInActivity extends AppCompatActivity {

    private Bundle args;

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

        if(getIntent().getExtras() != null)
            args = getIntent().getExtras();

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
        new DialogFancy.Builder()
                .setContext(this)
                .setTitle(R.string.text_no_connection)
                .setContent(R.string.text_you_need_internet)
                .setOnAcceptClick(v -> recreate())
                .setOnCancelListener(v -> exit())
                .build().show();
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
                getApplication(),
                NavigationActivity.class);

        if (args != null)
            intent.putExtras(args);

        startActivity(intent);
        finish();

    }

}
