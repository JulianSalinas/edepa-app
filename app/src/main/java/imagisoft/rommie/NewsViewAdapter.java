package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.google.firebase.database.ChildEventListener;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsViewAdapter extends MessagesViewAdapterOnline {


    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public NewsViewAdapter(MainViewFragment view) {

        super(view);

        ChildEventListener listener = new MessageViewAdapterChildEventListener();
        view.getFirebase().getNewsReference().addChildEventListener(listener);

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