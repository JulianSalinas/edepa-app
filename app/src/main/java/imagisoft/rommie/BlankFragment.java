package imagisoft.rommie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

public class BlankFragment extends MainActivityFragment {

    /**
     * Usada paa obtener el argumento dentro de bundle
     */
    private String DESCRIPTION_KEY = "DESCRIPTION";

    /**
     * Información que se muestra en el fragmento en blanco
     */
    private String description;

    /**
     * Contenedor visual de la variable "description"
     */
    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    /**
     * Se requiere el constructor vacio
     */
    public BlankFragment() {

    }

    /**
     * Se toma la descripción desde los argumentos
     * @param bundle: Contiene la descripción
     */
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        if(bundle != null)
            description = bundle.getString(DESCRIPTION_KEY);
        else
            description = getString(R.string.text_without_description);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.fragment_blank);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        descriptionTextView.setText(description);
    }

}
