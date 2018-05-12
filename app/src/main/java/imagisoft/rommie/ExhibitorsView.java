package imagisoft.rommie;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;


public class ExhibitorsView extends ExhibitorsViewFragment implements MaterialSearchView.OnQueryTextListener {


    MaterialSearchView searchView;

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    @BindView(R.id.exhibitor_recycler_view)
    RecyclerView exhibitorsRecyclerView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.exhibitors_view;
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupExhibitorsView();
        setHasOptionsMenu(true);
        setToolbarText(R.string.nav_people);
        setTabLayoutVisibility(View.GONE);
        searchView = getSearchView();
        searchView.setHint(getResources().getString(R.string.text_search));
        searchView.setOnQueryTextListener(this);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
    }

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
