package imagisoft.modelview.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ImageFragment extends MainFragment {

    protected String title;
    protected String imageUrl;

    @BindView(R.id.photo_view)
    PhotoView imageView;

    @Override
    public int getResource() {
        return R.layout.image_fragment;
    }

    public static Fragment newInstance(String imageUrl){
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        Fragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(String title, String imageUrl){
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("imageUrl", imageUrl);
        Fragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Se define cúal es el layout que va a utilizar
     * @param savedInstanceState: No se utiliza
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.VISIBLE);
        if(title != null) setToolbarText(title);
        else setToolbarText(R.string.text_image);
        setStatusBarColorRes(R.color.app_primary_dark);
        if(imageUrl != null) loadAsyncImage();
    }

    protected void loadAsyncImage(){
        Glide.with(getMainActivity())
                .load(imageUrl)
                .apply(getRequestOptions())
                .into(imageView);
    }

    private RequestOptions getRequestOptions(){
        return fitCenterTransform()
                .placeholder(R.drawable.img_not_available)
                .error(R.drawable.img_not_available)
                .priority(Priority.HIGH);
    }

}
