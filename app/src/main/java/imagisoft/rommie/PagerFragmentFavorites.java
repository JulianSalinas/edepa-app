package imagisoft.rommie;


import imagisoft.edepa.FavoriteList;

public class PagerFragmentFavorites extends PagerFragment {

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

        FavoriteList favorites = FavoriteList.getInstance();
        if(favorites.isChanged()) {
            favorites.getSortedEvents();
            adapter.notifyDataSetChanged();
        }

    }

}
