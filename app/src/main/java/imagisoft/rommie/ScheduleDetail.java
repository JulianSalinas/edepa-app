package imagisoft.rommie;

import java.util.List;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;


public class ScheduleDetail extends ExhibitorsViewFragment{

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    /**
     * Componentes visuales para mostrar los detalles de un evento
     */
    private TextView textAbstract;
    private TextView textLocation;
    private RecyclerView exhibitorsView;

    private View iconMap;
    private TextView textHeader;
    private TextView textEventype;
    private ImageView emphasisView;
    private FloatingActionLayout favoriteButton;

    private ImageView buttonBack;


    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleDetail newInstance(ScheduleEvent event) {
        ScheduleDetail fragment = new ScheduleDetail();
        fragment.event = event;
        return fragment;
    }

    /**
     * Obtiene todos los expositores que maneja la vista
     */
    @Override
    public List<Exhibitor> getExhibitors() {
        return event.getExhibitors();
    }

    /**
     * Se crea la vista que con los detalles del evento
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.schedule_detail, container, false);

        textAbstract = view.findViewById(R.id.text_abstract);
        textLocation = view.findViewById(R.id.text_location);
        textHeader = view.findViewById(R.id.schedule_detail_header);
        textEventype =  view.findViewById(R.id.schedule_detail_eventype);

        emphasisView = view.findViewById(R.id.schedule_detail_top);
        favoriteButton = view.findViewById(R.id.favorite_button);
        exhibitorsView = view.findViewById(R.id.exhibitors_view);

        iconMap = view.findViewById(R.id.ic_map);
        buttonBack = view.findViewById(R.id.button_back);

        return view;

    }

    /**
     * Justo después de crear la vista se deben cargar los detalles del evento
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        setToolbarVisible(false);
        bindInformation();
        setupExhibitorsView();

    }

    /**
     * Al cambiar a otra sección se deben volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        setToolbarVisible(true);

    }

    /**
     * Coloca la información del evento en cada uno de los componentes
     */
    private void bindInformation(){

        textAbstract.setText(event.getBrief(getActivity()));
        textLocation.setText(event.getLocation());
        textHeader.setText(event.getTitle());
        textEventype.setText(event.getEventype().toString());

        Drawable drawable = getResources().getDrawable(event.getEventype().getResource());
        emphasisView.setImageDrawable(drawable);

        iconMap.setOnClickListener(v -> switchFragment(new InformationMap()));
        buttonBack.setOnClickListener(v -> getNavigation().onBackPressed());

        favoriteButton.setOnClickListener(v -> {
            List<ScheduleEvent> events = FavoriteList.getInstance().getSortedEvents();
            if(!events.contains(event)) events.add(event);
            String msg = getResources().getString(R.string.text_marked_as_favorite);
            showStatusMessage(msg);
        });

    }

    /**
     * Se configura el exhibitorsView que contiene los expositores
     */
    public void setupExhibitorsView(){

        if (exhibitorsAdapter == null)
            exhibitorsAdapter = new ExhibitorsViewAdapter(this);

        exhibitorsView.setHasFixedSize(true);
        exhibitorsView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsView.setAdapter(exhibitorsAdapter);

    }

}