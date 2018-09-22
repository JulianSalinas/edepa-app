package edepa.info;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import edepa.cloud.CloudCongress;
import edepa.custom.CustomFragment;
import edepa.model.Congress;
import edepa.model.Location;
import edepa.modelview.R;

/**
 * Mapa que se muestra al presionar el mapa pequeño
 * del {@link InfoFragment}
 */
public class FullMapInfoFragment extends FullMapFragment
        implements OnMapReadyCallback, CloudCongress.Callbacks{

    /**
     * Información general del congreso
     */
    protected Congress congress;
    private CloudCongress cloudCongress;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Para que la información se actualice en tiempo real
        // y no cada vez que se abre la aplicación
        if(cloudCongress == null) {
            cloudCongress = new CloudCongress();
            cloudCongress.setCallbacks(this);
        }

    }

    /**
     * Se conecta con la nube para recibir cambios
     * en el congreso
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectCloud(){
        cloudCongress.connect();
    }

    /**
     * Se desconecta para dejar de recibir cambios
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectCloud(){
        cloudCongress.disconnect();
    }


    /**
     * Se sincroniza la información local con Firebase
     * @param congress: Información del congreso
     */
    public void updateCongress(Congress congress){
        this.congress = congress;
        tag = congress.getLocationTag();
        location = new Location();
        location.setLat(congress.getyCoord());
        location.setLng(congress.getxCoord());
        updateLocation();
    }

}
