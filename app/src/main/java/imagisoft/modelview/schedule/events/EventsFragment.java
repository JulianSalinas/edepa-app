package imagisoft.modelview.schedule.events;

import butterknife.BindView;

import android.os.Bundle;

import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;
import imagisoft.modelview.interfaces.IPageListener;
import imagisoft.modelview.interfaces.IPageSubject;
import imagisoft.modelview.views.SmoothLayout;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.widget.TextView;

import com.google.firebase.database.Query;


public abstract class EventsFragment
        extends MainFragment implements IPageSubject {

    /**
     * Textview que se coloca cuando no hay eventos
     */
    @BindView(R.id.events_empty_view)
    TextView eventsEmptyView;

    /**
     * Se colocan los eventos de manera visual
     * los eventos los obtiene de {@link #eventsAdapter}
     */
    @BindView(R.id.events_recycler_view)
    RecyclerView eventsRecyclerView;

    /**
     * Solo eventos de esta fecha deben estar en
     * este fragmento. Si no existe la fecha; se colocan
     * todos los eventos sin importar el día
     */
    protected long date;

    @Override
    public long getDate(){
        return date;
    }

    /**
     * Adaptador para {@link #eventsRecyclerView}
     */
    protected EventsAdapter eventsAdapter;

    public EventsAdapter getEventsAdapter() {
        return eventsAdapter;
    }

    /**
     * El {@link IPageListener} se da cuenta de los
     * cambios que son hechos en el fragmento
     */
    protected IPageListener pageListener;

    @Override
    public IPageListener getPageListener(){
        return this.pageListener;
    }

    @Override
    public void setPageListener(IPageListener pageListener){
        this.pageListener = pageListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.events_fragment;
    }

    /**
     * {@inheritDoc}
     * Se obtienen la fecha que es pasada como argumento
     * y se instancia el adaptador si es necesario
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se obtiene la fecha que deben de tener los
        // eventos presentes en el fragmento
        Bundle args = getArguments();
        if (args != null && args.containsKey("date"))
            date = args.getLong("date");

        if(eventsAdapter == null) eventsAdapter = instantiateAdapter();
    }

    /**
     * {@inheritDoc}
     * Se configura el {@link #eventsRecyclerView}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(eventsRecyclerView.getAdapter() == null) {

            // Se coloca el adaptador
            eventsRecyclerView.setHasFixedSize(true);
            eventsRecyclerView.setAdapter(eventsAdapter);

            // Agrega las animaciones para la inserción
            // deleción y modificación
            eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());

            // Agregar un LinearLayout que contiene una
            // transición más suave al hacer scroll
            eventsRecyclerView.setLayoutManager(new SmoothLayout(activity));
        }

    }

    protected abstract Query getFavoritesQuery();

    protected abstract Query getScheduleQuery();

    /**
     * Evita a las subclases tener que hacer la revisión
     * de nullpointer en el método {@link #onCreate(Bundle)}
     */
    protected abstract EventsAdapter instantiateAdapter();

}