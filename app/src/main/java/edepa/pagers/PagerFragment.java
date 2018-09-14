package edepa.pagers;

import butterknife.BindView;

import edepa.custom.CustomFragment;
import edepa.cloud.CloudEvents;
import edepa.schedule.IPageListener;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.schedule.ScheduleFragment;
import edepa.minilibs.TimeConverter;

import java.util.List;
import java.util.ArrayList;

import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public abstract class PagerFragment extends CustomFragment
        implements IPageListener, CloudEvents.Callbacks {

    /**
     * Constante usada para recuperar el último
     * elemento que estaba en pantalla
     */
    public static final String CURRENT_ITEM = "CURRENT_ITEM";

    /**
     * Espacio donde las páginas se cambian
     * para mostrar los eventos de otros días
     */
    @BindView(R.id.view_pager)
    ViewPager pager;

    /**
     * Textview que se coloca cuando no hay eventos
     */
    @BindView(R.id.events_empty_view)
    View eventsEmptyView;

    /**
     * Para guardar en que página quedo antes
     * de girar la pantalla
     */
    Bundle savedState;

    /**
     * Cantidad de eventos para controlar cuando
     * ya se ha terminado de realizar la carga
     * inicial de los datos
     */
    protected long eventsAmount;

    /**
     * Para dividir los eventos por día
     * Cada fecha representa una página
     * Es importante agregarlas en orden
     */
    protected List<Long> dates;

    /**
     * Contiene los fragmentos del páginador
     * los administra
     */
    protected FragmentPagerAdapter adapter;

    /**
     * Utilizado para obtener las fechas de los
     * eventos y poder crear las páginas
     */
    protected CloudEvents cloudEvents;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.schedule_pager;
    }

    /**
     * Se mueve hacia página es especifico
     * @param pageIndex: Número de página a mostrar
     */
    public void setCurrentPage(int pageIndex){
        pager.setCurrentItem(pageIndex);
    }

    /**
     * Coloca una vista vacía en {@link #eventsEmptyView}
     * Esta puede hacerce visible en {@link #updateInterface()}
     */
    protected abstract void inflateEmptyView();

    /**
     * {@inheritDoc}
     * En este método se instancia el adaptador
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se instancia el adaptador para contener los eventos
        dates = new ArrayList<>();
        adapter = instantiateAdapter();
        cloudEvents = new CloudEvents();
        cloudEvents.setCallbacks(this);

        inflateEmptyView();

        // Se agrega un Listener para obtener de antemano la
        // cantidad de eventos para así poder saber cuando
        // termina la carga inicial
        CloudEvents.getEventsQueryByStart()
                .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventsAmount = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(toString(), databaseError.getMessage());
            }

        });

    }

    /**
     * {@inheritDoc}
     * Obtiene los eventos, extrae todos los días que componen
     * los eventos y ajusta la interfaz para solo mostrar dichos
     * días por medio de un adaptador.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager.setAdapter(adapter);
        cloudEvents.connectByDate();
        updateInterface();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cloudEvents.disconnectByDate();
    }

    public void updateInterface(){
        boolean isEmpty = dates.size() == 0;
        pager.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        eventsEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(pager != null)
            outState.putInt(CURRENT_ITEM, pager.getCurrentItem());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) savedState = savedInstanceState;
    }

    /**
     * Remueve una página
     * Se utiliza cuando la vista de eventos se queda vacía
     * @param date Fecha de la página por remover
     * @see #addPage(long)
     */
    @Override
    public void onPageRemoved(long date){
        dates.remove(date);
        adapter.notifyDataSetChanged();
        updateInterface();
    }

    /**
     * Cuando un evento cambia de fecha se invoca esta
     * función que mueve el paginador hacia el evento ingresado
     * @param pageDate: Fecha de la página donde se agregó
     *                  el nuevo evento (o con fecha cambiada)
     */
    @Override
    public void onPageChanged(long pageDate) {
        int index = dates.indexOf(pageDate);
        if (index != -1) setCurrentPage(index);
        updateInterface();
    }

    /**
     * Agrega una página
     * Es utilizada por {@link #addPageIfNotExists(Event)}
     * @param date Fecha de los eventos en la página
     * @see #onPageRemoved(long)
     */
    private void addPage(long date){

        int index = findIndexToAddPage(date);
        dates.add(index, date);
        adapter.notifyDataSetChanged();

        // Solución para restaurar última página puesta
        if (savedState != null){
            int curretItem = savedState.getInt(CURRENT_ITEM);
            pager.setCurrentItem(curretItem);
        }

        // Se coloca la página donde esta el evento ingresado
        else if (eventsAmount <= 0)
            pager.setCurrentItem(index);

        updateInterface();

    }

    /**
     * Agrega una nueva página en caso de que no exista
     * Es utilizada cada vez que se agrega o mueve un evento
     * @param event: Donde se extrae la fecha para colocar en la página
     * @see #addPage(long)
     */
    public void addPageIfNotExists(Event event){
        long date = TimeConverter.atStartOfDay(event.getDate());
        if (!dates.contains(date)) {
            addPage(date);
            eventsAmount--;
        }
    }

    /**
     * Encuentra en que posición debe insertarse una fecha
     * @param date Fecha de la páginaw
     * @return índice donde se debe insertar la página
     * @see #addPage(long)
     */
    private int findIndexToAddPage(long date){
        if(dates.size() == 0) return 0;
        int index = 0;
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i) <= date) index += 1;
            else break;
        }   return index;
    }

    /**
     * Es utilizada por la función {@link #onActivityCreated(Bundle)}
     * Cada uno de los fragmentos, el de cronograma y el de
     * favoritos son iguales pero necesitan manejar distintos
     * eventos por los que cada uno implementa este método
     * @return PagerAdaptador adaptor para el fragmento
     */
    protected abstract FragmentPagerAdapter instantiateAdapter();

}
