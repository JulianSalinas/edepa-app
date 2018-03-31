package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import imagisoft.edepa.Congress;
import imagisoft.edepa.UDateConverter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;


public class InformationView extends MainViewFragment implements OnMapReadyCallback{

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Congress congressInformation;

    public InformationView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.information_view, container, false);

        setLabelsContent(view);
        setupMap();

        return view;
    }

    private void setLabelsContent(View view){

        congressInformation = getFirebase().getCongressInformation();

        TextView congressName = view.findViewById(R.id.text_congress_name);
        congressName.setText(congressInformation.getName());

        TextView congressStart = view.findViewById(R.id.shedule_start);
        congressStart.setText(UDateConverter.extractDate(congressInformation.getStart()));

        TextView congressEnd = view.findViewById(R.id.shedule_end);
        congressEnd.setText(UDateConverter.extractDate(congressInformation.getEnd()));

        TextView congressDescription = view.findViewById(R.id.text_presentation);
        congressDescription.setText(congressInformation.getDescription());

        TextView congressLocation = view.findViewById(R.id.text_location);
        congressLocation.setText(congressInformation.getWrittenLocation());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupMap(){

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
     * Se utiliza el API de google para mostrar el mapa y un marcador donde se indique.
     */

    @Override
    public void onMapReady(GoogleMap map) {

        // Coordenadas del lugar del congreso
        LatLng coordinates = new LatLng(congressInformation.getyCoord(), congressInformation.getxCoord());

        googleMap = map;
        map.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(congressInformation.getLocationTag()));
        moveMapLocation(coordinates);
    }

}
