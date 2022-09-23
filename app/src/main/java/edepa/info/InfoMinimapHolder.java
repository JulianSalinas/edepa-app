package edepa.info;

import static edepa.info.FullMinimapFragment.IMG_KEY;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.cloud.Cloud;
import edepa.custom.PhotoFragment;
import edepa.modelview.R;


/**
 * Muestra el croquis o mapa (imagen) según una posición
 * ya fijada en firebase /config/minimap
 */
public class InfoMinimapHolder extends RecyclerView.ViewHolder implements ValueEventListener {

    @BindView(R.id.minimap)
    ImageView minimap;

    private String imageUrl;

    public InfoMinimapHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(){
        Cloud.getInstance()
                .getReference(Cloud.CONFIG).child(IMG_KEY)
                .addValueEventListener(this);
    }

    /**
     * {@inheritDoc}
     * Carga la imagen del mapa en segundo plano
     * para no detener la aplicación mientras se descarga
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        imageUrl = dataSnapshot.getValue(String.class);
        if (imageUrl != null) loadAsyncImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(getClass().getSimpleName(), databaseError.getMessage());
    }

    /**
     * {@link Glide} carga la imagen de la url de forma
     * asincrona de forma que se puede usar la aplicación
     * mientras se descarga
     */
    protected void loadAsyncImage(){
        Context context = itemView.getContext().getApplicationContext();
        Glide.with(context)
                .load(imageUrl)
                .apply(PhotoFragment.getRequestOptions(context))
                .into(minimap);
    }

}
