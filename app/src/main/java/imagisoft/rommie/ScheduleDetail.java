package imagisoft.rommie;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.ArrayList;
import java.util.List;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;

public class ScheduleDetail extends ExhibitorsViewFragment{

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    /**
     * Componentes visuales
     */
    private TextView textAbstract;
    private TextView textLocation;
    private RecyclerView exhibitorsView;

    private View icMap;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.schedule_detail, container, false);
    }

    /**
     * Justo después de crear la vista se deben cargar los detalles del evento
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        ActionBar toolbar = getNavigation().getSupportActionBar();
        if(toolbar != null) toolbar.hide();

        bindViews();
        bindInformation();
        setupExhibitorsView();
    }

    /**
     * Al cambiar a otra sección se deben volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActionBar toolbar = getNavigation().getSupportActionBar();
        if(toolbar != null) toolbar.show();
    }

    /**
     * Se enlaza cada uno de los componentes visuales
     */
    private void bindViews() {

        assert getView() != null;

        textAbstract = getView().findViewById(R.id.text_abstract);
        textLocation = getView().findViewById(R.id.text_location);
        textHeader = getView().findViewById(R.id.schedule_detail_header);
        textEventype =  getView().findViewById(R.id.schedule_detail_eventype);

        emphasisView = getView().findViewById(R.id.schedule_detail_top);
        favoriteButton = getView().findViewById(R.id.favorite_button);
        exhibitorsView = getView().findViewById(R.id.exhibitors_view);

        icMap = getView().findViewById(R.id.ic_map);
        buttonBack = getView().findViewById(R.id.button_back);
        exhibitorsAdapter = new ExhibitorsViewAdapter(this);

    }

    /**
     * Coloca la información del evento en cada uno de los componentes
     * TODO: Colocar el abstract según el idioma
     */
    private void bindInformation(){

        textAbstract.setText(event.getBriefSpanish());
        textLocation.setText(event.getLocation());
        textHeader.setText(event.getTitle());
        textEventype.setText(event.getEventype().toString());

        Drawable drawable = getResources().getDrawable(event.getEventype().getResource());
        emphasisView.setImageDrawable(drawable);

        icMap.setOnClickListener(v -> switchFragment(new InformationMap()));
        buttonBack.setOnClickListener(v -> getActivity().onBackPressed());

    }

    /**
     * Se configura el exhibitorsView que contiene los expositores
     */
    public void setupExhibitorsView(){
        exhibitorsView.setHasFixedSize(true);
        exhibitorsView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsView.setAdapter(exhibitorsAdapter);
    }

}