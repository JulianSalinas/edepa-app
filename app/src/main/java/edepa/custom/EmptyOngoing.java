package edepa.custom;

import edepa.schedule.ScheduleOngoing;
import edepa.modelview.R;
import butterknife.OnClick;


public class EmptyOngoing extends BaseFragment {

    @Override
    public int getResource() {
        return R.layout.schedule_empty_ongoing;
    }

    @OnClick(R.id.refresh_button)
    public void refresh(){
        if (getParentFragment() instanceof ScheduleOngoing){
            ScheduleOngoing ongoing = (ScheduleOngoing) getParentFragment();
            ongoing.startRunnable();
            ongoing.showStatusMessage(R.string.text_complete_update);
        }
    }

}
