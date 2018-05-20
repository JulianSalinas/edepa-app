package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import java.util.List;
import butterknife.BindView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;


public class ScheduleDetailBrief extends ExhibitorsView{

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    /**
     * Componentes visuales para mostrar los detalles de un evento
     */
    @BindView(R.id.brief)
    TextView briefTextView;

    @BindView(R.id.icon_map)
    View iconMap;

    @BindView(R.id.exhibitor_recycler_view)
    RecyclerView exhibitorsRecyclerView;


    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleDetailBrief newInstance(ScheduleEvent event) {

        ScheduleDetailBrief fragment = new ScheduleDetailBrief();

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
        resource = R.layout.schedule_detail_focus;

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
        setupExhibitorsView();
    }


    /**
     * Coloca la información del evento en cada uno de los componentes
     */
    private void bindInformation(){

        briefTextView.setText(event.getBrief("es"));
        Linkify.addLinks(briefTextView, Linkify.WEB_URLS);
        iconMap.setOnClickListener(v -> switchFragment(new InformationMap()));

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