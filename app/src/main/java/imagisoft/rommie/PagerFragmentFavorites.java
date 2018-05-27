package imagisoft.rommie;

import android.os.Bundle;
import android.util.Log;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.FavoriteListener;
import imagisoft.edepa.ScheduleEvent;


public class PagerFragmentFavorites extends PagerFragment {


    private FavoriteList favoriteList;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        favoriteList = FavoriteList.getInstance();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        favoriteList.addListener((FavoriteListener) adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        favoriteList.saveFavorites(activity);
        favoriteList.removeListener((FavoriteListener) adapter);
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
