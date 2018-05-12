package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;


public class InformationMap extends MainActivityFragment {

    private String imgKey;
    private String imgRef;

    @BindView(R.id.img_map)
    PhotoView miniMap;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.imgKey = "minimap";
        resource = R.layout.information_map;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        setToolbarText(R.string.text_map);
        setTabLayoutVisibility(View.GONE);

        activity.getConfigReference().child(imgKey)
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgRef = dataSnapshot.getValue(String.class);
                loadMinimapAsync();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                miniMap.setImageResource(R.drawable.img_edepa_map);
            }

        });

    }

    /**
     * Carga el croquis en segundo plano para que la transición
     * se fluida
     */
    private void loadMinimapAsync(){
        Glide.with(activity)
                .load(imgRef)
                .into(miniMap);
    }

}
