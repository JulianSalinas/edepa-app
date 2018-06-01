package imagisoft.modelview;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import imagisoft.misc.DateConverter;
import imagisoft.misc.SearchNormalizer;
import imagisoft.model.ScheduleEvent;

class EventsAdapterSearch extends EventsAdapterOngoing implements MaterialSearchView.OnQueryTextListener {

    private List<ScheduleEvent> searchResults;
    private List<ScheduleEvent> eventsHolder;

    public EventsAdapterSearch(EventsFragment view) {
        super(view);
        searchResults = new ArrayList<>();
        eventsHolder = new ArrayList<>();
    }

    public void setupReference(){
        fragment.activity
                .getScheduleReference()
                .orderByChild("start")
                .addChildEventListener(this);
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
    public boolean onQueryTextSubmit(String query) {

        events.clear();
        searchResults.clear();

        for (ScheduleEvent event: eventsHolder){

            if(SearchNormalizer.normalize(event.getTitle())
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(DateConverter.extractDate(event.getStart()))
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(DateConverter.extractDate(event.getEnd()))
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(DateConverter.extractTime(event.getStart()))
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(DateConverter.extractTime(event.getEnd()))
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(event.getLocation())
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(event.getEventype().toString())
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

            if(SearchNormalizer.normalize(event.getId())
                    .contains(SearchNormalizer.normalize(query))){
                searchResults.add(event);
            }

        }

        events.addAll(searchResults);
        notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return onQueryTextSubmit(newText);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            int index = eventsHolder.size();
            eventsHolder.add(index, event);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if (event != null){
            event.setId(dataSnapshot.getKey());
            int index = eventsHolder.indexOf(event);
            eventsHolder.set(index, event);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null) {
            event.setId(dataSnapshot.getKey());
            int index = eventsHolder.indexOf(event);
            eventsHolder.remove(index);
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
