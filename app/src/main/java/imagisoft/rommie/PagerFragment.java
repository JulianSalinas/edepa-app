package imagisoft.rommie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.animation.Animation;

import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import butterknife.BindView;

import static javax.xml.datatype.DatatypeConstants.DURATION;

/**
 * Cada subclase tiene que encargarse de colocar el páginador
 * y su respectivo adaptador
 */
public abstract class PagerFragment extends MainActivityFragment {

    /**
     * Barra debajo de los tabs para colocar los días
     */
    @BindView(R.id.view_pager)
    protected ViewPager pager;

    /**
     * El adaptador contiene los fragmentos del páginador
     */
    protected PagerAdapter adapter;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.schedule_pager;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        this.setupViewPager();

        setToolbarText(R.string.app_name);
        setTabLayoutVisibility(View.VISIBLE);

        if(bundle != null)
            pager.setCurrentItem(bundle.getInt("currentPage"));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", pager.getCurrentItem());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            pager.setCurrentItem(savedInstanceState.getInt("currentPage"));
    }


    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    protected abstract void setupViewPager();

}
