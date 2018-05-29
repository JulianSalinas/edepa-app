package imagisoft.modelview;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.model.Exhibitor;
import imagisoft.model.ScheduleEvent;
import imagisoft.misc.DateConverter;

class EventsAdapterExhibitor
        extends EventsAdapter implements ChildEventListener{

    private Exhibitor exhibitor;

    public EventsAdapterExhibitor(EventsFragment view) {
        super(view);
        setupReference();
        this.exhibitor = ((ExhibitorDetail) view).getExhibitor();
        this.events = ((ExhibitorDetail) view).getEvents();
    }

    public void setupReference(){
        fragment.activity.getScheduleReference().addChildEventListener(this);
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
    protected String getDateAsString(long start){
        return DateConverter.extractDate(start);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // Los eventos son pasado por parámetro
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.indexOf(event);
        if(index != -1) {
            events.set(index, event);
            notifyItemChanged(index);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        int index = events.indexOf(event);
        if(index != -1) {
            events.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(fragment.getTag(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(fragment.getTag(), databaseError.getDetails());
    }

}
