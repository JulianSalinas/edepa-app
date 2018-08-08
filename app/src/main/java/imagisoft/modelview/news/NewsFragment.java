package imagisoft.modelview.news;

import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import butterknife.BindView;
import imagisoft.modelview.R;
import imagisoft.modelview.custom.RecyclerAdapter;
import imagisoft.modelview.custom.RecyclerFragment;
import imagisoft.modelview.custom.SmoothLayout;


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
    public int getResource() {
        return R.layout.news_view;
    }

    /**
     * {@inheritDoc}
     * Se configura el contenedor de mensajes {@link #newsRV}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsVA = new NewsFirebase(this);
    }

    /**
     * {@inheritDoc}
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias.
     * Si el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setToolbarText(R.string.nav_news);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);

        newsRV.setAdapter(newsVA);
        newsRV.setHasFixedSize(true);
        newsRV.setItemAnimator(new DefaultItemAnimator());
        newsRV.setLayoutManager(new SmoothLayout(getActivity()));

    }

}
