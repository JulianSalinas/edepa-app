package edepa.info;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import butterknife.OnClick;
import edepa.custom.CustomFragment;
import edepa.cloud.CloudCongress;
import edepa.modelview.R;
import edepa.model.Congress;
import butterknife.BindView;

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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return getString(R.string.text_information);
            else if (position == 1)
                return getString(R.string.text_lodging);
            else
                return getString(R.string.text_restaurants);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new InfoGeneralFragment();
            else if (position == 1)
                return new InfoLodgingFragment();
            else
                return new InfoRestaurantsFragment();
        }

    }

}