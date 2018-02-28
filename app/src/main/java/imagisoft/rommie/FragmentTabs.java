package imagisoft.rommie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FragmentTabs extends Fragment implements TabLayout.OnTabSelectedListener {

    private ArrayList<Fragment> tabs;

    public FragmentTabs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        setupInitialConfiguration();

        assert v != null;
        TabLayout tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void setupInitialConfiguration() {
        tabs = new ArrayList<>();
        tabs.add(new FragmentPager());
        tabs.add(new FragmentPager());
        tabs.add(new FragmentPager());
        setFragment(tabs.get(0));
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.tabs_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setFragment(tabs.get(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
