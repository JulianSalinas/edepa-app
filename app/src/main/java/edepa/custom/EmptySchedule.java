package edepa.custom;

import edepa.modelview.R;

public class EmptySchedule extends BaseFragment {

    @Override
    public int getResource() {
        return R.layout.schedule_empty_schedule;
    }

    // @OnClick(R.id.add_event_button)
    public void openEventEditor(){
        if(getParentFragment() instanceof CustomFragment) {
            CustomFragment parent = (CustomFragment) getParentFragment();
            parent.showStatusMessage(R.string.text_add_event);
        }
    }

}
