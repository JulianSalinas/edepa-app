package imagisoft.rommie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;

import imagisoft.edepa.Message;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsViewAdapter extends MessagesViewAdapterOnline {

    private int TIMESTAMP = 0;
    private int NEWS_ITEM = 1;

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public NewsViewAdapter(MainActivityFragment view) {

        super(view);

        ChildEventListener listener = new MessageViewAdapterChildEventListener();
        view.getFirebase().getNewsReference().addChildEventListener(listener);

    }

    /**
     *  Obtiene si es una noticia o una marca de tiempo
     */
    @Override
    public int getItemViewType(int position) {
        return msgs.get(position) instanceof Message ? NEWS_ITEM: TIMESTAMP;
    }

    /**
     * Crear la vista de la noticia o el separador según corresponda
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == NEWS_ITEM ?
                R.layout.news_item : R.layout.date_separator_2;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == NEWS_ITEM ?
                new MessageViewHolder(view) :
                new TimestampViewHolder(view);

    }

}