package imagisoft.rommie;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FragmentPager extends Fragment {

    private ArrayList<Fragment> tabs;

    public FragmentPager() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        setupInitialConfiguration();

        assert v != null;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = v.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

//        int pagerPadding = 16;
//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(pagerPadding, 0, pagerPadding, 0);

    }

    private void setupInitialConfiguration() {
//        tabs = new ArrayList<>();
//        tabs.add(new FragmentTab1());
//        tabs.add(new FragmentTab2());
//        tabs.add(new FragmentTab3());
//        setFragment(tabs.get(0));
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.pager_tab_strip, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 3;
        // Tab Titles
        private String tabtitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
        Context context;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                // Open FragmentTab1.java
                case 0:
                    FragmentTab1 fragmenttab1 = new FragmentTab1();
                    return fragmenttab1;

                // Open FragmentTab2.java
                case 1:
                    FragmentTab2 fragmenttab2 = new FragmentTab2();
                    return fragmenttab2;

                // Open FragmentTab3.java
                case 2:
                    FragmentTab3 fragmenttab3 = new FragmentTab3();
                    return fragmenttab3;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabtitles[position];
        }
    }

}
