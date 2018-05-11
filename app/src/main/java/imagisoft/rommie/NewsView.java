package imagisoft.rommie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NewsView extends MessagesView {

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.news_view;
    }

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