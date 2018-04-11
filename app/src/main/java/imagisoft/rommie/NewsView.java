package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;


public class NewsView extends MessagesView {


    /**
     * Se crea el contenedor de los atributos que son vistas
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.news_view, container, false);
        mainView = view.findViewById(R.id.news_view_recycler);
        return view;

    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
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