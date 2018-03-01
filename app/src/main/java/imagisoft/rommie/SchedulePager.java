package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class SchedulePager extends Fragment {

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

        // Test data: Days of congress
        ArrayList<String> dates = new ArrayList<>();
        for(int i = 12; i<=18; i++)
            dates.add(String.valueOf(i) + "/08/2018");

        SchedulePagerAdapter adapter = new SchedulePagerAdapter(dates);
        ViewPager viewPager = getView().findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
    }

    public class SchedulePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> dates;

        private SchedulePagerAdapter(ArrayList<String> dates) {
            super(getChildFragmentManager());
            this.dates = dates;
        }

        @Override
        public int getCount() {
            return dates.size();
        }

        @Override
        public Fragment getItem(int position) {
            // Here you can grab de date
            // This code is temporaly
            if(position != 0)
                return position == 1 ? new FragmentTab2() : new FragmentTab3();
            else
                return new ScheduleView();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dates.get(position);
        }

    }

}
