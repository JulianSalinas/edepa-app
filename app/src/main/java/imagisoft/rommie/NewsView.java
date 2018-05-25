package imagisoft.rommie;

import android.view.View;


public class NewsView extends MessagesFragment {

    /**
     * Se define cúal es el layout que va a utilizar
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.news_view;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     */
    @Override
    public void setupActivityView() {
        setToolbarText(R.string.nav_news);
        setToolbarVisibility(View.VISIBLE);
        setTabLayoutVisibility(View.GONE);
        super.setupActivityView();
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias
     */
    @Override
    public void setupAdapter(){

        if(messagesVA == null) {
            messagesVA = new NewsViewAdapter(this);
            registerAdapterDataObserver();
        }

    }

}