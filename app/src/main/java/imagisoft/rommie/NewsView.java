package imagisoft.rommie;

import imagisoft.edepa.Controller;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public class NewsView extends MainViewFragment {


    /**
     * Es la capa donde se coloca cada uno de los mensajes
     */
    private RecyclerView recyclerView;

    /**
     * Referencia al controlador principal
     */
    private Controller ctrl = Controller.getInstance();


    /**
     * Es necesario el adaptador para colocar nuevos mensajes
     */
    private NewsViewAdapter adapter;

    /**
     * Se crea el contenedor de los atributos que son vistas
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.news_view, container, false);
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        bindViews();
        setupAdapter();
        setupRecyclerView();
    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     * Aquí se consultan los mensajes al controlador por primera vez
     */
    private void bindViews(){
        assert getView() != null;
        recyclerView = getView().findViewById(R.id.news_view_recycler);
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias
     */
    public void setupAdapter(){
        adapter = new NewsViewAdapter(ctrl.getNewsRoom());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            /**
             * Al insertar un item el scroll se mueve al final
             */
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }

        });
    }

    /**
     * Se configura el contenedor de noticaciones , recyclerView
     */
    public void setupRecyclerView(){
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    }

}