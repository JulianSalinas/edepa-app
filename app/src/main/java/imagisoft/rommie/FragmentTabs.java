package imagisoft.rommie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentTabs extends Fragment {

    public FragmentTabs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_tabs, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        View v = getView();
        TabLayout tabLayout = v.findViewById(R.id.tab_layout);
        ViewPager tabPager = v.findViewById(R.id.tab_pager);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        tabPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(tabPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

    public class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new FragmentTab1();
                case 1: return new FragmentTab2();
                case 2: return new FragmentTab3();
                default: return new FragmentTab1();
            }
        }

        @Override
        public String getPageTitle(int position){
            switch (position){
                case 0: return "Cronograma";
                case 1: return "Mi agenda";
                case 2: return "En curso";
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }
}
