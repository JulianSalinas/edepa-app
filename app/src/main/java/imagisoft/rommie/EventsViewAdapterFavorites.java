package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.FavoriteListener;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;

import java.util.ArrayList;


public class EventsViewAdapterFavorites
        extends EventsViewAdapter implements ChildEventListener, FavoriteListener {

    private String date;
    private FavoriteList favoriteList;

    public EventsViewAdapterFavorites(EventsView view) {
        super(view);
        this.date = ((EventsViewWithDate) view).getDate();
        this.favoriteList = FavoriteList.getInstance();

        this.events.addAll(favoriteList.getSortedEvents());

        favoriteList.addListener(this);
        view.activity.getScheduleReference().addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // Si se agrega un evento al cronograma, en favoritos no pasa nada
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null &&
                DateConverter.extractDate(event.getStart()).equals(date) && favoriteList.contains(event)) {
            int index = events.size();
            events.add(index, event);
            notifyItemInserted(index);
            Log.i(view.getTag(), "eventAdded");
            view.showStatusMessage("Un evento ha sido agregado");
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null &&
                DateConverter.extractDate(event.getStart()).equals(date) &&
                favoriteList.contains(event)) {
                int index = events.indexOf(event);
                events.set(index, event);
                notifyItemChanged(index);
                Log.i(view.getTag(), "eventChanged");
                view.showStatusMessage("Un evento en tus favoritos ha sido actualizado");
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null &&
                DateConverter.extractDate(event.getStart()).equals(date) &&
                favoriteList.contains(event)) {
                int index = events.indexOf(event);
                events.remove(index);
                notifyItemRemoved(index);
                Log.i(view.getTag(), "eventRemoved");
                view.showStatusMessage("Un evento en tus favoritos ya no está disponible");
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(view.getTag(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(view.getTag(), databaseError.getDetails());
    }

    @Override
    public void onFavoriteAdded(ScheduleEvent event) {

    }

    @Override
    public void onFavoriteRemoved(ScheduleEvent event) {

    }

}
