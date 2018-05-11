package imagisoft.rommie;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;


public abstract class PagerFragment extends MainActivityFragment {

    /**
     * Barra debajo de los tabs para colocar los días
     */
    @BindView(R.id.view_pager)
    protected ViewPager pager;

    /**
     * El adaptador contiene los fragmentos del páginador
     */
    protected FragmentPagerAdapter adapter;

    /**
     * Crea la vista del paginador, es decir, donde se colocan los días
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.schedule_pager);
    }

    /**
     * Al crearse el fragmento se prepara el paginador para mostrar los días
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.setupViewPager();
        setTabLayoutVisibility(View.VISIBLE);
        setToolbarText(R.string.app_name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setTabLayoutVisibility(View.GONE);
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    protected abstract void setupViewPager();

}
