package edepa.notices;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.app.ActivityMain;
import edepa.cloud.CloudAdmin;
import edepa.cloud.CloudNotices;
import edepa.custom.PhotoFragment;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.TimeGenerator;
import edepa.cloud.Cloud;
import edepa.model.Notice;
import edepa.model.Preview;
import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;
import edepa.previews.NoticePreview;

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
                .inflate(R.layout.notices_item, parent, false);
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

        @BindView(R.id.preview_image)
        ImageView itemThumbnail;

        @BindView(R.id.news_item_text)
        View itemText;

        @BindView(R.id.link_preview)
        View linkPreview;

        @BindView(R.id.news_item_delete)
        View itemDelete;

        private URLSpan [] urls;

        public NewsItemHolder(View itemView) {
            super(itemView);
        }

        /**
         * {@inheritDoc}
         * @param notice Noticia con la que se crea la preview
         */
        public void bind(Notice notice){

            this.notice = notice;

            bindTitle();
            bindContent();
            bindBothTexts();
            bindViewed();
            bindTimeAgo();
            bindLinkPreview();

            CloudAdmin admin = new CloudAdmin();
            admin.setAdminPermissionListener(this);
            admin.requestAdminPermission();

        }

        private void bindBothTexts() {
            String title = notice.getTitle();
            String content = notice.getContent();
            if ((title != null && !title.isEmpty()) || (content != null && !content.isEmpty())){
                itemText.setVisibility(VISIBLE);
            }
            else {
                itemText.setVisibility(GONE);
            }
        }

        /**
         * Coloca el título en la vista. Si no hay título
         * esconde la vista vacía
         */
        public void bindTitle(){
            boolean visible = notice.getTitle() != null;
            itemTitle.setVisibility(visible ? VISIBLE : GONE);
            if(itemTitle.getVisibility() == VISIBLE)
                itemTitle.setText(notice.getTitle());
        }

        /**
         * Coloca el contenido de la noticia. Si no hay
         * contenido se oculata la vista vacia
         */
        public void bindContent(){

            boolean visible = notice.getContent() != null;
            itemContent.setVisibility(visible ? VISIBLE : GONE);
            if(visible) {

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

                visible = text != null && !text.isEmpty();
                itemContent.setVisibility(visible ? VISIBLE : GONE);
                if(visible) itemContent.setText(text);
            }
        }

        /**
         * Coloca una preview de la url en caso de que exista url
         * y la noticia no tenga una imagen definida
         */
        public void bindLinkPreview() {
            if (notice.getPreview() != null){
                linkPreview.setVisibility(VISIBLE);
                bindPreview(notice.getPreview());
            }
            else if (urls != null && urls.length > 0){
                linkPreview.setVisibility(VISIBLE);
                bindUrl(urls[0].getURL());
            }
            else linkPreview.setVisibility(GONE);
        }

        /**
         * Coloca los eventos correspondientes al tocar
         * la imagen
         */
        @OnClick(R.id.preview_item)
        public void onPreviewItemClick(){
            if (notice.getPreview() != null){
                Preview preview = notice.getPreview();
                String url = preview.getUrl();
                openUrlAndIncreaseViewed(url);
            }
        }

        @OnClick(R.id.preview_image)
        public void onPreviewImageClick(){
            if (notice.getPreview() != null){
                Preview preview = notice.getPreview();
                openImage(preview.getUrl());
            }
        }

        private void bindTimeAgo() {
            String timeAgo = timeGenerator.getTimeAgo(notice.getTime());
            itemTimeAgp.setText(timeAgo);
        }

        @OnClick(R.id.news_item_delete)
        public void deleteItem(){
            DialogFancy.Builder builder = new DialogFancy.Builder();
            if (context instanceof ActivityMain){
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
            int viewed = notice.getViewed();
            String text = context.getString(R.string.text_viewed);
            itemReadAmount.setText(String.format(
            Locale.getDefault(), "%d %s", viewed, text));
        }

        /**
         * Abre una imagen en un fragmento aparte
         * @param imageUrl: Url de la imagen
         */
        public void openImage(String imageUrl){
            Fragment imageFragment = PhotoFragment
                    .newInstance(notice.getTitle(), imageUrl);
            if(context instanceof ActivityMain) {
                ActivityMain activity = (ActivityMain) context;
                activity.setFragmentOnScreen(imageFragment, notice.getKey());
            }
        }

        /**
         * Abre en el explorador una URL
         * @param url: Url que el navegador debe abrir
         */
        public void openUrl(String url){
            try{ tryToOpenUrl(url); }
            catch (Exception exception){ handleOpenUrlException(exception); }
        }

        private void tryToOpenUrl(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if(context instanceof ActivityMain) {
                ActivityMain activity = (ActivityMain) context;
                activity.startActivity(intent);
            }
        }

        private void handleOpenUrlException(Exception exception){
            new DialogFancy.Builder()
                    .setContext(context)
                    .setStatus(DialogFancy.ERROR)
                    .setTitle(R.string.text_invalid_link)
                    .setContent(R.string.text_invalid_link_content)
                    .build().show();
            Log.e(toString(), exception.getMessage());
        }

        /**
         * Abre la url y aumenta la cantidad de vistos
         * de la noticia
         * @param url: Url que el navegador debe abrir
         */
        public void openUrlAndIncreaseViewed(String url){
            openUrl(url);
            increaseViewed();
        }

        /**
         * Aumenta la cantidad de vistos de la noticia publicada
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
