package edepa.custom;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import edepa.misc.MaterialGenerator;
import edepa.modelview.R;
import edepa.activity.MainFragment;

/**
 * Fragmento utilizado con el próposito de realizar pruebas
 */
public class FragmentBlanck extends MainFragment {

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
        return R.layout.custom_view;
    }

    /**
     * Crea una nueva instancia del fragmento
     * @return FragmentBlanck
     */
    public static FragmentBlanck newInstance(){
        return new FragmentBlanck();
    }

    /**
     * Crea una nueva instancia del fragmento y además
     * añade una descripción
     * @param description: Texto para mostrar
     * @return FragmentBlanck
     */
    public static FragmentBlanck newInstance(String description){
        Bundle args = new Bundle();
        FragmentBlanck frag = new FragmentBlanck();
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