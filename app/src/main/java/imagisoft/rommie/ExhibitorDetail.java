package imagisoft.rommie;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;


public class ExhibitorDetail extends MainActivityFragment {

    /**
     * Componentes visuales
     */
    @BindView((R.id.button_back))
    ImageView buttonBack;

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
     * Atributos importantes
     */
    private Exhibitor exhibitor;
    private List<ScheduleBlock> events;
    private ScheduleViewAdapter adapter;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ExhibitorDetail newInstance(Exhibitor exhibitor, List<ScheduleBlock> events) {
        ExhibitorDetail fragment = new ExhibitorDetail();
        fragment.events = events;
        fragment.exhibitor = exhibitor;
        return fragment;
    }

    /**
     * Se crea la vista con los eventos del expositor
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.exhibitor_detail);
    }

    /**
     * Justo después de crear la vista se deben cargar los detalles del evento
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        setToolbarVisibility(View.GONE);

        if(adapter == null)
            adapter = new ScheduleViewAdapter(this, events);

        bindInformation();
        setupEventsView();

    }

    /**
     * Al cambiar a otra sección se debe volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setToolbarVisibility(View.VISIBLE);
    }

    /**
     * Coloca la información del expositor
     */
    private void bindInformation(){
        String name = exhibitor.getCompleteName();
        this.nameTextView.setText(name);
        titleTextView.setText(exhibitor.getPersonalTitle());
        exhibitorAvatarView.setImageDrawable(new AvatarPlaceholder(name, 30));
        buttonBack.setOnClickListener(v -> getNavigation().onBackPressed());
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