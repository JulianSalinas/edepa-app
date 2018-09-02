package edepa.pagers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;
import edepa.custom.EmptyFavorites;
import edepa.events.EventsFavorites;
import edepa.events.EventsFavoritesByType;
import edepa.events.EventsScheduleByType;
import edepa.model.Event;
import edepa.model.EventType;
import edepa.modelview.R;


public class PagerFavoritesByType extends PagerFavorites {

    protected List<EventType> types;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.types = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     * Es invocado el método {@link #onCreate(Bundle)}
     * @return PagerAdapter
     */
    @Override
    protected FragmentPagerAdapter instantiateAdapter() {
        return new PagerAdapterByType(this, types) {
            protected Fragment instantiateEventsFragment() {
                return new EventsFavoritesByType();
            }};
    }

    /**
     * Agrega una nueva página en caso de que no exista
     * Es utilizada cada vez que se agrega o mueve un evento
     * @param event: Donde se extrae la fecha para colocar en la página
     * @see #addPage(long)
     */
    public void addPageIfNotExists(Event event){
        EventType type = event.getEventype();
        if (type != null && !types.contains(type)) {
            addPageType(type);
            eventsAmount--;
        }
    }

    public void addPageType(EventType type){

        int index = types.size();
        types.add(index, type);
        adapter.notifyDataSetChanged();

        // Solución para restaurar última página puesta
        if (savedState != null){
            int curretItem = savedState.getInt(CURRENT_ITEM);
            pager.setCurrentItem(curretItem);
        }

        // Se coloca la página donde esta el evento ingresado
        else if (eventsAmount <= 0)
            pager.setCurrentItem(index);

        updateInterface();

    }

    public void updateInterface(){
        boolean isEmpty = types.size() == 0;
        pager.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        eventsEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

}
