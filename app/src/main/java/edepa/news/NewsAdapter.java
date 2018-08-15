package edepa.news;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.app.EdepaAdmin;
import edepa.custom.DialogCustom;
import edepa.misc.DateCalculator;
import edepa.model.Cloud;
import edepa.model.NewsItem;
import edepa.modelview.R;
import edepa.custom.FragmentImage;
import edepa.custom.RecyclerAdapter;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class NewsAdapter extends RecyclerAdapter {

    protected List<NewsItem> items;
    protected NewsFragment fragment;

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(NewsItem item){
        items.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(NewsItem item){
        int index = items.indexOf(item);
        if (index != -1) {
            items.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void changeItem(NewsItem item){
        int index = items.indexOf(item);
        if (index != -1) {
            items.set(index, item);
            notifyItemChanged(index);
        }
    }

    public NewsAdapter(NewsFragment fragment) {
        this.fragment = fragment;
        this.items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         ((NewsItemHolder) holder).bind();
    }

    public class NewsItemHolder extends RecyclerView.ViewHolder {

        private int position;

        private NewsItem newsItem;

        @BindView(R.id.news_item_content)
        TextView itemContent;

        @BindView(R.id.news_item_title)
        TextView itemTitle;

        @BindView(R.id.news_item_time_ago)
        TextView itemTimeAgp;

        @BindView(R.id.news_item_read_amount)
        TextView itemReadAmount;

        @BindView(R.id.news_item_thumbnail)
        ImageView itemThumbnail;

        @BindView(R.id.news_item_delete)
        View itemDelete;

        public NewsItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(){

            this.position = getAdapterPosition();
            this.newsItem = items.get(position);

            itemTitle.setText(newsItem.getTitle());
            itemContent.setText(newsItem.getContent());
            linkifyContent();

            DateCalculator calc = new DateCalculator(itemView.getContext());
            itemTimeAgp.setText(calc.getTimeAgo(newsItem.getTime()));

            int viewed = newsItem.getViewedAmount();
            itemReadAmount.setVisibility(viewed > 0 ? View.VISIBLE : View.GONE);
            if (itemReadAmount.getVisibility() == View.VISIBLE) setViewedAmout();

            String imageUrl = newsItem.getImageUrl();
            itemThumbnail.setVisibility(imageUrl == null ? View.GONE: View.VISIBLE);
            if (itemThumbnail.getVisibility() == View.VISIBLE) loadThumbnail(imageUrl);
            setThumbnailOnClickListener();

            itemDelete.setVisibility(View.GONE);
            EdepaAdmin admin = new EdepaAdmin();
            admin.setAdminPermissionListener(new EdepaAdmin.AdminPermissionListener() {

                @Override
                public void onPermissionGranted() {
                    itemDelete.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPermissionDenied() {
                    itemDelete.setVisibility(View.GONE);
                }
            });
            admin.requestAdminPermission();

        }

        @OnClick(R.id.news_item_delete)
        public void deleteItem(){
            DialogCustom.Builder builder = new DialogCustom.Builder();
            builder .setInflater(fragment.getLayoutInflater())
                    .setStatus(DialogCustom.WARNING)
                    .setTitle(R.string.text_warning)
                    .setContent(R.string.text_warning_delete_new)
                    .setExistsCancel(true)
                    .setOnAcceptClick(v -> Cloud.getInstance()
                            .getReference(Cloud.NEWS)
                            .child(newsItem.getKey())
                            .removeValue());
            builder.build().show();
        }

        /**
         * Agrega los link presentes en el texto
         */
        public void linkifyContent(){
            BetterLinkMovementMethod
            .linkify(Linkify.ALL, itemContent)
            .setOnLinkClickListener((textView, url) ->
                    openUrlAndIncreaseViewed(url));
        }

        /**
         * Coloca la cantidad de vistos para una noticia
         */
        public void setViewedAmout(){
            itemReadAmount.setText(String.format(
            Locale.getDefault(), "%d %s",
            newsItem.getViewedAmount(),
            itemView.getContext().getResources()
                    .getString(R.string.text_viewed)));
        }

        /**
         * Carga la imagen en su vista
         * @param imageUrl: Url de la imagen
         */
        public void loadThumbnail(String imageUrl){
            Glide.with(itemView.getContext())
                .load(imageUrl)
                .apply(centerCropTransform()
                .placeholder(R.drawable.img_unavailable)
                .error(R.drawable.img_unavailable)
                .priority(Priority.HIGH)).into(itemThumbnail);
        }

        /**
         * Coloca los eventos correspondientes al tocar
         * la imagen
         */
        public void setThumbnailOnClickListener(){
            String imageUrl = newsItem.getImageUrl();
            URLSpan spans[] = itemContent.getUrls();
            if(spans.length > 0) itemThumbnail.setOnClickListener(v ->
                    openUrlAndIncreaseViewed(spans[0].getURL()));
            else itemThumbnail.setOnClickListener(v -> openImage(imageUrl));
        }

        /**
         * Abre una imagen en un fragmento aparte
         * @param imageUrl: Url de la imagen
         */
        public void openImage(String imageUrl){
            Fragment imageFragment = FragmentImage
                    .newInstance(newsItem.getTitle(), imageUrl);
            fragment.setFragmentOnScreen(imageFragment);
        }

        /**
         * Abre en el explorador una URL
         * @param url: Url que el navegador debe abrir
         */
        public void openUrl(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            fragment.getMainActivity().startActivity(intent);
        }

        /**
         * Abre la url y aumenta la cantidad de vistos
         * de la noticia
         * @param url: Url que el navegador debe abrir
         * @return True
         */
        public boolean openUrlAndIncreaseViewed(String url){
            openUrl(url);
            increaseViewed();
            return true;
        }

        /**
         * Aumenta la cantidad de visto de la publicación
         * o noticias
         */
        public void increaseViewed(){

            Cloud.getInstance()
            .getReference(Cloud.NEWS)
            .child(newsItem.getKey())
            .runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                NewsItem retrievedNew = mutableData.getValue(NewsItem.class);
                if (retrievedNew == null) return Transaction.success(mutableData);
                else retrievedNew.setViewedAmount(retrievedNew.getViewedAmount() + 1);
                mutableData.setValue(retrievedNew);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError,
                                   boolean b, DataSnapshot dataSnapshot) {
                Log.i("NewsAdapter", "increaseViewedComplete");
            }});

        }

    }

}
