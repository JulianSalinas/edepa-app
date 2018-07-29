package imagisoft.modelview.views;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.ActivityFragment;


public class BlankFragment extends ActivityFragment {

    /**
     * El texto en el centro que muestra en fragmento
     */
    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.fragment_blank;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupActivityView() {
        // No es necesario
    }

    /**
     * Crea una nueva instancia del fragmento
     * @param description: Mensaje que se debe mostrar en el fragmento
     * @return BlankFragment
     */
    public static BlankFragment newInstance(String description){
        Bundle args = new Bundle();
        BlankFragment frag = new BlankFragment();
        args.putString("msg", description);
        frag.setArguments(args);
        return frag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("msg"))
            descriptionTextView.setText(args.getString("msg"));
    }

}