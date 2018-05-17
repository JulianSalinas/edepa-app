package imagisoft.rommie;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import butterknife.BindView;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.DateConverter;


public class ScheduleDetailFront extends MainActivityFragment implements OnLikeListener{

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    /**
     * Es para marcar la estrella en caso que se necesite
     */
    private FavoriteList favoriteList;

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

    @BindView(R.id.readmore)
    View readmore;

    @BindView(R.id.schedule_detail_time)
    TextView timeTextView;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleDetailFront newInstance(ScheduleEvent event) {

        ScheduleDetailFront fragment = new ScheduleDetailFront();

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
        this.resource = R.layout.schedule_detail;
        this.favoriteList = FavoriteList.getInstance();

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
    @SuppressLint("SetTextI18n")
    private void bindInformation(){

        headerTextView.setText(event.getTitle());
        timeTextView.setText(
                DateConverter.extractDate(event.getStart()) + "\n" +
                DateConverter.extractTime(event.getStart()));

        int color = getResources().getColor(event.getEventype().getColor());
        emphasisImageView.setBackgroundColor(color);
        buttonBack.setOnClickListener(v -> activity.onBackPressed());

        favoriteButton.setLiked(favoriteList.contains(event));
        favoriteButton.setOnLikeListener(this);

        readmore.setOnClickListener(v -> {
            Fragment parent = getParentFragment();
            ScheduleDetailPager pager = ((ScheduleDetailPager) parent);
            pager.pager.setCurrentItem(1);
        });

    }

    @Override
    public void liked(LikeButton likeButton) {
        favoriteList.addEvent(event);
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        favoriteList.removeEvent(event);
    }

}