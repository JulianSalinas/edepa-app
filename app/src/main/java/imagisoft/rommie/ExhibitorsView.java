package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public class ExhibitorsView extends ExhibitorsViewFragment {

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    private RecyclerView exhibitorsView;

    /**
     * Se crea el contenedor de los exponentes
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.exhibitors_view, container, false);
        exhibitorsView = view.findViewById(R.id.exhibitors_view);
        return view;

    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        setupExhibitorsView();

    }

    /**
     * Se configura el exhibitorsView que contiene los expositores
     */
    public void setupExhibitorsView(){

        exhibitorsView.setHasFixedSize(true);
        exhibitorsView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsView.setAdapter(exhibitorsAdapter);

    }

}
