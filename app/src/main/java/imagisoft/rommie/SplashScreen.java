package imagisoft.rommie;

import java.util.Arrays;
import static android.os.SystemClock.sleep;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class SplashScreen extends AppCompatActivity{

    /**
     * Conexión con Firebase
     */
    final FirebaseAuth auth;
    final FirebaseDatabase database;
    private static final int RC_SIGN_IN = 123;

    /**
     * Solo se usa la autenticación, sin embargo el método
     * setPersistenceEnable debe ser llamado antes que cualquier otrsa
     * función de Firebase, de lo contrario la app se cierra inesperadamente
     */
    public SplashScreen() {

        // Guarda en persistencia para volver a descargar
        // Ayuda si la aplicación queda offline
        this.database = FirebaseDatabase.getInstance();
        this.database.setPersistenceEnabled(true);

        // No se puede mover arriba de this.database
        this.auth = FirebaseAuth.getInstance();

    }

    /**
     * Esconde las propiedades de la pantalla y solo muesrta la imagen de carga,
     * o conocida como splash screen. Luego al presionar muestra el login
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash_screen);
        View contentView = findViewById(R.id.fullscreen_content);

        // Esconde los items que no son importantes en la pantalla
        contentView.setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Pone a moverse el gif de cargar la aplicación
        ImageView loading_gif = findViewById(R.id.gif_splash_loading);
        GlideDrawableImageViewTarget viewTerget = new GlideDrawableImageViewTarget(loading_gif);
        Glide.with(this).load(R.drawable.img_loading).into(viewTerget);

        sleep(1000);

        startLoginActivity();

    }

    /**
     * Se inicia la pantalla de Login.
     * Solo sucede si es la primera vez que se abre la aplicación
     * Luego los datos quedan registrados
     */
    private void startLoginActivity() {

        // TODO: EL setIsSmartLockEnable en false es solo para debug
        if (auth.getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setTheme(R.style.LoginTheme)
                    .setLogo(R.drawable.img_edepa_logo)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                    .build(), RC_SIGN_IN
            );
        }

        else startApplicaction();
    }

    /**
     * Si el login es éxitoso se inicia la aplicación
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK)
            startApplicaction();
    }

    /**
     * Después de haber cargado los datos de la aplicación, se utiliza está función para abrirla
     */
    private void startApplicaction(){
        Intent intent = new Intent(getApplicationContext(), MainViewNavigation.class);
        startActivity(intent);
        finish();
    }

}
