package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.google.firebase.database.ChildEventListener;
/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsViewAdapter extends MessagesViewAdapter {

    /**
     * Constructor del adaptador
     */
    public NewsViewAdapter(NewsView newsView){

        super(newsView);

        ChildEventListener listener = new NewsViewAdapter.MessageViewAdapterChildEventListener();
        newsView.getFirebase().getNewsReference().addChildEventListener(listener);

    }

    /**
     * Crear la vista de la noticia
     */
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new MessageViewHolder(view);

    }

}