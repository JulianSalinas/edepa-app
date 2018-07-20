package imagisoft.modelview;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;

import android.support.v4.view.ViewPager;

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
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.schedule_pager;
    }

    public void setCurrentItem(int position){
        pager.setCurrentItem(position);
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param savedInstanceState: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager();
    }

    @Override
    public void setupActivityView() {
        setToolbarText(R.string.app_name);
    }

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    protected abstract void setupViewPager();

}
