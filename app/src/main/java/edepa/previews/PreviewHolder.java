package edepa.previews;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.crawler.LinkPreviewCallback;
import edepa.crawler.SourceContent;
import edepa.crawler.TextCrawler;
import edepa.custom.PhotoFragment;
import edepa.minilibs.RegexSearcher;
import edepa.model.Preview;
import edepa.modelview.R;


public abstract class PreviewHolder
        extends RecyclerView.ViewHolder implements LinkPreviewCallback  {

    /**
     * Cantidad máxima de texto que puede contener una
     * #Preview en {@link #buildDefaultPreview(SourceContent)}
     */
    public static final int MAX_TEXT_SIZE = 100;

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

    @BindView(R.id.preview_thumbnail)
    ImageView previewThumbnail;

    @BindView(R.id.uploading_view)
    View previewUploading;

    /**
     * Para poder utilizar la función
     * {@link TextCrawler#makePreview(LinkPreviewCallback, String)}
     */
    protected TextCrawler textCrawler;

    /**
     * Constructor del holder
     * @param itemView: Vista usada al instanciar el holder
     */
    public PreviewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Enlaza una url y activa las vistas necesarias
     * en el holder
     * @param url: Url de la #Preview
     */
    public abstract void bindUrl(String url);

    /**
     * Se evitan memory leaks al hacer que {@link #textCrawler}
     * deje de minar la información cuando la vista ya no existe
     */
    public void unbind(){
        if (textCrawler != null)
            textCrawler.cancel();
    }

    /**
     * Se activan los componentes necesarios para mostrar
     * la preview. Si la vista se está subiendo entonces se
     * activa el gif {@link #previewUploading}
     * @param preview: Información de la preview
     */
    public void bindPreview(Preview preview){
        if (preview.getUrl().equals("uploading")) onPre();
        else bindFixedPreview(preview);
    }

    /**
     * Es cuando la vista ya no está en estado "uploading",
     * usada únicamente por {@link #bindPreview(Preview)}
     * @param preview: Información de la preview
     */
    public void bindFixedPreview(Preview preview){

        // El link corresponde a una imagen
        boolean isImage = RegexSearcher.isImageFile(preview.getUrl());

        // Desaparece la animación de cargar y se visualiza
        // el preview ya sea multimedia o no
        previewUploading.setVisibility(GONE);
        previewItem.setVisibility(isImage ? GONE : VISIBLE);
        previewImage.setVisibility(isImage ? VISIBLE : GONE);

        // La preview es una imagen
        if(isImage){
            Context context = itemView.getContext().getApplicationContext();
            Glide.with(context)
                    .load(preview.getUrl())
                    .apply(PhotoFragment.getRequestOptions(context))
                    .into(previewImage);
        }

        // Si no es imagen se cargan el título de la página
        // el dominio y una descripción
        if (!isImage){
            String domain = RegexSearcher.findDomainFromUrl(preview.getUrl());
            previewDomain.setText(domain);
            previewHeader.setText(preview.getHeader());
            if (preview.getDescription() != null && !preview.getDescription().isEmpty()){
                previewDescription.setText(preview.getDescription());
                previewDescription.setVisibility(VISIBLE);
            }
            else {
                previewDescription.setVisibility(GONE);
            }
        }

        // Si contiene un thumbnail lo coloca
        if (!isImage && preview.getThumbnail() != null){
            Context context = itemView.getContext().getApplicationContext();
            Glide.with(context)
                    .load(preview.getThumbnail())
                    .apply(PhotoFragment.getRequestOptions(context))
                    .into(previewThumbnail);
        }

    }

    /**
     * Mientras se busca la preview en la página web,
     * se coloca el gif de {@link #previewUploading}1
     */
    @Override
    public void onPre() {
        previewItem.setVisibility(GONE);
        previewImage.setVisibility(GONE);
        previewUploading.setVisibility(VISIBLE);
    }

    /**
     * Es un callback de la función
     * {@link TextCrawler#makePreview(LinkPreviewCallback, String)}
     * @param sourceContent Clase con el contenido de la página
     * @param isNull: true si no se encontró nada en la url dada
     */
    @Override
    public void onPos(SourceContent sourceContent, boolean isNull) {
        if(!isNull) uploadPreview(buildDefaultPreview(sourceContent));
    }

    /**
     * No contiene descripción y la imagen está predefinida
     * en la base de datos
     * @param fileUrl: Enlace al documento
     * @return Preview del documento
     */
    public Preview buildDocumentPreview(String fileUrl){
        return new Preview.Builder()
                .url(fileUrl)
                .description(null)
                .header(getDocumentHeader(fileUrl))
                .thumbnail(getDocumentThumbnail(fileUrl)).build();
    }

    public String getDocumentThumbnail(String fileUrl){
        String imageUrl = getImageFromUrl(fileUrl);
        if (imageUrl == null) {
            imageUrl = MediaManager.get().url().generate("Logos/pdf_document.png");
        }
        return imageUrl;
    }

    public String getDocumentHeader(String fileUrl){
        String header = RegexSearcher.findFilenameFromUrl(fileUrl);
        if (header != null && !header.isEmpty()){
            header = decodeDocumentHeader(header);
        }
        if (header != null && !header.endsWith("pdf")) {
            header += ".pdf";
        }
        else if (header == null) {
            header = itemView.getContext().getString(R.string.text_pdf_document);
        }
        return header;
    }

    public String decodeDocumentHeader(String header){
        try {
            return URLDecoder.decode(header, "UTF-8");
        }
        catch (Exception e){
            Log.e(toString(), "Error parsing document header");
            return header;
        }
    }

    /**
     * Solo contiene la url de la imagen
     * @param fileUrl Enlace a la imagen
     * @return Preview de la imagen
     */
    public Preview buildImagePreview(String fileUrl) {
        return new Preview.Builder().url(fileUrl).build();
    }

    /**
     * Contiene todos los elementos de la preview
     * @param sourceContent: Propiedades extraídas del html
     * @return Preview de la página web
     */
    public Preview buildDefaultPreview(SourceContent sourceContent){

        String truncatedText = RegexSearcher
                .truncateText(sourceContent.getDescription(), MAX_TEXT_SIZE);

        return new Preview.Builder()
                .description(truncatedText)
                .header(sourceContent.getTitle())
                .url(sourceContent.getUrl())
                .thumbnail(getImageFromSource(sourceContent)).build();
    }

    public abstract DatabaseReference getPreviewReference();

    /**
     * Se sube a firebase y hace un callback por lo que no es
     * necesario actualizar la vista aquí por la función
     * {@link #bindUrl(String)} se ejecutará otra vez
     */
    public void uploadPreview(Preview preview){

        getPreviewReference().runTransaction(new Transaction.Handler() {

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            Preview curPreview = mutableData.getValue(Preview.class);
            if (curPreview == null) mutableData.setValue(preview);
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError,
                               boolean b, DataSnapshot dataSnapshot) {
            Log.i(toString(), "Upload transaction complete");
        }});
    }

    /**
     * Obtiene alguna de las imagenes presentes en la página web
     * @param sourceContent: Contenido de la página web
     * @return url de la imagen de la página
     */
    public String getImageFromSource(SourceContent sourceContent) {
        String url = RegexSearcher.normalize(sourceContent.getUrl());

        // Antes de retornar la url de la imagen de la página
        // se identifica si es una página de uso común y se coloca
        // una imagen personalizada (más presentable)
        String imageUrl = getImageFromUrl(url);
        if (imageUrl != null) return imageUrl;

        // De lo contrario se coloca la primera imagen que se encuentre
        else {
            List<String> images = sourceContent.getImages();
            return images.size() > 0 ? images.get(0) : null;
        }

    }

    /**
     * Se identifica si la url corresponde a una página de uso
     * común y retorna una imagen (bonita) de esa página
     * @param url: Url de la página web
     * @return Url de imagen hospedada en Cloudinary
     */
    public String getImageFromUrl(String url){

        url = RegexSearcher.normalize(url);

        if (RegexSearcher.isDocumentFile(url)){
            return MediaManager.get().url().generate("Logos/pdf_document.png");
        }
        else if (url.contains("tecdigital")){
            return MediaManager.get().url().generate("Logos/tecdigital.png");
        }
        else if (url.contains("presentation")){
            return MediaManager.get().url().generate("Logos/presentation.png");
        }
        else if (url.contains("document")){
            return MediaManager.get().url().generate("Logos/document.png");
        }
        else if (url.contains("forms")){
            return MediaManager.get().url().generate("Logos/forms.png");
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
        else if (url.contains("dropbox")){
            return MediaManager.get().url().generate("Logos/dropbox.png");
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
        else if (url.contains("spreadsheets")){
            return MediaManager.get().url().generate("Logos/spreadsheets.png");
        }
        else return null;

    }

}
