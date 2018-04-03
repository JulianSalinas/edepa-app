package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

/**
 * Muestra la lista de expositores del congreso
 */
public class ExhibitorsView extends MainViewFragment {

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    private RecyclerView recyclerView;

    /**
     * Se crea el contenedor de los exponentes
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.exhibitors_view, container, false);
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupRecyclerView();
    }

    /**
     * Se configura el recyclerView que contiene los expositores
     */
    public void setupRecyclerView(){
        assert getView() != null;
        recyclerView = getView().findViewById(R.id.exhibitors_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ExhibitorsViewAdapter(this));
    }

}
