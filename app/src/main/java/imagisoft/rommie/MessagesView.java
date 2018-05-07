package imagisoft.rommie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;


public abstract class MessagesView extends MainViewFragment {

    /**
     * Se obtiene el usuario actual o que envía
     */
    protected FirebaseAuth auth = FirebaseAuth.getInstance();
    protected FirebaseUser user = auth.getCurrentUser();

    /**
     * Es la capa (con su adaptador) donde se coloca cada uno de los mensajes
     */
    protected MessagesViewAdapter adapter;

    @BindView(R.id.messages_view_recycler)
    RecyclerView mainView;

    /**
     * Se inicializan las variables no gráficas
     */
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    /**
     * Se configuran las clases de las vistas y sus eventos
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupAdapter();
        setupMainView();
    }

    /**
     * Agrega un el evento de actualizar inserción al adaptado
     */
    public void registerAdapterDataObserver(){
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            /**
             * Al insertar un item el scroll se mueve al final
             */
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mainView.scrollToPosition(adapter.getItemCount()-1);
            }

        });
    }

    /**
     * Se configura el contenedor de mensajes, mainView
     */
    public void setupMainView(){
        mainView.setAdapter(adapter);
        mainView.setHasFixedSize(true);
        mainView.setItemAnimator(new DefaultItemAnimator());
        mainView.setLayoutManager(new SmoothLayout(this.getActivity()));
        mainView.scrollToPosition(adapter.getItemCount()-1);
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes
     */
    public abstract void setupAdapter();


}