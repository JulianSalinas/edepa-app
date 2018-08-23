package edepa.previews;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

import edepa.cloud.CloudNotices;
import edepa.minilibs.RegexSearcher;
import edepa.model.Notice;
import edepa.crawler.LinkPreviewCallback;
import edepa.crawler.TextCrawler;


public abstract class NoticePreview
        extends PreviewHolder implements LinkPreviewCallback  {

    /**
     * Noticia con la que se debe crear la preview
     */
    protected Notice notice;

    public NoticePreview(View itemView) {
        super(itemView);
    }

    /**
     * Obliga a la subclases a que la noticia no quede
     * en null
     * @param notice Noticia con la que se crea la preview
     */
    public abstract void bind(Notice notice);

    /**
     * Crea una preview con base a la url
     * @param url Url que estaba contenida en el content
     *            de la {@link #notice}
     */
    public void bindUrl(String url){

        // Ya existe una preview por lo que solo se coloca
        if(notice.getPreview() != null) {
            bindPreview(notice.getPreview());
        }

        // No existe la preview por lo que se crea una
        else if (RegexSearcher.isImageFile(url)){
            uploadPreview(buildImagePreview(url));
        }

        // No existe la preview pero es un documento
        else if (RegexSearcher.isDocumentFile(url)){
            uploadPreview(buildDocumentPreview(url));
        }

        // Se debe crear una preview por defecto
        else{
            textCrawler = new TextCrawler();
            textCrawler.makePreview(this, url);
        }
    }

    @Override
    public DatabaseReference getPreviewReference() {
        return CloudNotices.getPreviewReference(notice);
    }

}
