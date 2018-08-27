package edepa.info;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.minilibs.TimeConverter;
import edepa.model.Congress;
import edepa.modelview.R;

public class InfoGeneralFragment extends MapFragment {

    @BindView(R.id.end_text)
    TextView endText;

    @BindView(R.id.start_text)
    TextView startText;

    @BindView(R.id.location_text)
    TextView locationText;

    @BindView(R.id.description_text)
    TextView descriptionText;

    /**
     * A diferencia del mapa de Google, este es
     * uno que se provee desde Firebase para mostrar
     * el croquis de la sede
     */
    private Fragment miniMap;

    /**
     * Referencia a la versión completa del
     * mapa con la ubicación de la sede
     */
    private Fragment expandedMap;

    @Override
    public int getResource() {
        return R.layout.info_general;
    }

    /**
     * Coloca toda la información obtenida de la BD en los
     * componentes visuales
     * @param congress: Clase con la información del congreso
     */
    public void updateCongress(Congress congress){
        super.updateCongress(congress);
        locationText.setText(congress.getLocation());
        descriptionText.setText(congress.getDescription());
        endText.setText(TimeConverter.extractDate(congress.getEnd()));
        startText.setText(TimeConverter.extractDate(congress.getStart()));
    }

    /**
     * Cuando el mapa está listo se agregan los eventos para
     * que cuando lo toque se abra la versión más grande del mapa
     * @param map Mapa ya descargado
     */
    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);
        googleMap.setOnMapClickListener(latLng -> expandGoogleMap());
        googleMap.setOnMarkerClickListener(marker -> expandGoogleMap());
    }

    /**
     * Abre en un fragmento aparte el croquis de la sede
     */
    @OnClick(R.id.map_icon)
    public void expandMiniMap(){
        if(miniMap == null) miniMap = new MinimapFragment();
        setFragmentOnScreen(miniMap, "MINIMAP");
    }

    /**
     * Abre en un fragmento aparte la versión completa
     * del mapa de google con el marcador en la sede
     * @return True
     */
    public boolean expandGoogleMap(){
        if(expandedMap == null) expandedMap = new MapFragment();
        setFragmentOnScreen(expandedMap, "MAP");
        return true;
    }

}
