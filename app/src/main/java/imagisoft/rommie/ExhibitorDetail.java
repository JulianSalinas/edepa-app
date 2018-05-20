package imagisoft.rommie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarview.AvatarPlaceholder;

import java.util.List;
import java.util.ArrayList;
import butterknife.BindView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;


public class ExhibitorDetail extends EventsView {

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
        bindInformation();
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

    @Override
    protected void setupAdapter() {
        if(eventsViewAdapter == null)
            eventsViewAdapter = new EventsViewAdapterExhibitor(this);
    }

    public void setupEventsView(){
        eventsView.setHasFixedSize(true);
        eventsView.setAdapter(eventsViewAdapter);
        eventsView.setItemAnimator(new DefaultItemAnimator());
        eventsView.setLayoutManager(new SmoothLayout(activity));
    }

}