package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;


public class InformationMap extends MainViewFragment {

    private final String imgRefString = "gs://rommie-91186.appspot.com/edepa_map.png";
    private StorageReference imgRef;

    private PhotoView miniMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.information_map, container, false);
        miniMap = view.findViewById(R.id.img_map);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        miniMap.setImageResource(R.drawable.img_edepa_map);

        imgRef = getFirebase()
                .getStorage()
                .getReferenceFromUrl(imgRefString);

        loadMinimapAsync();

    }

    private void loadMinimapAsync(){
        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(imgRef)
                .into(miniMap);
    }

}
