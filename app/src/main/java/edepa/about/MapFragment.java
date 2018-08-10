package edepa.about;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import edepa.model.Cloud;
import edepa.model.Congress;
import edepa.modelview.R;
import edepa.activity.MainFragment;

/**
 * Mapa que se muestra al presionar el mapa pequeño
 * del {@link InfoFragment}
 */
public class MapFragment extends MainFragment
        implements OnMapReadyCallback, ValueEventListener {

    /**
     * Información general del congreso
     */
    protected Congress congress;

    /**
     * Soporte para colocar el mapa
     */
    protected GoogleMap googleMap;
    protected SupportMapFragment mapFragment;

    /**
     * Barra de progreso que se muestra hasta que
     * se ejecute {@link #onMapReady(GoogleMap)}
     */
    @BindView(R.id.progress_map)
    @Nullable
    ProgressBar progressBar;

    /**
     * Solo sirve para dar navegación hacia
     * atrás
     */
    @BindView(R.id.toolbar)
    @Nullable
    Toolbar toolbar;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.info_map;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Se coloca invisible la toolbar global
        setToolbarVisibility(View.GONE);

        // Se hace visible la toolbar del fragmento
        // que solo tiene un botón para retroceder
        if(toolbar != null) {
            Drawable icon = getResources().getDrawable(R.drawable.ic_back);
            icon.setTint(getResources().getColor(R.color.app_primary_font));
            toolbar.setNavigationIcon(icon);
            toolbar.setNavigationOnClickListener(v -> getMainActivity().onBackPressed());
        }

        // Se hace visible la barra hasta que se ejecute
        // el método nMapReady(GoogleMap)
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);

        // Para que la información se actualice en tiempo real
        // y no cada vez que se abre la aplicación
        Cloud.getInstance()
                .getReference(Cloud.CONGRESS)
                .addValueEventListener(this);

    }

    /**
     * Se sincroniza la información local con Firebase
     * @param dataSnapshot: Información del congreso
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Congress congress = dataSnapshot.getValue(Congress.class);
        if (congress != null) loadInformation(congress);
    }

    /**
     * Ha ocurrido un error al cargar la información
     * del congreso
     * @param databaseError: Informe del error
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(getTag(), databaseError.toString());
    }

    /**
     * Coloca toda la información obtenida de la BD en los
     * componentes visuales
     * @param congress: Clase con la información del congreso
     */
    protected void loadInformation(Congress congress){
        this.congress = congress;

        // Se revisa si el mapa ya está en la cache para no
        // instanciarlo de nuevo.
        // Permite mostrar el mapa aunque no haya conexión
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    /**
     * Se muestra el mapa después de haberse descargado
     * @param map Mapa ya descargado
     */
    @Override
    public void onMapReady(GoogleMap map) {

        // Se oculta la barra de progreso
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);

        // Se obtienen las coordenadas para el marcador
        LatLng coordinates = new LatLng(
                congress.getyCoord(),
                congress.getxCoord());

        // Se obtiene y guarda la referencia del mapa creado
        googleMap = map;

        // Se le coloca al mapa un tono oscuro
        // googleMap.setMapStyle(MapStyleOptions
        //        .loadRawResourceStyle(getMainActivity(), R.raw.maps_style));

        // Se agrega el marcador según las coordenadas
        googleMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(congress.getLocationTag()));

        moveMapLocation(coordinates);

    }

    /**
     * Mueve la camara hacia una posición es específica
     * con una animación
     * @param currentLocation: Ubicación
     */
    private void moveMapLocation(LatLng currentLocation) {

        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(currentLocation,15));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory
                .zoomTo(15), 2000, null);

    }

}
