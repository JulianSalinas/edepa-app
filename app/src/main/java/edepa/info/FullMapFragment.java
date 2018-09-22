package edepa.info;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.view.View;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.model.Location;
import edepa.custom.CustomFragment;

/**
 * Mapa que se muestra al presionar el mapa pequeño
 * del {@link InfoFragment}
 */
public class FullMapFragment extends CustomFragment implements OnMapReadyCallback{

    public static final String TAG_KEY = "tag";
    public static final String LOCATION_KEY = "location";

    protected String tag;
    protected Location location;

    private boolean isStatic = false;

    /**
     * Soporte para colocar el mapa
     */
    protected GoogleMap googleMap;
    protected SupportMapFragment mapFragment;

    /**
     * Solo sirve para dar navegación hacia
     * atrás
     */
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * Barra de progreso que se muestra hasta que
     * se ejecute {@link #onMapReady(GoogleMap)}
     */
    @Nullable
    @BindView(R.id.progress_circle)
    ProgressBar progressCircle;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.util_map_screen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null && args.containsKey(LOCATION_KEY)){
            location = args.getParcelable(LOCATION_KEY);
            isStatic = true;
        }

        if (args != null && args.containsKey(TAG_KEY)){
            tag = args.getString(TAG_KEY);
        }

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
            toolbar.setNavigationOnClickListener(v -> getNavigationActivity().onBackPressed());
            if (tag != null) toolbar.setTitle(tag);
        }

        // Se hace visible la barra hasta que se ejecute
        // el método {@link #onMapReady(GoogleMap)}
        if (progressCircle != null)
            progressCircle.setVisibility(View.VISIBLE);

        if (isStatic) updateLocation();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_KEY, tag);
        outState.putParcelable(LOCATION_KEY, location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            tag = savedInstanceState.getString(TAG_KEY);
            location = savedInstanceState.getParcelable(LOCATION_KEY);
        }
    }

    /**
     * Se sincroniza la información local con Firebase
     */
    public void updateLocation(){

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
        if (progressCircle != null)
            progressCircle.setVisibility(View.GONE);

        // Se obtiene y guarda la referencia del mapa creado
        googleMap = map;

        // Se obtienen las coordenadas para el marcador

        LatLng coordinates = new LatLng(location.getLat(), location.getLng());

        // Se agrega el marcador según las coordenadas
        googleMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(tag));

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
