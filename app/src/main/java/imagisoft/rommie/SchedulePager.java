package imagisoft.rommie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.view.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import imagisoft.edepa.Schedule;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.edepa.UTestController;

/**
 * Contiene los fragmentos donde se muestras las actividades del congreso
 */
public class SchedulePager extends ActivityMainFrag {

    /**
     * Barra debajo de los tabs para colocar los días
     */
    private ViewPager viewPager;

    /**
     * Conexión con el controlador para obtener los días correspondientes al congreso
     */
    private UTestController controller = UTestController.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_pager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.setupViewPager();
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y coloca la interfaz para solo mostrar dichos dias
     */
    private void setupViewPager() {
        assert getView() != null;
        viewPager = getView().findViewById(R.id.view_pager);
        ValueEventListener listener = new SchedulePagerValueEventListener();
        controller.getScheduleSection().addValueEventListener(listener);
    }

    class SchedulePagerValueEventListener implements ValueEventListener{

        private ArrayList<String> dates;
        private SchedulePagerAdapter adapter;
        private GenericTypeIndicator<ArrayList<ScheduleEvent>> typeIndicator;

        SchedulePagerValueEventListener(){
            dates = new ArrayList<>();
            typeIndicator = new GenericTypeIndicator<ArrayList<ScheduleEvent>>(){};
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Schedule schedule = new Schedule(dataSnapshot.getValue(typeIndicator));
            dates = Collections.list((schedule).getEventsByDay().keys());
            adapter = new SchedulePagerAdapter(dates, getChildFragmentManager());
            viewPager.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            dates.add(getResources().getString(R.string.text_no_connection));
            adapter = new SchedulePagerAdapter(dates, getChildFragmentManager());
            viewPager.setAdapter(adapter);
        }

    }

}
