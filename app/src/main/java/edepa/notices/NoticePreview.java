package edepa.notices;

import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.List;

import edepa.cloud.Cloud;
import edepa.cloud.CloudNotices;
import edepa.minilibs.RegexSearcher;
import edepa.model.Notice;
import edepa.model.Preview;
import edepa.preview.LinkPreviewCallback;
import edepa.preview.SourceContent;
import edepa.preview.TextCrawler;


public abstract class NoticePreview
        extends PreviewHolder implements LinkPreviewCallback  {

    protected Notice notice;

    public NoticePreview(View itemView) {
        super(itemView);
    }

    public abstract void bind(Notice notice);

    public void bindUrl(String url){
        if(notice.getPreview() != null) {
            bindPreview(notice.getPreview());
        }
        else if (RegexSearcher.isPdfFile(url)){
            uploadPreview(buildPdfPreview(url));
        }
        else{
            textCrawler = new TextCrawler();
            textCrawler.makePreview(this, url);
        }
    }

    @Override
    public void onPos(SourceContent sourceContent, boolean isNull) {
        if(!isNull){

            String title = sourceContent.getTitle();

            if(title == null || title.isEmpty()){
                List<String> images = sourceContent.getImages();
                if(images.size() > 0) uploadImageUrl(images.get(0));
            }

            else {
                Preview preview = buildPreview(sourceContent);
                bindPreview(preview);
                uploadPreview(preview);
            }

        }
    }

    public DatabaseReference getPreviewReference(){
        return Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .child(notice.getKey())
                .child("preview");
    }

    public DatabaseReference getImageReference(){
        return Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .child(notice.getKey())
                .child("imageUrl");
    }

    public void uploadPreview(Preview preview){

        getPreviewReference().runTransaction(new Transaction.Handler() {

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            Preview curPreview = mutableData.getValue(Preview.class);
            if (curPreview == null)
                mutableData.setValue(preview);
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError,
                               boolean b, DataSnapshot dataSnapshot) {
        }});

    }

    public void uploadImageUrl(String imageUrl){

        getImageReference().runTransaction(new Transaction.Handler() {

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            String curPreview = mutableData.getValue(String.class);
            if (curPreview == null) mutableData.setValue(imageUrl);
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError,
                               boolean b, DataSnapshot dataSnapshot) {
        }});

    }

}
