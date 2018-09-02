package edepa.custom;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.TextView;

import edepa.modelview.R;
import butterknife.BindView;
import edepa.minilibs.ColorGenerator;

/**
 * Fragmento utilizado con el próposito de realizar pruebas
 */
public class BlankFragment extends CustomFragment {

    /**
     * El texto en el centro que muestra en fragmento
     */
    @BindView(R.id.description_text)
    TextView descriptionTextView;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.util_blank_screen;
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
            descriptionTextView.setTextColor(Color.WHITE);
            setRandomColor();
        }
    }

    /**
     * Se coloca un accent con base a la descripción
     * que fue pasada como argumento
     */
    public void setRandomColor(){
        ColorGenerator generator = new ColorGenerator(activity);
        int color = generator.getColor(descriptionTextView.getText());
        if(getView() != null) getView().setBackgroundColor(color);
    }

}