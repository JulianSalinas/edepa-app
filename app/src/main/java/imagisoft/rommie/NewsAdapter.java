package imagisoft.rommie;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import imagisoft.edepa.Message;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsAdapter extends MessagesAdapterOnline {

    private int NEWS_ITEM = 1;
    private int NEWS_ITEM_WITH_SEP = 0;

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public NewsAdapter(ActivityFragment view) {
        super(view);
    }

    @Override
    protected void setupReference() {
        this.reference = view.activity.getNewsReference();
    }

    /**
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param msg: Message
     */
    @Override
    protected int getItemWithoutSeparator(Message msg){
        return NEWS_ITEM;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param msg: Message
     */
    protected int getItemWithSeparator(Message msg){
        return NEWS_ITEM_WITH_SEP;
    }

    /**
     * Crear la vista de la noticia o el separador según corresponda
     */
    @Override
    public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType == NEWS_ITEM ?
                R.layout.news_item : R.layout.news_item_with_sep;

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return viewType == NEWS_ITEM ?
                new MessageVH(view) :
                new MessageVHWS(view);

    }

}