package edepa.notices;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
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
import edepa.app.MainActivity;
import edepa.app.NavigationActivity;
import edepa.cloud.CloudAdmin;
import edepa.cloud.CloudNotices;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.TimeGenerator;
import edepa.cloud.Cloud;
import edepa.model.Notice;
import edepa.modelview.R;
import edepa.custom.FragmentImage;
import edepa.custom.RecyclerAdapter;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class NoticesAdapter extends RecyclerAdapter implements CloudNotices.Callbacks {

    protected Context context;

    protected List<Notice> notices;

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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         ((NewsItemHolder) holder).bind();
    }

    public class NewsItemHolder extends RecyclerView.ViewHolder {

        private int position;

        private Notice notice;

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

        @BindView(R.id.uploading_view)
        View uploading_view;

        @BindView(R.id.news_item_delete)
        View itemDelete;

        public NewsItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(){

            this.position = getAdapterPosition();
            this.notice = notices.get(position);

            itemTitle.setText(notice.getTitle());
            itemContent.setText(notice.getContent());
            linkifyContent();

            TimeGenerator calc = new TimeGenerator(itemView.getContext());
            itemTimeAgp.setText(calc.getTimeAgo(notice.getTime()));

            int viewed = notice.getViewed();
            itemReadAmount.setVisibility(viewed > 0 ? View.VISIBLE : View.GONE);
            if (itemReadAmount.getVisibility() == View.VISIBLE) setViewedAmout();

            String imageUrl = notice.getImageUrl();
            itemThumbnail.setVisibility(imageUrl == null ? View.GONE: View.VISIBLE);
            if (itemThumbnail.getVisibility() == View.VISIBLE) loadThumbnail(imageUrl);
            setThumbnailOnClickListener();

            itemDelete.setVisibility(View.GONE);
            CloudAdmin admin = new CloudAdmin();
            admin.setAdminPermissionListener(new CloudAdmin.AdminPermissionListener() {

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
            notice.getViewed(),
            itemView.getContext().getResources()
                    .getString(R.string.text_viewed)));
        }

        /**
         * Carga la imagen en su vista
         * @param imageUrl: Url de la imagen
         */
        public void loadThumbnail(String imageUrl){
            boolean uploading = imageUrl.equals("uploading");
            itemThumbnail.setVisibility(uploading ? View.GONE: View.VISIBLE);
            uploading_view.setVisibility(uploading ? View.VISIBLE: View.GONE);
            if (itemThumbnail.getVisibility() == View.VISIBLE)
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .apply(FragmentImage.getRequestOptions(context))
                        .into(itemThumbnail);
        }

        /**
         * Coloca los eventos correspondientes al tocar
         * la imagen
         */
        public void setThumbnailOnClickListener(){
            String imageUrl = notice.getImageUrl();
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

    }

}
