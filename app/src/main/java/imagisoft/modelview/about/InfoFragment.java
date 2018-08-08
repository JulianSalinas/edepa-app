package imagisoft.modelview.about;

import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import com.google.android.gms.maps.GoogleMap;


import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.model.Congress;
import imagisoft.misc.DateConverter;

/**
 * Fragmento utilizado para mostrar la información
 * del congreso. Herede de {@link MapFragment} para
 * poder colocar una versión previa del mapa completo
 */
public class InfoFragment extends MapFragment {

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

    @BindView(R.id.button_back)
    ImageView buttonBack;

    /**
     * Referencia a la versión completa del
     * mapa con la ubicación de la sede
     */
    private Fragment expandedMap;

    /**
     * A diferencia del mapa de Google, este es
     * uno que se provee desde Firebase para mostrar
     * el croquis de la sedew
     */
    private Fragment miniMap;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.information_view;
    }

    /**
     * Coloca toda la información obtenida de la BD en los
     * componentes visuales
     * @param congress: Clase con la información del congreso
     */
    protected void loadInformation(Congress congress){
        super.loadInformation(congress);
        nameTextView.setText(congress.getName());
        locationTextView.setText(congress.getLocation());
        descriptionTextView.setText(congress.getDescription());
        endTextView.setText(DateConverter.extractDate(congress.getEnd()));
        startTextView.setText(DateConverter.extractDate(congress.getStart()));
        iconMap.setOnClickListener(v -> expandMiniMap());
        buttonBack.setOnClickListener(v -> activity.onBackPressed());
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