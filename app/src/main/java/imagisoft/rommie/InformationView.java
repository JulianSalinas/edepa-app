package imagisoft.rommie;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class InformationView extends Fragment implements OnMapReadyCallback{

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    public InformationView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.information_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Se revisa si el mapa ya está en la cache para no instanciarlo de nuevo.
        // Permite mostrar el mapa aunque no haya conexión
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        // Coloca el gmap en la posición destinada para tal fin
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

    }

    private void moveMapLocation(LatLng currentLocation) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }

    /**
     * TODO: La dirección está estática, debe ser dinámica se debe cargar junto con la aplicación.
     * Se utiliza el API de google para mostrar el mapa y un marcador donde se indique.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        // Coordenadas del itcr
        LatLng itcr = new LatLng(9.856355, -83.912864);

        googleMap = map;
        map.addMarker(new MarkerOptions()
                .position(itcr)
                .title("ITCR"));
        moveMapLocation(itcr);
    }

}
