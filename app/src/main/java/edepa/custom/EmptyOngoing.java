package edepa.custom;

import edepa.modelview.R;
import butterknife.OnClick;
import edepa.app.BaseFragment;
import edepa.events.EventsOngoing;


public class EmptyOngoing extends BaseFragment {

    @Override
    public int getResource() {
        return R.layout.empty_ongoing;
    }

    @OnClick(R.id.refresh_button)
    public void refresh(){
        if (getParentFragment() instanceof  EventsOngoing){
            EventsOngoing ongoing = (EventsOngoing) getParentFragment();
            ongoing.startRunnable();
            ongoing.showStatusMessage(R.string.text_complete_update);
        }
    }

}
