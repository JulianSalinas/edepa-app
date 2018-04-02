package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;


public class InformationMap extends MainViewFragment {

    private final String imgRefString = "gs://rommie-91186.appspot.com/edepa_map.png";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.information_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        View view = getView();
        assert view != null;

        PhotoView imgMap = view.findViewById(R.id.img_map);
        imgMap.setImageResource(R.drawable.img_edepa_map);

        StorageReference imgRef = getFirebase()
                .getStorage()
                .getReferenceFromUrl(imgRefString);

        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(imgRef)
                .into(imgMap);

    }


}
