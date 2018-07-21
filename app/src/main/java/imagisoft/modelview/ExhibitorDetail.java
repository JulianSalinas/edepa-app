package imagisoft.modelview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.DefaultItemAnimator;

import java.util.List;
import java.util.ArrayList;
import butterknife.BindView;
import com.mklimek.circleinitialsview.CircleInitialsView;

import imagisoft.misc.MaterialGenerator;
import imagisoft.model.Exhibitor;
import imagisoft.model.ScheduleEvent;


public class ExhibitorDetail extends EventsFragment {

    /**
     * Componentes visuales
     */
    @BindView((R.id.button_back))
    View buttonBack;

    @BindView(R.id.name_text_view)
    TextView nameTextView;

    @BindView(R.id.title_text_view)
    TextView titleTextView;

    @BindView(R.id.emphasis_image_view)
    ImageView emphasisImageView;

    @BindView(R.id.exhibitor_avatar_view)
    CircleInitialsView exhibitorAvatarView;

    private MaterialGenerator materialGenerator;

    /**
     * Expositor que se muestra junto con los eventos
     * relacionados
     */
    private Exhibitor exhibitor;
    private ArrayList<ScheduleEvent> events;

    public Exhibitor getExhibitor() {
        return exhibitor;
    }

    public ArrayList<ScheduleEvent> getEvents() {
        return events;
    }

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     * @param exhibitor: Expositor de que se deben mostrar los detalles
     */
    public static ExhibitorDetail newInstance(Exhibitor exhibitor, List<ScheduleEvent> events) {
        ExhibitorDetail fragment = new ExhibitorDetail();
        Bundle args = new Bundle();
        args.putParcelable("exhibitor", exhibitor);
        args.putParcelableArrayList("events", new ArrayList<>(events));
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Se define cúal es el layout que va a utilizar
     * @param savedInstanceState: No se utiliza
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null){
            exhibitor = args.getParcelable("exhibitor");
            events = args.getParcelableArrayList("events");
        }

        materialGenerator = new MaterialGenerator(activity);

    }

    @Override
    public void setupResource() {
        this.resource = R.layout.exhibitor_detail;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     */

    @Override
    public void setupActivityView() {
        super.setupActivityView();
        setToolbarVisibility(View.GONE);
        bindInformation();
    }

    /**
     * Coloca la información del expositor
     */
    private void bindInformation(){
        String name = exhibitor.getCompleteName();
        nameTextView.setText(name);
        titleTextView.setText(exhibitor.getPersonalTitle());
        exhibitorAvatarView.setText(name);
        exhibitorAvatarView.setTextColor(Color.WHITE);

        exhibitorAvatarView
                .setBackgroundColor(materialGenerator.getColor(name));

        buttonBack.setOnClickListener(v -> activity.onBackPressed());
    }

    @Override
    protected void setupAdapter() {
        if(eventsVA == null)
            eventsVA = new EventsAdapterExhibitor(this);
    }

    public void setupEventsView(){
        eventsRV.setHasFixedSize(true);
        eventsRV.setAdapter(eventsVA);
        eventsRV.setItemAnimator(new DefaultItemAnimator());
        eventsRV.setLayoutManager(new SmoothLayout(activity));
    }

}