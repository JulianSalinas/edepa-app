package imagisoft.modelview.about;

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
import imagisoft.model.Cloud;
import imagisoft.model.Congress;
import imagisoft.misc.DateConverter;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;


public class InfoFragment
        extends MainFragment implements OnMapReadyCallback, ValueEventListener {

    /**
     * Soporte para colocar el mapa
     */
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

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
    public int getResource() {
        return R.layout.information_view;
    }

    /**
     * Coloca toda la información obtenida de la BD en los
     * componentes visuales
     * @param congress: Clase con la información del congreso
     */
    private void loadInformation(Congress congress){
        this.congress = congress;
        nameTextView.setText(congress.getName());
        locationTextView.setText(congress.getLocation());
        descriptionTextView.setText(congress.getDescription());
        endTextView.setText(DateConverter.extractDate(congress.getEnd()));
        startTextView.setText(DateConverter.extractDate(congress.getStart()));
        iconMap.setOnClickListener(v -> setFragmentOnScreen(new MapFragment()));
        buttonBack.setOnClickListener(v -> activity.onBackPressed());
        setupMap();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.GONE);

        // Para que la información se actualice en tiempo real
        // y no cada vez que se abre la aplicación
        Cloud.getInstance().getReference(Cloud.CONGRESS)
                .addValueEventListener(this);

    }


    private void setupMap(){

        // Se revisa si el mapa ya está en la cache para no instanciarlo de nuevo.
        // Permite mostrar el mapa aunque no haya conexión
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        // Coloca el gmap en la posición destinada para tal fin

    }

    /**
     * Se utiliza el API de google para mostrar el mapa y un marcador donde se indique.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        // Coordenadas del lugar del congreso
        Log.i("mapY", String.valueOf(congress.getyCoord()));
        Log.i("mapX", String.valueOf(congress.getxCoord()));

        LatLng coordinates = new LatLng(
                congress.getyCoord(),
                congress.getxCoord());

        googleMap = map;
        map.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(congress.getLocationTag()));

        moveMapLocation(coordinates);

    }

    private void moveMapLocation(LatLng currentLocation) {

        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(currentLocation,15));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory
                .zoomTo(15), 2000, null);


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Congress congress = dataSnapshot.getValue(Congress.class);
        if (congress != null) loadInformation(congress);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(getTag(), databaseError.toString());
    }

}