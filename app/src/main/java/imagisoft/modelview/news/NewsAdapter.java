package imagisoft.modelview.news;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.misc.DateCalculator;
import imagisoft.model.NewsItem;
import imagisoft.modelview.R;
import imagisoft.modelview.views.ImageFragment;
import imagisoft.modelview.views.RecyclerAdapter;

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

        public NewsItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(){

            this.position = getAdapterPosition();
            this.newsItem = items.get(position);

            itemTitle.setText(newsItem.getTitle());
            itemContent.setText(newsItem.getContent());
            Linkify.addLinks(itemContent, Linkify.ALL);

            DateCalculator calc = new DateCalculator(itemView.getContext());
            itemTimeAgp.setText(calc.getTimeAgo(newsItem.getTime()));

            itemReadAmount.setText(String.format("%s %s",
                    newsItem.getViewedAmount().toString(),
                    itemView.getContext().getResources()
                            .getString(R.string.text_viewed)));

            String imageUrl = newsItem.getImageUrl();
            itemThumbnail.setVisibility(
                    imageUrl == null ? View.GONE: View.VISIBLE);

            if (itemThumbnail.getVisibility() == View.VISIBLE){
                Glide.with(itemView.getContext()).load(imageUrl)
                        .apply(centerCropTransform()
                                .placeholder(R.drawable.img_not_available)
                                .error(R.drawable.img_not_available)
                                .priority(Priority.HIGH)).into(itemThumbnail);
            }

            URLSpan spans[] = itemContent.getUrls();

            if(spans.length > 0){
                itemThumbnail.setOnClickListener(v -> {
                    String url = spans[0].getURL();
                    fragment.getMainActivity()
                            .startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                });
            }
            else{
                itemThumbnail.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putString("imageUrl", imageUrl);
                    ImageFragment imageFragment = new ImageFragment();
                    imageFragment.setArguments(args);
                    fragment.setFragmentOnScreen(imageFragment);
                });
            }

        }

    }

}
