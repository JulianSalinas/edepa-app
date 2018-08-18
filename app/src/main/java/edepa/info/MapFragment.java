package edepa.info;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
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


import butterknife.BindView;
import edepa.cloud.CloudCongress;
import edepa.modelview.R;
import edepa.model.Congress;
import edepa.app.MainFragment;

/**
 * Mapa que se muestra al presionar el mapa pequeño
 * del {@link InfoFragment}
 */
public class MapFragment extends MainFragment
        implements OnMapReadyCallback, CloudCongress.Callbacks{

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
        return R.layout.info_map;
    }

    private CloudCongress cloudCongress;

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
        }

        // Se hace visible la barra hasta que se ejecute
        // el método {@link #onMapReady(GoogleMap)}
        if (progressCircle != null)
            progressCircle.setVisibility(View.VISIBLE);

        // Para que la información se actualice en tiempo real
        // y no cada vez que se abre la aplicación
        if(cloudCongress != null) {
            cloudCongress = new CloudCongress();
            cloudCongress.setCallbacks(this);
        }

    }

    /**
     * Se conecta con la nube para recibir cambios
     * en el congreso
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void connectCloud(){
        cloudCongress.connect();
    }

    /**
     * Se desconecta para dejar de recibir cambios
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disconnectCloud(){
        cloudCongress.disconnect();
    }


    /**
     * Se sincroniza la información local con Firebase
     * @param congress: Información del congreso
     */
    public void updateCongress(Congress congress){
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
        if (progressCircle != null)
            progressCircle.setVisibility(View.GONE);

        // Se obtienen las coordenadas para el marcador
        LatLng coordinates = new LatLng(
                congress.getxCoord(),
                congress.getyCoord());

        // Se obtiene y guarda la referencia del mapa creado
        googleMap = map;

        // Se le coloca al mapa un tono oscuro
        // googleMap.setMapStyle(MapStyleOptions
        //        .loadRawResourceStyle(getNavigationActivity(), R.raw.maps_style));

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
