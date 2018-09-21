package edepa.custom;

import butterknife.OnClick;
import edepa.modelview.R;
import edepa.pagers.PagerFragment;
import edepa.pagers.TabbedFragmentDefault;

public class EmptyFavorites extends BaseFragment {

    @Override
    public int getResource() {
        return R.layout.schedule_empty_favorites;
    }

    @OnClick(R.id.goto_button)
    public void gotoSchedule(){
        if (getParentFragment() instanceof PagerFragment){
            moveToTabSchedule((PagerFragment) getParentFragment());
        }
    }

    public void moveToTabSchedule(PagerFragment pager){
        if(pager.getParentFragment() instanceof TabbedFragmentDefault){
            TabbedFragmentDefault tabs = (TabbedFragmentDefault) pager.getParentFragment();
            tabs.moveToTab(TabbedFragmentDefault.SCHEDULE);
        }
    }

}
