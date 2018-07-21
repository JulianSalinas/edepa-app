package imagisoft.modelview;

import android.os.Bundle;

import imagisoft.model.FavoriteList;
import imagisoft.model.FavoriteListener;


public class PagerFragmentFavorites extends PagerFragment {


    private FavoriteList favoriteList;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param savedInstanceState: No se utiliza
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteList = FavoriteList.getInstance();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoriteList.addListener((FavoriteListener) adapter);
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    protected void setupViewPager() {

        // Se revisa porque al entrar por seguna vez, no es necesario colocar el adaptador
        // de lo contrario se reinicia a la posición inicial (visualmente)
        if(adapter == null)
            adapter = new PagerAdapterFavorites(this);

        pager.setAdapter(adapter);

    }

}
