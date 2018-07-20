package imagisoft.modelview;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;


public class BlankFragment extends MainActivityFragment {

    /**
     * El texto en el centro que muestra en fragmento
     */
    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    /**
     * Crea una nueva instancia del fragmento
     * Guarda y restaura el mensaje en caso de que la pantalla se gire
     * @param description: Mensaje que se debe mostrar en el fragmento
     * @return BlankFragment
     */
    public static BlankFragment newInstance(String description){

        Bundle args = new Bundle();
        args.putString("msg", description);

        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void setupResource() {
        this.resource = R.layout.fragment_blank;
    }

    /**
     * Se coloca el mensaje en la vista
     * @param savedInstanceState: No es necesario
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null)
            descriptionTextView.setText(savedInstanceState.getString("description"));

    }

    @Override
    public void setupActivityView() {
        // No es necesario
    }

}