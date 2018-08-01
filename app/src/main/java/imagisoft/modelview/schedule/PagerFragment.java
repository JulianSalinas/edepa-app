package imagisoft.modelview.schedule;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;


public abstract class PagerFragment extends MainFragment {

    /**
     * Espacio donde las páginas se cambian
     * para mostrar los eventos de otros días
     * @see #setupAdapter()
     */
    @BindView(R.id.view_pager)
    ViewPager pager;

    /**
     * Contiene los fragmentos del páginador
     * @see #setupAdapter()
     */
    private PagerAdapter adapter;

    Bundle pausedState;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.schedule_pager;
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen
     * los eventos y ajusta la interfaz para solo mostrar dichos
     * días por medio de un adaptador.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setupAdapter(){
        Log.i(toString(), "setupAdapter()");
        if (adapter == null)
            adapter = getAdapter();
    }

    /**
     * Usualmente adapter es conservado pero pager no conserva
     * el adapter y hay que colocarlo otra vez
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void setupPager(){
        Log.i(toString(), "setupAdapter()");
        if (pager.getAdapter() == null)
            pager.setAdapter(adapter);
    }

    /**
     * Es utilizada por la función {@link #onActivityCreated(Bundle)}
     * Cada uno de los fragmentos, el de cronograma y el de
     * favoritos son iguales pero necesitan manejar distintos
     * eventos por los que cada uno implementa este método
     * @return PagerAdaptador adaptor para el fragmento
     */
    protected abstract PagerAdapter getAdapter();


    public static class Schedule extends PagerFragment {
        @Override
        protected PagerAdapter getAdapter() {
            return new PagerAdapter.Schedule(this);
        }
    }

    public static class Favorites extends PagerFragment {
        @Override
        protected PagerAdapter getAdapter() {
            return new PagerAdapter.Favorites(this);
        }
    }

}
