package imagisoft.rommie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import imagisoft.edepa.ScheduleEvent;

public class ScheduleDetail extends MainViewFragment {

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

    private View emphasisView;
    private TextView textHeader;
    private TextView textEventype;
    private Button buttonMap;

    private ExhibitorsViewAdapter exhibitorsAdapter;

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
        exhibitorsView = getView().findViewById(R.id.exhibitors_view);
        emphasisView = getView().findViewById(R.id.schedule_detail_top);
        textHeader = getView().findViewById(R.id.schedule_detail_header);
        textEventype =  getView().findViewById(R.id.schedule_detail_eventype);
        buttonMap = getView().findViewById(R.id.button_map);
        exhibitorsAdapter = new ExhibitorsViewAdapter(event.getExhibitors());
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
        emphasisView.setBackgroundResource(event.getEventype().getColor());
        buttonMap.setOnClickListener(v -> switchFragment(new InformationMap()));
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