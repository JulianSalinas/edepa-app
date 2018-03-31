package imagisoft.rommie;

import java.util.ArrayList;

import imagisoft.edepa.Message;
import imagisoft.edepa.UDateConverter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.NewsItemViewHolder> {

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private ArrayList<Message> news;

    /**
     * Constructor del adaptador
     * @param news Noticias obtenidos del modelo
     */
    NewsViewAdapter(ArrayList<Message> news){
        this.news = news;
    }

    /**
     * Agrega una nueva noticia a la vista
     */
    public void addNew(Message msg){
        news.add(msg);
        notifyItemInserted(news.size()-1);
    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return news.size();
    }

    /**
     * Crear la vista de la noticia
     */
    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsItemViewHolder(view);
    }

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(NewsItemViewHolder holder, int position) {
        Message msg = news.get(holder.getAdapterPosition());
        holder.username.setText(msg.getUsername());
        holder.messageContent.setText(msg.getContent());
        holder.timeDescription.setText(UDateConverter.extractTime(msg.getTime()));
    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    class NewsItemViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView timeDescription;
        TextView messageContent;

        NewsItemViewHolder(View view) {
            super(view);
            this.username = view.findViewById(R.id.news_item_username);
            this.timeDescription = view.findViewById(R.id.news_item_time_description);
            this.messageContent = view.findViewById(R.id.news_item_content);
        }

    }

}