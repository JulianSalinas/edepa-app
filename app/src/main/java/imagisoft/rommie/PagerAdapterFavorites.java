package imagisoft.rommie;

import java.util.List;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.FavoriteListener;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;


public class PagerAdapterFavorites extends PagerAdapter implements FavoriteListener{

    /**
     * Para agreagar los eventos que solicita la clase padre
     */
    private FavoriteList favoriteList;

    public PagerAdapterFavorites(PagerFragment fragment) {
        super(fragment);
        favoriteList = FavoriteList.getInstance();
        addEvents(favoriteList.getSortedEvents());
        notifyDataSetChanged();
    }

    /**
     * Función usada por getItem para obtener una nueva instancia únicamente
     * cuando sea necesario.
     */
    @Override
    protected ScheduleView createScheduleView(int position){
        List<ScheduleBlock> blocks = blocksByDay.get(dates.get(position));
        return ScheduleViewFavorites.newInstance(blocks);
    }

    @Override
    public void onFavoriteAdded(ScheduleEvent event) {

    }

    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {

    }

}