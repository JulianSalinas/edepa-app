package imagisoft.rommie;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;


public class ExhibitorsFragment extends ActivityFragment
        implements MaterialSearchView.OnQueryTextListener {

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    @BindView(R.id.exhibitor_recycler_view)
    RecyclerView exhibitorsRecyclerView;

    /**
     * Tomada de la toolbar de la actividad
     */
    private MaterialSearchView searchView;

    /**
     * Lista de expositores obtenida de la lista de eventos
     */
    protected ExhibitorsAdapter exhibitorsAdapter;


    public void setupAdapter(){
        if (exhibitorsAdapter == null){
            exhibitorsAdapter = new ExhibitorsAdapter(this);
        }
    }

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void setupResource() {
        this.resource = R.layout.exhibitors_view;
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void setupActivityView() {
        setToolbarText(R.string.nav_people);
        setTabLayoutVisibility(View.GONE);
        setToolbarVisibility(View.VISIBLE);
        setupSearchView();
        setupAdapter();
        setupExhibitorsView();
    }

    /**
     * Activa la barra de búsqueda de la actividad
     * para poder buscar expositores expecificos
     */
    private void setupSearchView(){
        searchView = getSearchView();
        searchView.setHint(getResources().getString(R.string.text_search));
        searchView.setOnQueryTextListener(this);
        searchView.setVoiceSearch(true);
        searchView.setVoiceIcon(getResources().getDrawable(R.drawable.ic_voice));
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
    }

    /**
     * Cuando el fragmento se cambia se debe cerrar
     * la barra de búsquedad
     */
    @Override
    public void onPause() {
        super.onPause();
        searchView.closeSearch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchView.closeSearch();
    }

    /**
     * Se agrega el icono de la barra de búsqueda en la toolbar
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        searchView.setMenuItem(searchItem);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Se configura el exhibitorsRecyclerView que contiene los expositores
     */
    public void setupExhibitorsView(){

        exhibitorsRecyclerView.setHasFixedSize(true);
        exhibitorsRecyclerView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsRecyclerView.setAdapter(exhibitorsAdapter);

        // Efecto visual para dividir a los expositores
        exhibitorsRecyclerView.addItemDecoration(
                new DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL));

    }


    /**
     * Ejecutada al presionar el botón de búsqueda
     * @param query: Lo que está escrito en la barra de búsqueda
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        exhibitorsAdapter.filter(query);
        return true;
    }

    /**
     * Ejecutada cada vez que se ingresa una letra
     * @param newText: Lo que está escrito en la barra de búsqueda
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        exhibitorsAdapter.filter(newText);
        return true;
    }

}