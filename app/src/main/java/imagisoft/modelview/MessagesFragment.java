package imagisoft.modelview;

import butterknife.BindView;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public abstract class MessagesFragment extends ActivityFragment {

    /**
     * Donde se coloca cada uno de los mensajes
     */
    protected MessagesAdapter messagesVA;

    @BindView(R.id.messages_recycler_view)
    RecyclerView messagesRV;

    /**
     * Al insertar un item el scroll se mueve al final
     */
    protected RecyclerView.AdapterDataObserver observer =
            new RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            messagesRV.scrollToPosition(messagesVA.getItemCount()-1);
        }

    };

    /**
     * Se configuran las clases de las vistas y sus eventos
     */
    @Override
    public void setupActivityView() {
        setupAdapter();
        setupRecyclerView();
    }

    /**
     * Coloca la vista hasta el último elemento insertado
     */
    public void registerAdapterDataObserver(){
        messagesVA.registerAdapterDataObserver(observer);
    }

    /**
     * Se configura el contenedor de mensajes, messagesRV
     */
    public void setupRecyclerView(){
        messagesRV.setAdapter(messagesVA);
        messagesRV.setHasFixedSize(true);
        messagesRV.setItemAnimator(new DefaultItemAnimator());
        messagesRV.setLayoutManager(new SmoothLayout(this.getActivity()));
        messagesRV.scrollToPosition(messagesVA.getItemCount()-1);
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes
     */
    public abstract void setupAdapter();


}