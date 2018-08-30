package edepa.info;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.OnClick;
import edepa.app.CustomFragment;
import edepa.cloud.Cloud;
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
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private CloudCongress cloud;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.information_screen;
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

        Cloud.getInstance().getReference(Cloud.CONGRESS)
                .child("lodging").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                adapter.setHasLodging(size > 0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        cloud = new CloudCongress();
        cloud.setCallbacks(this);
        cloud.connect();

    }

    @Override
    public void onDestroyView() {
        cloud.disconnect();
        super.onDestroyView();
    }

    /**
     * Coloca toda el nombre del congreso
     * @param congress: Clase con la información del congreso
     */
    public void updateCongress(Congress congress){
        nameText.setText(congress.getName());
    }

    public class LodgingAdapter extends FragmentPagerAdapter {

        private boolean hasLodging = false;

        public LodgingAdapter() {
            super(InfoFragment.this.getChildFragmentManager());
        }

        public void setHasLodging(boolean hasLodging) {
            this.hasLodging = hasLodging;
            tabLayout.setVisibility(hasLodging ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getCount() {
            return hasLodging ? 2 : 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ?
                    getString(R.string.text_information) :
                    getString(R.string.text_lodging);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new InfoGeneralFragment();
            else
                return new InfoLodgingFragment();
        }

    }

}