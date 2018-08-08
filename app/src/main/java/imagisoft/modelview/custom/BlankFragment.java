package imagisoft.modelview.custom;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import imagisoft.misc.MaterialGenerator;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;

/**
 * Fragmento utilizado con el próposito de realizar pruebas
 */
public class BlankFragment extends MainFragment {

    /**
     * El texto en el centro que muestra en fragmento
     */
    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.fragment_blank;
    }

    /**
     * Crea una nueva instancia del fragmento
     * @return BlankFragment
     */
    public static BlankFragment newInstance(){
        return new BlankFragment();
    }

    /**
     * Crea una nueva instancia del fragmento y además
     * añade una descripción
     * @param description: Texto para mostrar
     * @return BlankFragment
     */
    public static BlankFragment newInstance(String description){
        Bundle args = new Bundle();
        BlankFragment frag = new BlankFragment();
        args.putString("description", description);
        frag.setArguments(args);
        return frag;
    }

    /**
     * {@inheritDoc}
     * Se coloca el la descripción en el centro del fragmento
     * Además se colorea el fragmento de manera aleatoria
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("description")) {
            String description = args.getString("description");
            descriptionTextView.setText(description);
            setRandomColor();
        }
    }

    /**
     * Se coloca un color con base a la descripción
     * que fue pasada como argumento
     */
    public void setRandomColor(){
        MaterialGenerator generator = new MaterialGenerator(activity);
        int color = generator.getColor(descriptionTextView.getText());
        if(getView() != null) getView().setBackgroundColor(color);
    }

}