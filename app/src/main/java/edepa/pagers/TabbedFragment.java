package edepa.pagers;

import android.os.Bundle;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;

import edepa.modelview.R;
import butterknife.BindView;
import edepa.app.MainFragment;
import edepa.events.EventsOngoing;


public class TabbedFragment extends MainFragment{

    /**
     * Está el fragmento de favoritos, el del cronograma
     * y el de eventos en curso
     */
    private static final int FRAGMENTS = 3;

    /**
     * Es para colocar el fragment para los eventos que
     * suceden al presionar un tab
     */
    @BindView(R.id.toolbar_tabs_layout)
    TabLayout tabLayout;

    /**
     * Páginador para deslizar las tres pantallas cuando
     * se sobrepase la última página del cronograma
     */
    @BindView(R.id.tabs_pager)
    ViewPager tabsPager;

    /**
     * {@inheritDoc}
     * @return R.layout.events_tabs
     */
    @Override
    public int getResource() {
        return R.layout.events_tabs;
    }

    /**
     * {@inheritDoc}
     * Se coloca setOffscreenPageLimit a 3 para mantener
     * todos los fragmento en memoria
     * @param savedInstanceState: Argumentos guardados
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customizeActivity();
        tabsPager.setAdapter(new TabbedAdapter());
        tabsPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(tabsPager, true);
    }

    /**
     * Se personaliza la vista de la actividad que
     * contiene este fragmento
     */
    public void customizeActivity(){
        setToolbarText(R.string.app_name);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);
    }

    /**
     * Adaptador para {@link #tabsPager}
     */
    public class TabbedAdapter extends FragmentPagerAdapter {

        /**
         * Cantidad de fragmentos en los tabs
         * @return #FRAGMENTS = 3
         */
        @Override
        public int getCount() {
            return FRAGMENTS;
        }

        /**
         * Constructor del adaptador
         */
        public TabbedAdapter() {
            super(TabbedFragment.this.getChildFragmentManager());
        }

        /**
         * Sirve para instancia cada uno de los tabs
         * @param position: Posición del tab
         * @return PagerSchedule | PagerFavorites | EventsOngoing
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new PagerSchedule();
                case 1: return new PagerFavorites();
                default: return new EventsOngoing();
            }
        }

        /**
         * Obtiene el título del tab según su posición
         * @param position: Posición del tab
         * @return Título del tab
         */
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
