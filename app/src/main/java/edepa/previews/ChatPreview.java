package edepa.previews;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

import edepa.cloud.CloudChat;
import edepa.crawler.LinkPreviewCallback;
import edepa.crawler.TextCrawler;
import edepa.minilibs.RegexSearcher;
import edepa.model.Message;


public abstract class ChatPreview
        extends PreviewHolder implements LinkPreviewCallback  {

    /**
     * Mensaje con el que se debe crear la preview
     */
    protected Message message;

    public ChatPreview(View itemView) {
        super(itemView);
    }

    /**
     * Obliga a la subclases a que el mensaje no este en null
     * @param msg Mensaje con la que se crea la preview
     */
    public abstract void bind(Message msg);

    /**
     * Crea una preview con base a la url
     * @param url Url que estaba contenida en el content
     *            de {@link #message}
     */
    public void bindUrl(String url){

        // Ya existe una preview por lo que solo se coloca
        if(message.getPreview() != null) {
            bindPreview(message.getPreview());
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
        return CloudChat.getPreviewReference(message);
    }

}
