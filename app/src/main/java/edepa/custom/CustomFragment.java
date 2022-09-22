package edepa.custom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import edepa.app.MainActivity;
import edepa.app.NavigationActivity;


public abstract class CustomFragment
        extends BaseFragment implements LifecycleObserver{

    /**
     * Para obtener un referencia a la actividad y no
     * tener que hacer cast en cada momento que se requiera
     */
    protected MainActivity activity;

    /**
     * Cuando se cambian los fragmentos es necesario conservar
     * el accent que tenía la barra de notificaciones
     */
    protected int lastStatusBarColor;

    /**
     * Cuando se cambian los fragmentos es necesario conservar
     * la visibilidad que tenia anteriormente, con el fin de restaurar
     * al presionar atrás
     */
    protected int lastToolbarVisibility;

    /**
     * Cuando se cambian los fragmentos es necesario conservar el
     * nombre que estaba en la barra de herramientas para poder
     * colocarlo al presionar atrás
     */
    protected String lastToolbarTitle;

    /**
     * Permite obtener la toolbar a las subclases, esto para
     * que puedan cambiar el nombre
     * @return Toolbar global de la aplicación
     */
    public Toolbar getToolbar(){
        return activity.getToolbar();
    }

    /**
     * Función para que los fragmentos puedan agregar los
     * listeners de firebase
     * @return NavigationActivity
     */
    public NavigationActivity getNavigationActivity(){
        return (NavigationActivity) activity;
    }

    /**
     * Permite que cada sección o fragmento muestre un nombre
     * diferente cuando se coloque
     */
    public void setToolbarText(int resource){
        activity.getToolbar().setTitle(resource);
    }

    public void setToolbarText(String text){
        activity.getToolbar().setTitle(text);
    }

    /**
     * Permite obtener la barra de búsqueda que por defecto esta
     * oculta a menos que se utilice el onCreateOptionsMenu
     * @return MaterialSearchView de la aplicación
     */
    public MaterialSearchView getSearchView(){
        return activity.getSearchView();
    }

    /**
     * Oculta tanto la toolbar como la barra de búsqueda
     * @param visibility View.GONE o View.VISIBLE
     */
    public void setToolbarVisibility(int visibility){
        getToolbar().setVisibility(visibility);
        getSearchView().setVisibility(visibility);
        activity.getToolbarContainer().setVisibility(visibility);
    }

    /**
     * Permiten colocar el accent de la barra superior
     * donde se muestran las notificaciones
     */
    public int getStatusBarColor(){
        return activity.getWindow().getStatusBarColor();
    }

    public void setStatusBarColorRes(int resource){
        int color = activity.getResources().getColor(resource);
        activity.getWindow().setStatusBarColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        getLifecycle().addObserver(this);
    }

    /**
     * Todas las subclases usan el mismo método, lo único que cambia
     * es el resource, por tanto se implementa aquí
     * @return Vista del fragmento
     */
    @Override public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return view;
    }

    /**
     * Cuando se cambia el fragmento, se debe guardar el estado
     * anterior del toolbar. Las subclases pueden editar
     * estas vistas sin verse afectadas.
     * @param savedInstanceState: Argumentos guardados
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(toString(), "onActivityCreated()");
        lastStatusBarColor = getStatusBarColor();
        lastToolbarTitle = getToolbar().getTitle().toString();
        lastToolbarVisibility = getToolbar().getVisibility();
    }

    /**
     * Al quitarse este fragmento la barra de tareas
     * se debe colocar a como estaban antes
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(toString(), "onDestroyView()");
        setToolbarText(lastToolbarTitle);
        setToolbarVisibility(lastToolbarVisibility);
        activity.getWindow().setStatusBarColor(lastStatusBarColor);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void setFragmentOnScreen(Fragment fragment, String tag){
        activity.setFragmentOnScreen(fragment, tag);
    }

    /**
     * Realiza lo mismo que showMessage(String msg) solo
     * que esta utiliza un recurso de R.string
     * @param resource: R.string.<resource>
     */
    public void showStatusMessage(int resource){
        activity.showMessage(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
