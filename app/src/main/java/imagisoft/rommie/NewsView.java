package imagisoft.rommie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NewsView extends MessagesView {


    /**
     * Se crea el contenedor de los atributos que son vistas
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        setToolbarText(R.string.nav_people);
        return inflate(inflater, container, R.layout.news_view);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setToolbarText(R.string.nav_news);
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