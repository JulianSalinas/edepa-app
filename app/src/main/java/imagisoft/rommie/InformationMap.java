package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.ValueEventListener;


public class InformationMap extends MainViewFragment {

    private String imgKey;
    private StorageReference imgRef;

    @BindView(R.id.img_map) PhotoView miniMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.imgKey = "minimap";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.information_map);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        getFirebase()
                .getConfigReference().child(imgKey)
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgRef = getFirebase()
                        .getStorage()
                        .getReferenceFromUrl(imgKey);
                loadMinimapAsync();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                miniMap.setImageResource(R.drawable.img_edepa_map);
            }

        });

    }

    private void loadMinimapAsync(){
        Glide.with(getNavigation())
                .load(imgRef)
                .into(miniMap);
    }

}
