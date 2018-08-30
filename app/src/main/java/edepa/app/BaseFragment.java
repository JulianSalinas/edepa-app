package edepa.app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;


public abstract class BaseFragment extends Fragment {

    /**
     * Entero que representa el layout que está utilizando el
     * fragmento
     */
    protected int resource;

    /**
     * Obliga a las subclases a colocar el atributo resource
     */
    public abstract int getResource();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resource = getResource();
    }

    /**
     * Todas las subclases usan el mismo método, lo único que cambia
     * es el resource, por tanto se implementa aquí
     * @return Vista del fragmento
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(resource, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
