package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;
import butterknife.BindView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.ColorConverter;


public class ScheduleDetail extends MainActivityFragment {

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    private FavoriteList favoriteList = FavoriteList.getInstance();

    /**
     * Componentes visuales para mostrar los detalles de un evento
     */
    @BindView(R.id.header)
    TextView headerTextView;

    @BindView(R.id.emphasis_image_view)
    ImageView emphasisImageView;

    @BindView(R.id.favorite_button)
    LikeButton favoriteButton;

    @BindView(R.id.button_back)
    View buttonBack;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleDetail newInstance(ScheduleEvent event) {

        ScheduleDetail fragment = new ScheduleDetail();

        Bundle args = new Bundle();
        args.putParcelable("event", event);

        fragment.setArguments(args);
        return fragment;

    }

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        resource = R.layout.schedule_detail;

        Bundle args = getArguments();
        if(args != null)
            event = args.getParcelable("event");

    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        bindInformation();
    }

    /**
     * Coloca la información del evento en cada uno de los componentes
     */
    private void bindInformation(){

//        briefTextView.setText(event.getBrief("es"));
//        Linkify.addLinks(briefTextView, Linkify.WEB_URLS);

        headerTextView.setText(event.getTitle());
//        eventypeTextView.setText(event.getEventype().toString());

        int color = getResources().getColor(event.getEventype().getColor());
        emphasisImageView.setBackgroundColor(color);

//        iconMap.setOnClickListener(v -> switchFragment(new InformationMap()));
        buttonBack.setOnClickListener(v -> activity.onBackPressed());

        favoriteButton.setLiked(favoriteList.contains(event));
        favoriteButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                activity.addFavorite(event);
                showStatusMessage(R.string.text_marked_as_favorite);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                activity.removeFavorite(event);
                showStatusMessage(R.string.text_unmarked_as_favorite);
            }

        });

    }

}