package edepa.custom;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import butterknife.ButterKnife;

public abstract class DefaultDialog extends DialogFragment {

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
