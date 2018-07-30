package imagisoft.modelview.views;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ImageFragment extends MainFragment {

    private String imageUrl;

    @BindView(R.id.photo_view)
    PhotoView imageView;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param savedInstanceState: No se utiliza
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null && args.containsKey("imageUrl"))
            imageUrl = args.getString("imageUrl");
    }

    @Override
    public void setupResource() {
        this.resource = R.layout.image_fragment;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     */
    @Override
    public void setupActivityView() {
        setToolbarText(R.string.text_map);
        // setTabLayoutVisibility(View.GONE);

        if(imageUrl != null) {
            Glide.with(getMainActivity()).load(imageUrl)
                    .apply(fitCenterTransform()
                            .placeholder(R.drawable.img_not_available)
                            .error(R.drawable.img_not_available)
                            .priority(Priority.HIGH)).into(imageView);
        }
    }

}
