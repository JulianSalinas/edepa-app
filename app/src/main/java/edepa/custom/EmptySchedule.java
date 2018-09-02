package edepa.custom;

import butterknife.OnClick;
import edepa.modelview.R;

public class EmptySchedule extends BaseFragment {

    @Override
    public int getResource() {
        return R.layout.empty_schedule;
    }

    @OnClick(R.id.add_event_button)
    public void openEventEditor(){
        if(getParentFragment() instanceof CustomFragment) {
            CustomFragment parent = (CustomFragment) getParentFragment();
            String tag = "EVENT_EDITOR";
//            EventEditor frag = (EventEditor) parent.getNavigationActivity()
//                    .getSupportFragmentManager().findFragmentByTag(tag);
//            setFragmentOnScreen(frag != null ? frag: new EventEditor(), tag);
            parent.showStatusMessage(R.string.text_add_event);
        }
    }

}
