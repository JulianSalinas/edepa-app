package imagisoft.rommie;

import java.util.List;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.views.AvatarView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleBlock;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.support.v7.app.ActionBar;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;


public class ExhibitorDetail extends MainViewFragment{

    /**
     * Componentes visuales
     */
    private TextView textName;
    private TextView textTitle;
    private ImageView buttonBack;
    private ImageView emphasisView;
    private RecyclerView eventsView;
    private AvatarView avatarView;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.exhibitor_detail, container, false);
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
        setupEventsView();
    }

    /**
     * Al cambiar a otra sección se debe volver a colocar la toolbar
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

        textName = getView().findViewById(R.id.exhibitor_detail_name);
        textTitle = getView().findViewById(R.id.exhibitor_detail_title);
        avatarView = getView().findViewById(R.id.exhibitor_detail_avatar);

        emphasisView = getView().findViewById(R.id.schedule_detail_top);
        eventsView = getView().findViewById(R.id.exhibitors_view);

        buttonBack = getView().findViewById(R.id.button_back);
        adapter = new ScheduleViewAdapter(this, events);

    }

    /**
     * Coloca la información del expositor
     */
    private void bindInformation(){

        String name = exhibitor.getCompleteName();

        textName.setText(name);
        textTitle.setText(exhibitor.getPersonalTitle());
        avatarView.setImageDrawable(new AvatarPlaceholder(name, 30));
        buttonBack.setOnClickListener(v -> getActivity().onBackPressed());

    }

    /**
     * Se configura el eventsView que contiene los eventos
     */
    public void setupEventsView(){
        eventsView.setHasFixedSize(true);
        eventsView.setLayoutManager(new SmoothLayout(getActivity()));
        eventsView.setItemAnimator(new DefaultItemAnimator());
        eventsView.setAdapter(adapter);
    }

}