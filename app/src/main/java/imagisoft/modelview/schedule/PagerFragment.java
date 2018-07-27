package imagisoft.modelview.schedule;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.ActivityFragment;

import android.support.v4.view.ViewPager;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;


public abstract class PagerFragment extends ActivityFragment {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.schedule_pager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupActivityView() {
        setToolbarText(R.string.app_name);
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen
     * los eventos y ajusta la interfaz para solo mostrar dichos
     * días por medio de un adaptador
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void setupAdapter(){
        if (adapter == null) {
            adapter = getAdapter();
            pager.setAdapter(adapter);
            Log.i(toString(), "setupAdapter()");
        }
    }

    /**
     * Es utilizada por la función {@link #setupAdapter()}
     * Cada uno de los fragmentos, el de cronograma y el de
     * favoritos son iguales pero necesitan manejar distintos
     * eventos por los que cada uno implementa este método
     * @return PagerAdaptador adaptor para el fragmento
     */
    protected abstract PagerAdapter getAdapter();

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public static class Schedule extends PagerFragment {

        @Override
        protected PagerAdapter getAdapter() {
            return new PagerAdapter.Schedule(this);
        }

    }

}
