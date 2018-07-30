package imagisoft.modelview.news;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.views.RecyclerAdapter;
import imagisoft.modelview.views.RecyclerFragment;
import imagisoft.modelview.views.SmoothLayout;


public class NewsFragment extends RecyclerFragment {

    /**
     * Es donde se colocan cada uno de los mensajes
     * de forma VISUAL
     */
    @BindView(R.id.news_rv)
    RecyclerView newsRV;

    @Override
    protected RecyclerView getRecyclerView() {
        return newsRV;
    }

    /**
     * Contiene todos los posts de las noticias y ejecuta
     * los evento de inserción, deleción y modificación
     */
    protected NewsAdapter newsVA;

    @Override
    protected RecyclerAdapter getViewAdapter() {
        return newsVA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.news_view;
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias.
     * Si el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setupAdapter(){
        if(newsVA == null) {
            newsVA = new NewsFirebase(this);
            Log.i(toString(), "setupAdapter()");
        }
    }

    /**
     * Se configura el contenedor de mensajes {@link #newsRV}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void setupRecyclerView(){
        if (newsRV.getAdapter() == null){
            newsRV.setAdapter(newsVA);
            newsRV.setHasFixedSize(true);
            newsRV.setItemAnimator(new DefaultItemAnimator());
            newsRV.setLayoutManager(new SmoothLayout(getActivity()));
            Log.i(toString(), "setupRecyclerView()");
        }
    }

    @Override
    public void setupActivityView() {
        setToolbarText(R.string.nav_news);
    }

}
