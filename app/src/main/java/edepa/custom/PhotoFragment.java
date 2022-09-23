package edepa.custom;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import edepa.modelview.R;

/**
 * Es importante para abrir imagenes dentro de la
 * aplicación
 */
public class PhotoFragment extends CustomFragment {

    /**
     * Se muestra en NavigationActivity{@link #getToolbar()}
     * como título de la imagen
     */
    protected String title;

    /**
     * Es la ubicación en internet de donde {@link Glide}
     * tiene que descargar la imagen
     */
    protected String imageUrl;

    /**
     * Vista donde se coloca la imagen. Esta clase
     * permite al usuario realizar zoom utilizando
     * los gestos comunes para tal fin
     */
    @BindView(R.id.photo_view)
    PhotoView imageView;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.util_photo_screen;
    }

    /**
     * Se proporciona este contructor para obligar
     * a que {@link #imageUrl} no se null y así Glide
     * no deje la imagen en blanco
     * @param imageUrl: Dirección web de la imagen
     * @return PhotoFragment
     */
    public static Fragment newInstance(String imageUrl){
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        Fragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Hace lo mismo que {@link PhotoFragment#newInstance(String)}
     * pero además se coloca un título personalizado a la imagen
     * @param title: Título de la imagen
     * @param imageUrl: Dirección web de la imagen
     * @return PhotoFragment
     */
    public static Fragment newInstance(String title, String imageUrl){
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("imageUrl", imageUrl);
        Fragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     * Se recuperan los argumentos pasados en
     * {@link PhotoFragment#newInstance(String)}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            title = args.getString("title");
            imageUrl = args.getString("imageUrl");
        }
    }

    /**
     * Si la url no es nula se carga la imagen de dicha url
     * Además si no se fue pasado un título, se coloca
     * el texto "Imagen" pode defecto
     * @param savedInstanceState: Argumentos guardados
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.GONE);
        if(title != null) setToolbarText(title);
        else setToolbarText(R.string.text_image);
        setStatusBarColorRes(R.color.app_primary_dark);
        if(imageUrl != null) loadAsyncImage();
    }

    /**
     * {@link Glide} carga la imagen de la url de forma
     * asincrona de forma que se puede usar la aplicación
     * mientras se descarga
     */
    public void loadAsyncImage(){
        Glide.with(getNavigationActivity())
                .load(imageUrl)
                .apply(getRequestOptions(getContext()))
                .into(imageView);
    }

    /**
     * Crea opciones utilizadas por {@link #loadAsyncImage()}
     * por si no se encuentra una imagen en la url específicada
     * @return RequestOptions con el placeholder de la imagen y
     * otro por si ocurre un error
     */
    public static RequestOptions getRequestOptions(Context context){
        return fitCenterTransform()
                .placeholder(getLoadingDrawable(context))
                .error(R.drawable.img_unavailable)
                .priority(Priority.HIGH);
    }

    /**
     * Se obtiene un gif para colocar mientras la imagen está cargado
     * @return CircularProgressDrawable
     */
    public static CircularProgressDrawable getLoadingDrawable(Context context){
        CircularProgressDrawable circularProgressDrawable =
                new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

}
