package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import android.support.v4.view.ViewPager;

/**
 * Cada subclase tiene que encargarse de colocar el páginador
 * y su respectivo adaptador
 */
public abstract class PagerFragment extends ActivityFragment {

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
     */

    @Override
    public void setupResource() {
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
        setupViewPager();
    }

    @Override
    public void setupActivityView() {
        setToolbarText(R.string.app_name);
        setTabLayoutVisibility(View.VISIBLE);
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    protected abstract void setupViewPager();

}
