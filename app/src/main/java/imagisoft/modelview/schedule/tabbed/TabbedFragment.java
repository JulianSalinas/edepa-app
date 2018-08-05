package imagisoft.modelview.schedule.tabbed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;
import imagisoft.modelview.schedule.events.EventsOngoing;
import imagisoft.modelview.schedule.paged.PaggedFragment;

public class TabbedFragment extends MainFragment{


    @BindView(R.id.tabs_pager)
    ViewPager tabsPager;

    @Override
    public int getResource() {
        return R.layout.schedule_tabs_view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMainActivity().activeTabbedMode();
        setToolbarText(R.string.app_name);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);

        tabsPager.setAdapter(new Adapter(getChildFragmentManager()));
        getMainActivity()
                .getToolbarTabsLayout()
                .setupWithViewPager(tabsPager, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().deactiveTabbedMode();
    }

    public class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.nav_schedule);
                case 1:
                    return getResources().getString(R.string.nav_favorites);
                default:
                    return getResources().getString(R.string.text_ongoing);
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PaggedFragment.Schedule();
                case 1:
                    return new PaggedFragment.Favorites();
                default:
                    return new EventsOngoing();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}
