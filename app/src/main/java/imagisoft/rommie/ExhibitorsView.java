package imagisoft.rommie;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class ExhibitorsView extends MainViewFragment {

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    private RecyclerView exhibitorsView;
    private ExhibitorsViewAdapter exhibitorsAdapter;

    /**
     * Lista con todos los expositores
     */
    private List<Exhibitor> exhibitors;

    /**
     * Se crea el contenedor de los exponentes
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.exhibitors_view, container, false);
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     * Se enlaza con firebase para obtener los expositores de todos los eventos
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        assert getView() != null;
        exhibitors = Collections.synchronizedList(new ArrayList<>());
        exhibitorsView = getView().findViewById(R.id.exhibitors_view);
        exhibitorsAdapter = new ExhibitorsViewAdapter(exhibitors);
        setupExhibitorsView();

        getFirebase()
                .getScheduleReference()
                .addValueEventListener(new ExhibitorsViewValueEventListener());

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
     * Se configura el exhibitorsView que contiene los expositores
     */
    public void setupExhibitorsView(){
        exhibitorsView.setHasFixedSize(true);
        exhibitorsView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsView.setAdapter(exhibitorsAdapter);
    }

    class ExhibitorsViewValueEventListener implements ValueEventListener {

        /**
         * Por cada evento extrae los expositores y actualiza la UI
         */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                ScheduleEvent event = postSnapshot.getValue(ScheduleEvent.class);
                if(event != null) exhibitors.addAll(event.getExhibitors());
            }   updateExhibitors();

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
