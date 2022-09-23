package edepa.notices;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.app.MainActivity;
import edepa.app.NavigationActivity;
import edepa.cloud.Cloud;
import edepa.cloud.CloudAdmin;
import edepa.cloud.CloudComments;
import edepa.cloud.CloudNotices;
import edepa.comments.CommentEditor;
import edepa.custom.PhotoFragment;
import edepa.custom.RecyclerAdapter;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.TextHighlighter;
import edepa.minilibs.TimeGenerator;
import edepa.model.Notice;
import edepa.model.Preferences;
import edepa.model.Preview;
import edepa.modelview.R;
import edepa.previews.NoticePreview;

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

        @BindView(R.id.news_item_read_amount_container)
        View itemReadAmountContainer;

        @BindView(R.id.news_item_comments_amount)
        TextView itemCommentsAmount;

        @BindView(R.id.news_item_comments_amount_container)
        View itemCommentsAmountContainer;

        @BindView(R.id.preview_image)
        ImageView itemThumbnail;

        @BindView(R.id.news_item_text)
        View itemText;

        @BindView(R.id.link_preview)
        View linkPreview;

        @BindView(R.id.news_item_delete)
        View itemDelete;

        private URLSpan [] urls;

        private int commentsAmount;

        public NewsItemHolder(View itemView) {
            super(itemView);
            commentsAmount = 0;
        }

        /**
         * {@inheritDoc}
         * @param notice Noticia con la que se crea la preview
         */
        public void bind(Notice notice){

            this.notice = notice;
            this.commentsAmount = 0;

            bindTitle();
            bindContent();
            bindTimeAgo();
            bindLinkPreview();

            bindViewedAmount();
            bindCommentsAmount();

            CloudAdmin admin = new CloudAdmin();
            admin.setAdminPermissionListener(this);
            admin.requestAdminPermission();

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

                itemContent.setText(
                        TextHighlighter.decodeSpannables(notice.getContent()));

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
                increaseViewed();
            }
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

        public void bindViewedAmount(){
            int viewed = notice.getViewed();
            itemReadAmountContainer.setVisibility(viewed > 0 ? VISIBLE : GONE);
            if (itemReadAmountContainer.getVisibility() == VISIBLE){
                itemReadAmount.setText(String.valueOf(notice.getViewed()));
            }
        }

        @OnClick(R.id.news_item_comments_amount_container)
        public void openComments(){

            if (context instanceof NavigationActivity) {
                NavigationActivity activity = (NavigationActivity) context;

                String tag = "COMMENTS_" + notice.getKey();
                Fragment frag = NoticeComments.newInstance(notice);

                if (commentsAmount > 0) {
                    activity.setFragmentOnScreen(frag, tag);
                }

                else {
                    String tagEditor = "COMMENT_EDITOR";
                    CommentEditor editor = new CommentEditor();
                    editor.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    editor.setCommentSender(comment -> {
                        CloudComments.getNoticeCommentsQuery(notice)
                                .getRef().push().setValue(comment);
                        activity.setFragmentOnScreen(frag, tag);
                    });
                    editor.show(activity.getSupportFragmentManager(), tagEditor);
                }

            }

        }

        public void bindCommentsAmount(){

            if (Preferences.getBooleanPreference(context, Preferences.COMMENTS_KEY)){
                itemCommentsAmountContainer.setVisibility(VISIBLE);
            }
            else {
                itemCommentsAmountContainer.setVisibility(GONE);
            }
            if (itemCommentsAmountContainer.getVisibility() == VISIBLE) {
                CloudComments.getNoticeCommentsQuery(notice)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                commentsAmount = (int) dataSnapshot.getChildrenCount();
                                itemCommentsAmount.setText(String.valueOf(commentsAmount));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }

        /**
         * Abre una imagen en un fragmento aparte
         * @param imageUrl: Url de la imagen
         */
        public void openImage(String imageUrl){
            Fragment imageFragment = PhotoFragment
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
            try{ tryToOpenUrl(url); }
            catch (Exception exception){ handleOpenUrlException(exception); }
        }

        private void tryToOpenUrl(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if(context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
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
