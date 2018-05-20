package imagisoft.rommie;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;

class EventsViewAdapterExhibitor
        extends EventsViewAdapter implements ChildEventListener{

    private Exhibitor exhibitor;

    public EventsViewAdapterExhibitor(EventsView view) {
        super(view);
//        setupReference();
        this.exhibitor = ((ExhibitorDetail) view).getExhibitor();
        this.events = ((ExhibitorDetail) view).getEvents();
    }

    public void setupReference(){
        view.activity.getScheduleReference().addChildEventListener(this);
    }

    /**
     *  Obtiene si la vista es un bloque de hora o una actividad
     */
    @Override
    public int getItemViewType(int position) {

        ScheduleEvent item = events.get(position);

        if(position == 0)
            return WITH_SEPARATOR;

        else {
            ScheduleEvent upItem = events.get(position - 1);
            String itemStart = DateConverter.extractDate(item.getStart());
            String upItemStart = DateConverter.extractDate(upItem.getStart());
            return itemStart.equals(upItemStart) ? SINGLE: WITH_SEPARATOR;
        }

    }

    @Override
    protected String getDatesAsString(ScheduleEvent event){
        return DateConverter.extractDate(event.getStart());
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null && event.getExhibitors() != null){
            if(event.getExhibitors().contains(exhibitor)){
                int index = events.size();
                events.add(index, event);
                notifyItemInserted(index);
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.indexOf(event);
        events.set(index, event);
        notifyItemChanged(index);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.indexOf(event);
        events.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(view.getTag(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(view.getTag(), databaseError.getDetails());
    }

}
