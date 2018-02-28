package imagisoft.rommie;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTest extends Fragment {

    private ViewPager viewPager;
//    private TabPagerAdapter pagerAdapter;

    public FragmentTest() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.activity_test, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        View v = getView();

//        // Locate the viewpager in activity_main.xml
//        ViewPager viewPager = v.findViewById(R.id.pager);
//
//        // Set the ViewPagerAdapter into ViewPager
//        viewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
//
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        pagerAdapter = new TabPagerAdapter(getChildFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        this.viewPager = v.findViewById(R.id.tab_content);
//        this.viewPager.setAdapter(pagerAdapter);
//
//        TabLayout tabLayout = v.findViewById(R.id.tabs);
//
//        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(this.viewPager));


    }



}
