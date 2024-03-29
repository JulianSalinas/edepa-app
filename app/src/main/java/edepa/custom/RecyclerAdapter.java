package edepa.custom;

import android.support.v7.widget.RecyclerView;

/**
 * Clase que únicamente que es utilizada para darle un
 * alias a la clase #RecyclerView.TabbedAdapter<RecyclerView.SearchHolderPerson>
 * pues es un nombre muy largo
 */
public abstract class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * {@inheritDoc}
     * Es utilizada para debug
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
