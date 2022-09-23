package edepa.info;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.CloudCongress;
import edepa.custom.CustomFragment;
import edepa.model.Congress;
import edepa.modelview.R;

/**
 * Fragmento utilizado para mostrar la información
 * del congreso.
 */
public class InfoFragment extends CustomFragment implements CloudCongress.Callbacks {

    @BindView(R.id.name_text)
    TextView nameText;

    @BindView(R.id.congress_view_pager)
    ViewPager pager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private CloudCongress cloud;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.info_screen;
    }

    /**
     * Retorna hacia el fragmento anterior
     */
    @OnClick(R.id.back_button)
    public void onBackPressed(){
        activity.onBackPressed();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(View.GONE);
        LodgingAdapter adapter = new LodgingAdapter();
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectCloud(){
        cloud = new CloudCongress();
        cloud.setCallbacks(this);
        cloud.connect();
    }

    /**
     * Coloca toda el nombre del congreso
     * @param congress: Clase con la información del congreso
     */
    public void updateCongress(Congress congress){
        nameText.setText(congress.getName());
    }

    public class LodgingAdapter extends FragmentPagerAdapter {

        public LodgingAdapter() {
            super(InfoFragment.this.getChildFragmentManager());
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return getString(R.string.text_information);
            else if (position == 1)
                return getString(R.string.text_lodging);
            else if (position == 2)
                return getString(R.string.text_restaurants);
            else if (position == 3)
                return getString(R.string.text_near_places);
            else
                return getString(R.string.text_banks);

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new InfoGeneralFragment();
            else if (position == 1)
                return new InfoLodgingFragment();
            else if (position == 2)
                return new InfoRestaurantsFragment();
            else if (position == 3)
                return new InfoNearPlacesFragment();
            else
                return new InfoBanksFragment();
        }

    }

}