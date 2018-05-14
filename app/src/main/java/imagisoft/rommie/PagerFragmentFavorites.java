package imagisoft.rommie;

import android.os.Bundle;
import android.util.Log;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.FavoriteListener;
import imagisoft.edepa.ScheduleEvent;


public class PagerFragmentFavorites extends PagerFragment implements FavoriteListener{


    private FavoriteList favoriteList;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.favoriteList = FavoriteList.getInstance();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteList.saveFavorites(activity);
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

    @Override
    public void onFavoriteAdded(ScheduleEvent event) {
        Log.i("PagerFragmentFavorites", "Favorite added");
    }

    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {
        Log.i("PagerFragmentFavorites", "favorite removed");
    }

}
