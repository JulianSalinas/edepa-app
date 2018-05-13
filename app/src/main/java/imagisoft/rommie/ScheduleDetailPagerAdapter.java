package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import imagisoft.edepa.ScheduleEvent;

public class ScheduleDetailPagerAdapter extends FragmentPagerAdapter {

    ScheduleEvent event;

    public ScheduleDetailPagerAdapter(FragmentManager fm, ScheduleEvent event) {
        super(fm);
        this.event = event;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return ScheduleDetail.newInstance(event);
        }
        else{
            return ScheduleDetailFocus.newInstance(event);
        }
    }

}
