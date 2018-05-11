package imagisoft.rommie;

import imagisoft.edepa.FavoriteList;


public class PagerAdapterFavorites extends PagerAdapter {


    public PagerAdapterFavorites(MainActivityFragment schedulePager) {
        super(schedulePager);
        FavoriteList favorites = FavoriteList.getInstance();
        events = favorites.getSortedEvents();
        notifyDataSetChanged();
    }

}