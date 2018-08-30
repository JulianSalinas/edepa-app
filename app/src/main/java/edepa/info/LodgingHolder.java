package edepa.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edepa.app.ActivityMain;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.RegexSearcher;
import edepa.model.Lodging;
import edepa.modelview.R;

public class LodgingHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_text)
    TextView nameText;

    @BindView(R.id.description_text)
    TextView descriptionText;

    @BindView(R.id.lodging_web)
    TextView lodgingWeb;

    public LodgingHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Lodging lodging){
        nameText.setText(lodging.getName());
        descriptionText.setText(lodging.getLocation());

        String domain = RegexSearcher.findDomainFromUrl(lodging.getWeb());
        lodgingWeb.setVisibility(domain != null ? View.VISIBLE : View.GONE);

        if (lodgingWeb.getVisibility() == View.VISIBLE){
            lodgingWeb.setText(domain);
        }

        Linkify.addLinks(lodgingWeb, Linkify.ALL);
        itemView.setOnClickListener(v -> openUrl(lodging.getWeb()));
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
        Context context = itemView.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if(context instanceof ActivityMain) {
            ActivityMain activity = (ActivityMain) context;
            activity.startActivity(intent);
        }
    }

    private void handleOpenUrlException(Exception exception){
        new DialogFancy.Builder()
                .setContext(itemView.getContext())
                .setStatus(DialogFancy.ERROR)
                .setTitle(R.string.text_invalid_link)
                .setContent(R.string.text_invalid_link_content)
                .build().show();
        Log.e(toString(), exception.getMessage());
    }

}
