package edepa.info;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edepa.cloud.Cloud;
import edepa.custom.PhotoFragment;


/**
 * Muestra el croquis o mapa (imagen) según una posición
 * ya fijada en firebase /config/minimap
 */
public class FullMinimapFragment
        extends PhotoFragment implements ValueEventListener {

    /**
     * Nombre de la clave donde se encuentra la imagen
     * en la BD Firebase
     */
    public static final String IMG_KEY = "minimap";

    /**
     * {@inheritDoc}
     * Se agrega el adminPermissionListener para obtener la url del mapa
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        loadAsyncImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

}
