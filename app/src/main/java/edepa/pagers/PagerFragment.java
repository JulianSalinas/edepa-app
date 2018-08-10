package edepa.pagers;

import butterknife.BindView;
import edepa.model.Cloud;
import edepa.modelview.R;
import edepa.misc.DateConverter;
import edepa.model.ScheduleEvent;
import edepa.interfaces.IPageListener;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public abstract class PagerFragment
        extends PagerFirebase implements IPageListener {

    /**
     * Constante usada para recuperar el último
     * elemento que estaba en pantalla
     */
    private final String CURRENT_ITEM = "CURRENT_ITEM";

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
    TextView eventsEmptyView;

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
    private long eventsAmount;

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
    private PagerAdapter adapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.events_pager;
    }

    /**
     * Se mueve hacia página es especifico
     * @param pageIndex: Número de página a mostrar
     */
    public void setCurrentPage(int pageIndex){
        pager.setCurrentItem(pageIndex);
    }

    /**
     * Obtiene una referecnia hacia todos los eventos de
     * la base de datos, para que cuando se agregue uno nuevo
     * en un fecha todavía no registrada, se agregue la página nueva
     * @return Query
     */
    public Query getEventsQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .orderByChild("start");
    }

    /**
     * {@inheritDoc}
     * En este método se instancia el adaptador
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se instancia el adaptador para contener los eventos
        if (adapter == null) {
            dates = new ArrayList<>();
            adapter = instantiateAdapter();
        }

        // Se agrega un listener para obtener de antemano la
        // cantidad de eventos para así poder saber cuando
        // termina la carga inicial
        getEventsQuery()
        .addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            eventsAmount = dataSnapshot.getChildrenCount();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(toString(), databaseError.getMessage());
        }});

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
        // pager.setOffscreenPageLimit(2);
        updateInterface();
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
     * Es utilizada por {@link #addPageIfNotExists(ScheduleEvent)}
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

        // Se coloca la página donde esta el evento
        // ingresado
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
    public void addPageIfNotExists(ScheduleEvent event){
        long date = DateConverter.atStartOfDay(event.getStart());
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
        int index = 0;
        for (Long aDate : dates) {
            if (aDate < date) index += 1;
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
    protected abstract PagerAdapter instantiateAdapter();

}
