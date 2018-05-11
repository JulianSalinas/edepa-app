package imagisoft.rommie;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;


public class ExhibitorsViewFragment extends MainActivityFragment {

    /**
     * Lista de expositores obtenida de la lista de eventos
     */
    protected List<Exhibitor> exhibitors;
    protected ExhibitorsViewAdapter exhibitorsAdapter;
    protected LinkedMultiValueMap<Exhibitor, ScheduleBlock> eventsByExhibitors;

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        assert getView() != null;
        exhibitors = new ArrayList<>();
        eventsByExhibitors = new LinkedMultiValueMap<>();
        exhibitorsAdapter = new ExhibitorsViewAdapter(this);

        getFirebase()
                .getScheduleReference()
                .addValueEventListener(new ExhibitorsViewValueEventListener());

    }

    /**
     * Obtiene todos los expositores que maneja la vista
     */
    public List<Exhibitor> getExhibitors() {
        return exhibitors;
    }

    /**
     * Obtiene los eventos en que participa un expositor
     */
    public List<ScheduleBlock> getExhibitorsEvents(Exhibitor exhibitor) {
        return eventsByExhibitors.get(exhibitor);
    }

    /**
     * Agregar los expositores y a la vez los asocia a sus eventos
     * @param event: Evento de donde se extraeran los exponentes
     */
    public void addExhibitors(ScheduleEvent event){
        for (Exhibitor exhibitor : event.getExhibitors()) {
            exhibitors.add(exhibitor);
            eventsByExhibitors.add(exhibitor, event);
        }
    }

    /**
     * Elimina los expositores repetidos que participan en más de un evento
     */
    public void removeRepeatedExhibitors(){
        HashSet<Exhibitor> hashSet = new HashSet<>();
        hashSet.addAll(exhibitors);
        exhibitors.clear();
        exhibitors.addAll(hashSet);
    }

    /**
     * Acomoda alfabeticament a los expositores
     */
    public void sortExhibitors(){
        Collections.sort(exhibitors, (first, second) ->
                first.getCompleteName().compareTo(second.getCompleteName()));
    }

    /**
     * Actualiza la lista de expositores. Es usada cuando se agrega un
     * nuevo evento
     */
    public void updateExhibitors(){
        removeRepeatedExhibitors();
        sortExhibitors();
        exhibitorsAdapter.notifyDataSetChanged();
    }

    /**
     * Por cada evento extrae los expositores y actualiza el
     * contenido del adaptador
     */
    private void updateAdapter(DataSnapshot dataSnapshot){
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
            ScheduleEvent event = postSnapshot.getValue(ScheduleEvent.class);
            if(event != null) addExhibitors(event);
        }
    }

    /**
     * Clase que conecta a los expositores con el adaptador en tiempo real
     */
    class ExhibitorsViewValueEventListener implements ValueEventListener {

        /**
         * Actualiza tanto adaptador como UI
         */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            updateAdapter(dataSnapshot);
            updateExhibitors();
        }

        /**
         * Si existe error, se escribe en el LOG
         */
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(getTag(), databaseError.toString());
        }

    }

}
