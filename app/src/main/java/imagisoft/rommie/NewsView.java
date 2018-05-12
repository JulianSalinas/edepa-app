package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.support.annotation.Nullable;


public class NewsView extends MessagesView {

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.news_view;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setToolbarText(R.string.nav_news);
        setTabLayoutVisibility(View.GONE);
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias
     */
    @Override
    public void setupAdapter(){

        if(adapter == null) {
            adapter = new NewsViewAdapter(this);
            registerAdapterDataObserver();
        }

    }

}