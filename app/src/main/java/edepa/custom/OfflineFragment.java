package edepa.custom;

import android.view.View;
import android.os.Handler;
import android.widget.ProgressBar;
import android.support.v7.widget.CardView;
import android.support.v7.app.AppCompatActivity;

import edepa.app.BaseFragment;
import edepa.modelview.R;
import butterknife.BindView;
import butterknife.OnClick;


public class OfflineFragment extends BaseFragment {

    /**
     * Se debe esperar tres segundos antes de que el botón
     * de reintentar se puede presionar otra vez
     */
    public static final long RETRY_DELAY = 3000;

    @BindView(R.id.retry_button)
    CardView retryButton;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public int getResource() {
        return R.layout.offline_screen;
    }

    /**
     * Al tocar el botón de reintentar se recrea la actividad esperando
     * que pueda conectarse a la red
     */
    @OnClick(R.id.retry_button)
    public void retryConnection(){
        showProgress(true);
        Handler handler = new Handler();
        handler.postDelayed(this::recreateActivity, RETRY_DELAY);
    }

    /**
     * Se recrea la actividad padre, esto permite que la aplicación
     * se trate de conectar a la red
     */
    private void recreateActivity(){
        if (getContext() instanceof AppCompatActivity) {
            showProgress(false);
            ((AppCompatActivity) getContext()).recreate();
        }
    }

    /**
     * Cunado se presiona el botón de reintentar se invoca este método
     * remplando el botón por una barra de progreso que tiene una duración
     * igual a {@link #RETRY_DELAY} según {@link #retryConnection()}
     * @param visible: true si la barra de progresso debe estar visible
     */
    private void showProgress(boolean visible){
        retryButton.setVisibility(visible ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
