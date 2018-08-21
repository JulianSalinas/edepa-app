package edepa.notices;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.app.MainActivity;
import edepa.cloud.CloudAdmin;
import edepa.cloud.CloudNotices;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.TimeGenerator;
import edepa.cloud.Cloud;
import edepa.model.Notice;
import edepa.modelview.R;
import edepa.custom.FragmentImage;
import edepa.custom.RecyclerAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NoticesAdapter extends RecyclerAdapter implements CloudNotices.Callbacks {

    protected Context context;
    protected List<Notice> notices;
    protected TimeGenerator timeGenerator;
    protected ColorGenerator colorGenerator;

    @Override
    public int getItemCount() {
        return notices.size();
    }

    @Override
    public void addNotice(Notice notice){
        notices.add(0, notice);
        notifyItemInserted(0);
    }

    public void removeNotice(Notice item){
        int index = notices.indexOf(item);
        if (index != -1) {
            notices.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void changeNotice(Notice item){
        int index = notices.indexOf(item);
        if (index != -1) {
            notices.set(index, item);
            notifyItemChanged(index);
        }
    }

    public NoticesAdapter(Context context) {
        this.context = context;
        this.notices = new ArrayList<>();
        this.timeGenerator = new TimeGenerator(context);
        this.colorGenerator = new ColorGenerator(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Notice notice = notices.get(holder.getAdapterPosition());
        ((NewsItemHolder) holder).bind(notice);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((NewsItemHolder) holder).unbind();
        super.onViewDetachedFromWindow(holder);
    }

    public class NewsItemHolder extends NoticePreview
            implements CloudAdmin.AdminPermissionListener {

        @BindView(R.id.news_item_title)
        TextView itemTitle;

        @BindView(R.id.news_item_content)
        TextView itemContent;

        @BindView(R.id.news_item_time_ago)
        TextView itemTimeAgp;

        @BindView(R.id.news_item_read_amount)
        TextView itemReadAmount;

        @BindView(R.id.news_item_thumbnail)
        ImageView itemThumbnail;

        @BindView(R.id.news_item_delete)
        View itemDelete;

        private URLSpan [] urls;

        public NewsItemHolder(View itemView) {
            super(itemView);
        }

        public void bind(Notice notice){

            this.urls =  null;
            this.notice = notice;

            bindTitle();
            bindContent();
            bindViewed();
            bindTimeAgo();
            bindThumbnail();
            bindLinkPreview();
            itemDelete.setVisibility(GONE);

            CloudAdmin admin = new CloudAdmin();
            admin.setAdminPermissionListener(this);
            admin.requestAdminPermission();

        }

        /**
         * Coloca el título en la vista. Si no hay título
         * esconde la vista vacía
         */
        public void bindTitle(){
            boolean isNull = notice.getTitle() == null;
            itemTitle.setVisibility(isNull ? GONE : VISIBLE);
            if(itemTitle.getVisibility() == VISIBLE)
                itemTitle.setText(notice.getTitle());
        }

        /**
         * Coloca el contenido de la noticia. Si no hay
         * contenido se oculata la vista vacia
         */
        public void bindContent(){

            boolean isNull = notice.getContent() == null;
            itemContent.setVisibility(isNull ? GONE : VISIBLE);
            if(itemContent.getVisibility() == VISIBLE) {

                itemContent.setText(notice.getContent());
                Linkify.addLinks(itemContent, Linkify.ALL);
                urls = itemContent.getUrls().clone();
                String text = itemContent.getText().toString();

                for (URLSpan span: itemContent.getUrls()){
                    String url = span.getURL();
                    url = url.replaceAll("https?:\\/\\/", "");
                    text = text.replace(url, "");
                    text = text.replaceAll("https?:\\/\\/", "");
                }

                itemContent.setVisibility(text == null || text.isEmpty() ? GONE : VISIBLE);
                if(itemContent.getVisibility() == VISIBLE)
                    itemContent.setText(text);
            }
        }

        /**
         * Coloca la imagen de la noticia. Si no hay imagen
         * se trata de colocar una previsualizanción de un link en
         * caso de  que haya alguno en el contenido
         */
        public void bindThumbnail() {
            String imageUrl = notice.getImageUrl();
            itemThumbnail.setVisibility(imageUrl == null ? GONE: VISIBLE);
            if (itemThumbnail.getVisibility() == VISIBLE)
                loadThumbnail(imageUrl);
        }

        /**
         * Carga la imagen en su vista
         * @param imageUrl: Url de la imagen
         */
        public void loadThumbnail(String imageUrl){
            boolean uploading = imageUrl.equals("uploading");
            itemThumbnail.setVisibility(uploading ? GONE: VISIBLE);
            uploadingView.setVisibility(uploading ? VISIBLE: GONE);
            if (itemThumbnail.getVisibility() == VISIBLE) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .apply(FragmentImage.getRequestOptions(context))
                        .into(itemThumbnail);
            }
        }

        /**
         * Coloca una preview de la url en caso de que exista url
         * y la noticia no tenga una imagen definida
         */
        public void bindLinkPreview() {
            if(urls != null) {
                boolean visible = urls.length > 0 && notice.getImageUrl() == null;
                previewItem.setVisibility(visible ? VISIBLE : GONE);
                if (visible) super.bindUrl(urls[0].getURL());
            }
            else previewItem.setVisibility(GONE);

        }

        /**
         * Coloca los eventos correspondientes al tocar
         * la imagen
         */
        @OnClick({R.id.news_item_thumbnail, R.id.preview_item})
        public void onThumbnailClickListener(){
            String imageUrl = notice.getImageUrl();
            if(urls != null && urls.length > 0)
                openUrlAndIncreaseViewed(urls[0].getURL());
            else openImage(imageUrl);
        }

        private void bindTimeAgo() {
            String timeAgo = timeGenerator.getTimeAgo(notice.getTime());
            itemTimeAgp.setText(timeAgo);
        }

        @OnClick(R.id.news_item_delete)
        public void deleteItem(){
            DialogFancy.Builder builder = new DialogFancy.Builder();
            if (context instanceof MainActivity){
                builder .setContext(context)
                        .setStatus(DialogFancy.WARNING)
                        .setTitle(R.string.text_warning)
                        .setContent(R.string.text_warning_delete_new)
                        .setExistsCancel(true)
                        .setOnAcceptClick(v -> CloudNotices.removeNotice(notice));
                builder.build().show();
            }
        }

        public void bindViewed(){
            int viewed = notice.getViewed();
            itemReadAmount.setVisibility(viewed > 0 ? VISIBLE : GONE);
            if (itemReadAmount.getVisibility() == VISIBLE) setViewedAmout();
        }

        /**
         * Coloca la cantidad de vistos para una noticia
         */
        public void setViewedAmout(){
            itemReadAmount.setText(String.format(
            Locale.getDefault(), "%d %s",
            notice.getViewed(),
            itemView.getContext().getResources()
                    .getString(R.string.text_viewed)));
        }

        /**
         * Abre una imagen en un fragmento aparte
         * @param imageUrl: Url de la imagen
         */
        public void openImage(String imageUrl){
            Fragment imageFragment = FragmentImage
                    .newInstance(notice.getTitle(), imageUrl);
            if(context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
                activity.setFragmentOnScreen(imageFragment, notice.getKey());
            }
        }

        /**
         * Abre en el explorador una URL
         * @param url: Url que el navegador debe abrir
         */
        public void openUrl(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if(context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
                activity.startActivity(intent);
            }
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
                .child(notice.getKey())
                .runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Notice notice = mutableData.getValue(Notice.class);
                if (notice == null) return Transaction.success(mutableData);
                else notice.setViewed(notice.getViewed() + 1);
                mutableData.setValue(notice);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError,
                                   boolean b, DataSnapshot dataSnapshot) {
                Log.i("NoticesAdapter", "increaseViewedComplete");
            }});

        }

        @Override
        public void onPermissionGranted() {
            itemDelete.setVisibility(VISIBLE);
        }

        @Override
        public void onPermissionDenied() {
            itemDelete.setVisibility(GONE);
        }

    }

}
