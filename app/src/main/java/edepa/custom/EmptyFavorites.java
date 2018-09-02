package edepa.custom;

import butterknife.OnClick;
import edepa.modelview.R;
import edepa.pagers.PagerFragment;
import edepa.pagers.TabbedFragment;

public class EmptyFavorites extends BaseFragment {

    @Override
    public int getResource() {
        return R.layout.empty_favorites;
    }

    @OnClick(R.id.goto_button)
    public void gotoSchedule(){
        if (getParentFragment() instanceof PagerFragment){
            moveToTabSchedule((PagerFragment) getParentFragment());
        }
    }

    public void moveToTabSchedule(PagerFragment pager){
        if(pager.getParentFragment() instanceof TabbedFragment){
            TabbedFragment tabs = (TabbedFragment) pager.getParentFragment();
            tabs.moveToTab(TabbedFragment.SCHEDULE);
        }
    }

}
