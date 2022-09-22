package edepa.info;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edepa.cloud.CloudCongress;
import edepa.model.Congress;
import edepa.model.Location;

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
