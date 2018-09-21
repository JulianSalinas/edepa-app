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
import edepa.app.MainActivity;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.DialogFancy;
import edepa.model.Place;
import edepa.modelview.R;


public class RestaurantHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_text)
    TextView nameText;

    @BindView(R.id.description_text)
    TextView descriptionText;

    @BindView(R.id.lodging_web)
    TextView lodgingWeb;

    @BindView(R.id.lodging_emphasis_view)
    View emphasisView;

    @BindView(R.id.lodging_emphasis_view_tag)
    View emphasisViewTag;

    private ColorGenerator colorGenerator;

    public RestaurantHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        colorGenerator = new ColorGenerator(itemView.getContext());
    }

    public void bind(Place lodging){
        nameText.setText(lodging.getName());
        descriptionText.setText(lodging.getLocation());

        String domain = lodging.getWeb();
        lodgingWeb.setVisibility(domain != null ? View.VISIBLE : View.GONE);

        if (lodgingWeb.getVisibility() == View.VISIBLE){
            lodgingWeb.setText(domain);
        }

        Linkify.addLinks(lodgingWeb, Linkify.ALL);
        itemView.setOnClickListener(v -> openUrl(lodging.getWeb()));

        int color = colorGenerator.getColor(lodging.getName());
        emphasisView.setBackgroundColor(color);
        emphasisViewTag.setBackgroundColor(color);

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
        if(context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
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
