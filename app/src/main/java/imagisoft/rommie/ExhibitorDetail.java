package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarview.AvatarPlaceholder;

import java.util.List;
import java.util.ArrayList;
import butterknife.BindView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;


public class ExhibitorDetail extends MainActivityFragment {

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
    AvatarView exhibitorAvatarView;

    @BindView(R.id.exhibitor_recycler_view)
    RecyclerView exhibitorsRecyclerView;

    /**
     * Expositor que se muestra junto con los eventos
     * relacionados
     */
    private Exhibitor exhibitor;
    private List<ScheduleBlock> events;

    /**
     * Adaptador para colocar los eventos relacionados
     */
    private ScheduleViewAdapter adapter;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     * @param exhibitor: Expositor de que se deben mostrar los detalles
     * @param events: Eventos en los que participa el expositor
     */
    public static ExhibitorDetail newInstance(Exhibitor exhibitor, List<ScheduleBlock> events) {

        ExhibitorDetail fragment = new ExhibitorDetail();

        Bundle args = new Bundle();
        args.putParcelable("exhibitor", exhibitor);
        args.putParcelableArrayList("events", new ArrayList<>(events));

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
        this.resource = R.layout.exhibitor_detail;

        Bundle args = getArguments();

        if (args != null){
            exhibitor = args.getParcelable("exhibitor");
            events = args.getParcelableArrayList("events");
        }

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

        if(adapter == null)
            adapter = new ScheduleViewAdapter(this, events);

        bindInformation();
        setupEventsView();

    }

    /**
     * Coloca la información del expositor
     */
    private void bindInformation(){
        String name = exhibitor.getCompleteName();
        nameTextView.setText(name);
        titleTextView.setText(exhibitor.getPersonalTitle());
        exhibitorAvatarView.setImageDrawable(new AvatarPlaceholder(name, 30));
        buttonBack.setOnClickListener(v -> activity.onBackPressed());
    }

    /**
     * Se configura el exhibitorsRecyclerView que contiene los eventos
     */
    public void setupEventsView(){
        exhibitorsRecyclerView.setHasFixedSize(true);
        exhibitorsRecyclerView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsRecyclerView.setAdapter(adapter);
    }

}