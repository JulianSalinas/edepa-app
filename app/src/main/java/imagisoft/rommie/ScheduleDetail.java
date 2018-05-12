package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;
import butterknife.BindView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.ColorConverter;


public class ScheduleDetail extends ExhibitorsViewFragment {

    int statusBarColor;

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    /**
     * Componentes visuales para mostrar los detalles de un evento
     */
    @BindView(R.id.brief)
    TextView briefTextView;

    @BindView(R.id.header)
    TextView headerTextView;

    @BindView(R.id.icon_map)
    View iconMap;

    @BindView(R.id.eventype)
    TextView eventypeTextView;

    @BindView(R.id.emphasis_image_view)
    ImageView emphasisImageView;

    @BindView(R.id.exhibitor_recycler_view)
    RecyclerView exhibitorsRecyclerView;

    @BindView(R.id.favorite_button)
    MaterialFavoriteButton favoriteButton;

    @BindView(R.id.button_back)
    View buttonBack;

    /**
     * Obtiene todos los expositores que maneja la vista
     */
    @Override
    public List<Exhibitor> getExhibitors() {
        return event.getExhibitors();
    }

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
        setToolbarVisibility(View.GONE);
        setTabLayoutVisibility(View.GONE);
        statusBarColor = getStatusBarColor();
        bindInformation();
        setupExhibitorsView();

    }

    /**
     * Al cambiar a otra sección se deben volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColor(statusBarColor);

    }

    /**
     * Coloca la información del evento en cada uno de los componentes
     */
    private void bindInformation(){

        briefTextView.setText(event.getBrief("es"));
        Linkify.addLinks(briefTextView, Linkify.WEB_URLS);

        headerTextView.setText(event.getTitle());
        eventypeTextView.setText(event.getEventype().toString());

        int color = getResources().getColor(event.getEventype().getColor());
        emphasisImageView.setBackgroundColor(color);
        setStatusBarColor(ColorConverter.darken(color, 12));

        iconMap.setOnClickListener(v -> switchFragment(new InformationMap()));
        buttonBack.setOnClickListener(v -> activity.onBackPressed());

        favoriteButton.setFavorite(FavoriteList.getInstance().getSortedEvents().contains(event));

        favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
            List<ScheduleEvent> events = FavoriteList.getInstance().getSortedEvents();
            if(!events.contains(event)) events.add(event);
            else events.remove(event);
            String msg = getResources().getString(R.string.text_marked_as_favorite);
            showStatusMessage(msg);
            FavoriteList.getInstance().saveFavorites(activity);
        });

    }

    /**
     * Se configura el exhibitorsRecyclerView que contiene los expositores
     */
    public void setupExhibitorsView(){

        if (exhibitorsAdapter == null)
            exhibitorsAdapter = new ExhibitorsViewAdapter(this);

        exhibitorsRecyclerView.setHasFixedSize(true);
        exhibitorsRecyclerView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsRecyclerView.setAdapter(exhibitorsAdapter);

    }

}