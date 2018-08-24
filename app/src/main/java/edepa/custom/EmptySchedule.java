package edepa.custom;

import butterknife.OnClick;
import edepa.app.MainFragment;
import edepa.app.SimpleFragment;
import edepa.events.EventsOngoing;
import edepa.modelview.R;

public class EmptySchedule extends SimpleFragment {

    @Override
    public int getResource() {
        return R.layout.empty_schedule;
    }

    @OnClick(R.id.add_event_button)
    public void openEventEditor(){
        if(getParentFragment() instanceof MainFragment) {
            MainFragment parent = (MainFragment) getParentFragment();
            String tag = "EVENT_EDITOR";
//            EventEditor frag = (EventEditor) parent.getNavigationActivity()
//                    .getSupportFragmentManager().findFragmentByTag(tag);
//            setFragmentOnScreen(frag != null ? frag: new EventEditor(), tag);
            parent.showStatusMessage(R.string.text_add_event);
        }
    }

}
