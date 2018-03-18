package imagisoft.rommie;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.view.ViewPager;

/**
 * Contiene los fragmentos donde se muestras las actividades del congreso
 */
public class SchedulePager extends ActivityMainFrag {

    public SchedulePager() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_pager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: Información de prueba, solamente para mostrar como se ve
        ArrayList<String> dates = new ArrayList<>();
        for(int i = 12; i<=18; i++)
            dates.add(String.valueOf(i) + "/08/2018");

        SchedulePagerAdapter adapter = new SchedulePagerAdapter(dates, getChildFragmentManager());
        ViewPager viewPager = getView().findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
    }

}
