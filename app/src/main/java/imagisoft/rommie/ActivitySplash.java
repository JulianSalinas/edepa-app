package imagisoft.rommie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class ActivitySplash extends AppCompatActivity{

    private View contentView;

    /**
     * Esconde las propiedades de la pantalla y solo muesrta la imagen de carga,
     * o conocida como splash screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        contentView = findViewById(R.id.fullscreen_content);

        // Esconde los items que no son importantes en la pantalla
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        loadApplication();

    }

    /**
     * Se coloca el gif de cargar la aplicacioón
     * TODO: Esta pantalla se aprovecha para cargar en la aplicación
     * TODO: Se usa para comprobar la conoxión a internet
     */
    private void loadApplication() {

        // Pone a moverse el gif de cargar la aplicación
        ImageView loading_gif = findViewById(R.id.gif_splash_loading);
        GlideDrawableImageViewTarget viewTerget = new GlideDrawableImageViewTarget(loading_gif);
        Glide.with(this).load(R.drawable.img_loading).into(viewTerget);

        // TODO: Aqui va el código para cargar los datos necesarios al inicio
        initApplication();
        finish();
    }

    /**
     * Se utiliza solo para debuggear, el usuario presiona la pantalla y
     * se pasa al cronograma
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean defaultResult = super.onTouchEvent(event);
        initApplication();
        return defaultResult;
    }

    /**
     * Después de haber cargado los datos de la aplicación, se utiliza está función para abrirla
     */
    private void initApplication(){
        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(intent);
    }

}
