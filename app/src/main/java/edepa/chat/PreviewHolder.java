package edepa.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.custom.FragmentImage;
import edepa.minilibs.RegexSearcher;
import edepa.model.Preview;
import edepa.modelview.R;
import edepa.preview.LinkPreviewCallback;
import edepa.preview.SourceContent;
import edepa.preview.TextCrawler;

public abstract class PreviewHolder
        extends RecyclerView.ViewHolder implements LinkPreviewCallback  {

    public static final int MAX_TEXT_SIZE = 80;

    @BindView(R.id.link_preview)
    View linkPreview;

    @BindView(R.id.preview_item)
    View previewItem;

    @BindView(R.id.preview_header)
    TextView previewHeader;

    @BindView(R.id.preview_domain)
    TextView previewDomain;

    @BindView(R.id.preview_descripcion)
    TextView previewDescription;

    @BindView(R.id.preview_image)
    ImageView previewImage;

    @BindView(R.id.uploading_view)
    View uploadingView;

    protected TextCrawler textCrawler;

    public PreviewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void unbind(){
        if (textCrawler != null)
            textCrawler.cancel();
    }

    public void bindPreview(Preview preview){

        uploadingView.setVisibility(View.GONE);
        previewItem.setVisibility(View.VISIBLE);

        previewDomain.setText(preview.getUrl());
        previewHeader.setText(preview.getHeader());
        previewDescription.setText(preview.getDescription());

        if (preview.getImageUrl() != null){
            Context context = itemView.getContext().getApplicationContext();
            Glide.with(context)
                    .load(preview.getImageUrl())
                    .apply(FragmentImage.getRequestOptions(context))
                    .into(previewImage);
        }
    }

    @Override
    public void onPre() {
        previewItem.setVisibility(View.GONE);
        uploadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPos(SourceContent sourceContent, boolean isNull) {
        if(!isNull) Log.i("Preview", "Source content retrieve and its not null");
    }

    public Preview buildPdfPreview(String fileUrl){

        String imageUrl = MediaManager.get().url()
                .generate("Logos/pdf_document.png");

        fileUrl = RegexSearcher.truncateText(fileUrl, MAX_TEXT_SIZE);

        return new Preview.Builder()
                .header(RegexSearcher.findFilenameFromUrl(fileUrl))
                .url(RegexSearcher.findDomainFromUrl(fileUrl))
                .description(fileUrl)
                .imageUrl(imageUrl).build();
    }

    public Preview buildPreview(SourceContent sourceContent){

        String truncatedText = RegexSearcher
                .truncateText(sourceContent.getDescription(), MAX_TEXT_SIZE);

        return new Preview.Builder()
                .header(sourceContent.getTitle())
                .url(sourceContent.getCannonicalUrl())
                .description(truncatedText)
                .imageUrl(getImageFromSource(sourceContent)).build();
    }

    public String getImageFromSource(SourceContent sourceContent) {
        String url = sourceContent.getUrl();
        url = RegexSearcher.normalize(url);
        String imageUrl = getImageFromUrl(url);
        if (imageUrl != null) return imageUrl;
        else {
            List<String> images = sourceContent.getImages();
            return images.size() > 0 ? images.get(0) : null;
        }
    }

    public String getImageFromUrl(String url){

        url = RegexSearcher.normalize(url);

        if (url.contains("tecdigital")){
            return MediaManager.get().url().generate("Logos/tecdigital.png");
        }
        else if (url.contains("presentation")){
            return MediaManager.get().url().generate("Logos/presentation.png");
        }
        else if (url.contains("document")){
            return MediaManager.get().url().generate("Logos/document.png");
        }
        else if (url.contains("facebook")){
            return MediaManager.get().url().generate("Logos/facebook.png");
        }
        else if (url.contains("twitter")){
            return MediaManager.get().url().generate("Logos/twitter.png");
        }
        else if (url.contains("drive")){
            return MediaManager.get().url().generate("Logos/drive.png");
        }
        else if (url.contains("youtube")){
            return MediaManager.get().url().generate("Logos/youtube.png");
        }
        else if (url.contains("whatsapp")){
            return MediaManager.get().url().generate("Logos/whatsapp.png");
        }
        else if (url.contains("telegram")){
            return MediaManager.get().url().generate("Logos/telegram.png");
        }
        else if (url.contains("skype")){
            return MediaManager.get().url().generate("Logos/skype.png");
        }
        else if (url.contains("dropbox")){
            return MediaManager.get().url().generate("Logos/dropbox.png");
        }
        else if (url.contains("spreadsheets")){
            return MediaManager.get().url().generate("Logos/spreadsheets.png");
        }
        else return null;

    }

}
