package imagisoft.rommie;

import imagisoft.edepa.Congress;
import imagisoft.edepa.UDateConverter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class InformationView extends MainViewFragment implements OnMapReadyCallback{

    /**
     * Soporte para colocar el mapa
     */
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    /**
     * Vista para mostrar el croquis o minimapa de la sede
     */
    private InformationMap miniMap;

    /**
     * Componentes gráficos de la pantalla de información
     */
    private View iconMap;
    private TextView name;
    private TextView end;
    private TextView start;
    private TextView location;
    private TextView description;

    /**
     * Botón para retornar a la pantalla anterior
     */
    private ImageView buttonBack;

    /**
     * Referencia hacia la información del congreso que se muestra
     */
    private Congress congress;

    /**
     * Crea la vista principal donde se coloca la información del congreso
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.information_view, container, false);

        end = view.findViewById(R.id.shedule_end);
        start = view.findViewById(R.id.shedule_start);
        name = view.findViewById(R.id.text_congress_name);

        location = view.findViewById(R.id.text_location);
        description = view.findViewById(R.id.text_presentation);

        iconMap = view.findViewById(R.id.ic_map);
        buttonBack = view.findViewById(R.id.button_back);

        return view;

    }

    private void bindInformation(Congress congress){

        this.congress = congress;

        if(miniMap == null)
            miniMap = new InformationMap();

        name.setText(congress.getName());
        start.setText(UDateConverter.extractDate(congress.getStart()));
        end.setText(UDateConverter.extractDate(congress.getEnd()));
        description.setText(congress.getDescription());
        location.setText(congress.getLocation());

        buttonBack.setOnClickListener(v -> getNavigation().onBackPressed());
        iconMap.setOnClickListener(v -> switchFragment(miniMap));

    }

    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        setToolbarVisible(false);

        // Para que la información se actualice en tiempo real y no cada vez que
        // se abre la aplicación
        getFirebase()
                .getCongressReference()
                .addValueEventListener(new InformationViewValueEventListener());

    }

    /**
     * Al cambiar a otra sección se deben volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        setToolbarVisible(true);

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
        LatLng coordinates = new LatLng(congress.getyCoord(), congress.getxCoord());

        googleMap = map;
        map.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(congress.getLocationTag()));

        moveMapLocation(coordinates);

    }

    class InformationViewValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            bindInformation(dataSnapshot.getValue(Congress.class));
            setupMap();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(getTag(), databaseError.toString());
        }

    }

}
