package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import imagisoft.edepa.ScheduleEvent;


public class ScheduleDetailPagerAdapter extends FragmentPagerAdapter {

    /**
     * Evento que se adapta visualmente
     */
    private ScheduleEvent event;
    private ScheduleDetailPager pager;

    public ScheduleDetailPagerAdapter(ScheduleDetailPager pager) {
        super(pager.getChildFragmentManager());
        this.event = pager.getEvent();
    }

    /**
     * Se necesita una vista para ScheduleDetailBrief y ScheduleDetailFront
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Solo se instancia una vez, luego los administra el adaptador
     */
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return ScheduleDetailFront.newInstance(event);
        else
            return ScheduleDetailBrief.newInstance(event);
    }

}
