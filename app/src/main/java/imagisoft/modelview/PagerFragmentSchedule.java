package imagisoft.modelview;


public class PagerFragmentSchedule extends PagerFragment {

    /**
     * Obtiene los eventos, extrae todos los días que componen los eventos
     * y ajusta la interfaz para solo mostrar dichos días por medio de un adaptador
     */
    protected void setupViewPager() {

        if(adapter == null)
            adapter = new PagerAdapterSchedule(this);

        pager.setAdapter(adapter);

    }

}
