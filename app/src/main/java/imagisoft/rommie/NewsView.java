package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import java.util.ArrayList;


public class NewsView extends MainViewFragment {


    /**
     * Es la capa donde se coloca cada uno de los mensajes
     */
    private RecyclerView newsView;

    /**
     * Es necesario el adaptador para colocar nuevos mensajes
     */
    private NewsViewAdapter adapter;

    /**
     * Se crea el contenedor de los atributos que son vistas
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.news_view, container, false);
        newsView = view.findViewById(R.id.news_view_recycler);
        return view;

    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupAdapter();
        setupNewsView();
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias
     */
    public void setupAdapter(){

        if(adapter == null) {
            adapter = new NewsViewAdapter(new ArrayList<>());
            registerAdapterDataObserver();
        }

    }

    /**
     * Ayuda a configurar el adaptador cuando es creado por primera vez
     */
    private void registerAdapterDataObserver(){

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            /**
             * Al insertar un item el scroll se mueve al final
             */
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                newsView.scrollToPosition(adapter.getItemCount()-1);
            }

        });

    }

    /**
     * Se configura el contenedor de noticaciones, newsView
     */
    public void setupNewsView(){

        newsView.setAdapter(adapter);
        newsView.setHasFixedSize(true);
        newsView.setItemAnimator(new DefaultItemAnimator());
        newsView.setLayoutManager(new SmoothLayout(this.getActivity()));
        newsView.scrollToPosition(adapter.getItemCount()-1);

    }

}