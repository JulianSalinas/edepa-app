package imagisoft.rommie;

import java.util.List;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleBlock;


public class PagerAdapterFavorites extends PagerAdapter {


    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    @Override
    protected ScheduleView createScheduleView(int position){
        List<ScheduleBlock> blocks = blocksByDay.get(dates.get(position));
        return ScheduleViewFavorites.newInstance(blocks);
    }

    public PagerAdapterFavorites(MainActivityFragment schedulePager) {
        super(schedulePager);
        FavoriteList favorites = FavoriteList.getInstance();
        events = favorites.getSortedEvents();
        notifyDataSetChanged();
    }

}