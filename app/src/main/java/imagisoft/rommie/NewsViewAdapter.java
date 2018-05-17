package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;

import imagisoft.edepa.Message;
import imagisoft.miscellaneous.DateConverter;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsViewAdapter extends MessagesViewAdapterOnline {

    private int NEWS_ITEM = 1;
    private int NEWS_ITEM_WITH_SEPARATOR = 0;

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public NewsViewAdapter(MainActivityFragment view) {

        super(view);

        ChildEventListener listener = new MessageViewAdapterChildEventListener();
        view.activity.getNewsReference().addChildEventListener(listener);

    }

    /**
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param item: Message
     */
    @Override
    protected int getItemViewTypeWithoutSeparator(Message item){
        return NEWS_ITEM;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param item: Message
     */
    protected int getItemViewTypeWithSeparator(Message item){
        return NEWS_ITEM_WITH_SEPARATOR;
    }

    /**
     * Crear la vista de la noticia o el separador según corresponda
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == NEWS_ITEM ?
                R.layout.news_item : R.layout.news_item_with_separator;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == NEWS_ITEM ?
                new MessageViewHolder(view) :
                new MessageWithSeparatorViewHolder(view);

    }

}