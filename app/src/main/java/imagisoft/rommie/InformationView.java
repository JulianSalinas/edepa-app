package imagisoft.rommie;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import imagisoft.edepa.Congress;
import imagisoft.miscellaneous.DateConverter;


public class InformationView extends MainActivityFragment implements OnMapReadyCallback {

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
    @BindView(R.id.icon_map)
    View iconMap;

    @BindView(R.id.name_text_view)
    TextView nameTextView;

    @BindView(R.id.end_text_view)
    TextView endTextView;

    @BindView(R.id.start_text_view)
    TextView startTextView;

    @BindView(R.id.location_text_view)
    TextView locationTextView;

    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    /**
     * Botón para retornar a la pantalla anterior
     */
    @BindView(R.id.button_back)
    ImageView buttonBack;

    /**
     * Referencia hacia la información del congreso que se muestra
     */
    private Congress congress;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.information_view;
    }

    /**
     * Coloca toda la información obtenida de la bd en los componentes visuales
     * @param congress: Clase con la información del congreso
     */
    private void bindInformation(Congress congress){

        this.congress = congress;

        if(miniMap == null)
            miniMap = new InformationMap();

        nameTextView.setText(congress.getName());
        locationTextView.setText(congress.getLocation());
        descriptionTextView.setText(congress.getDescription());
        endTextView.setText(DateConverter.extractDate(congress.getEnd()));
        startTextView.setText(DateConverter.extractDate(congress.getStart()));

        iconMap.setOnClickListener(v -> switchFragment(miniMap));
        buttonBack.setOnClickListener(v -> activity.onBackPressed());

    }

    /**
     * Se coloca un listener para refrescar la vista cuando hay un cambio
     * en la base de datos
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        setToolbarVisibility(View.GONE);
        setTabLayoutVisibility(View.GONE);

        // Para que la información se actualice en tiempo real y no cada vez que
        // se abre la aplicación
        activity.getCongressReference()
                .addValueEventListener(new InformationViewValueEventListener());

    }

    /**
     * Al cambiar a otra sección se deben volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setToolbarVisibility(View.VISIBLE);
    }

    private void setupMap(){

        // Se revisa si el mapa ya está en la cache para no instanciarlo de nuevo.
        // Permite mostrar el mapa aunque no haya conexión
         if (mapFragment == null) {
             mapFragment = SupportMapFragment.newInstance();
             getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
         }


        mapFragment.getMapAsync(this);
        // Coloca el gmap en la posición destinada para tal fin

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
        Log.i("mapY", String.valueOf(congress.getyCoord()));
        Log.i("mapX", String.valueOf(congress.getxCoord()));
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
