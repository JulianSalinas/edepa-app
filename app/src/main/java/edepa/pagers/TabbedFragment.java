package edepa.pagers;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import edepa.modelview.R;
import edepa.activity.MainFragment;
import edepa.events.EventsOngoing;

public class TabbedFragment extends MainFragment{

    private static final int FRAGMENTS = 3;

    @BindView(R.id.tabs_pager)
    ViewPager tabsPager;

    @Override
    public int getResource() {
        return R.layout.events_tabs;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getMainActivity().activeTabbedMode();
        setToolbarText(R.string.app_name);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);

        tabsPager.setAdapter(new TabbedAdapter());
        tabsPager.setOffscreenPageLimit(4);
        TabLayout tabLayout = getMainActivity().getToolbarTabsLayout();
        tabLayout.setupWithViewPager(tabsPager, true);

    }

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().getToolbarTabs().setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().getToolbarTabs().setVisibility(View.GONE);
    }

    public class TabbedAdapter extends FragmentPagerAdapter {

        @Override
        public int getCount() {
            return FRAGMENTS;
        }

        public TabbedAdapter() {
            super(TabbedFragment.this.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new PagerSchedule();
                case 1: return new PagerFavorites();
                default: return new EventsOngoing();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.nav_schedule);
                case 1: return getString(R.string.nav_favorites);
                default: return getString(R.string.nav_ongoing);
            }
        }

    }

}
