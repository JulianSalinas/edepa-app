package imagisoft.modelview.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.arch.lifecycle.LifecycleObserver;

//import butterknife.ButterKnife;

import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.ButterKnife;
import imagisoft.model.Preferences;


public abstract class MainFragment
        extends Fragment implements LifecycleObserver{

    /**
     * Entero que representa el layout que está utilizando el
     * fragmento
     */
    protected int resource;

    /**
     * Para obtener un referencia a la actividad y no hacer
     * cast en cada momento que se requiera
     */
    protected MainActivity activity;

    /**
     * Cuando se cambian los fragmentos es necesario conservar
     * el color que tenía la barra de notificaciones
     */
    private int lastStatusBarColor;

    private boolean activeTabbedMode;

    private boolean lastActiveTabbedMode;

    /**
     * Cuando se cambian los fragmentos es necesario conservar
     * la visibilidad que tenia anteriormente, con el fin de restaurar
     * al presionar atrás
     */
    private int lastToolbarVisibility;

    /**
     * Cuando se cambian los fragmentos es necesario conservar el
     * nombre que estaba en la barra de herramientas para poder
     * colocarlo al presionar atrás
     */
    private CharSequence lastUsedToolbarText;

    /**
     * Variable para que todos los fragmentos tengan a mano las
     * preferencias compartidas
     */
    protected final Preferences prefs = Preferences.getInstance();

    /**
     * Proxy para acceder a la base de datos
     */
    protected FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Función para que los fragmentos puedan agregar los
     * listeners de firebase
     * @return MainNavigation
     */
    public MainNavigation getMainActivity(){
        return (MainNavigation) activity;
    }

    /**
     * Permite obtener la toolbar a las subclases, esto para que puedan
     * cambiar el nombre
     * @return Toolbar global de la aplicación
     */
    public Toolbar getToolbar(){
        return activity.getToolbar();
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
     * Permite que cada sección o fragmento muestre un nombre
     * diferente cuando se coloque
     * @param resource: R.string.<resource>
     */
    public void setToolbarText(int resource){
        activity.getToolbar().setTitle(resource);
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

    public boolean isActiveTabbedMode(){
        return activeTabbedMode;
    }

    public void setActiveTabbedMode(boolean active){
        activeTabbedMode = active;
    }

    /**
     * Permiten colocar el color de la barra superior
     * donde se muestran las notificaciones
     */
    public void setStatusBarColorRes(int resource){
        int color = activity.getResources().getColor(resource);
        activity.getWindow().setStatusBarColor(color);
    }

    public void setStatusBarColor(int color){
        activity.getWindow().setStatusBarColor(color);
    }

    /**
     * Permiten obtener el color de la barra superior
     * donde se muestran las notificaciones
     */
    public int getStatusBarColor(){
        return activity.getWindow().getStatusBarColor();
    }

    /**
     * Las subclases deben sobreescribirse y colocar el
     * atributo resource aquí
     * @param savedInstanceState: Contiene los argumentos
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setupResource();
        getLifecycle().addObserver(this);
    }

    /**
     * Obliga a las subclases a colocar el atributo resource
     */
    public abstract void setupResource();

    /**
     * Todas las subclases usan el mismo método, lo único que cambia
     * es el resource, por tanto se implementa aquí
     * @return Vista del fragmento
     */
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(resource, container, false);
        ButterKnife.bind(this, view);
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
        saveState();
        setupActivityView();
        setupTabbedMode();
    }

    /**
     * Guarda aspectos visuales con el fin de poder restaurarlos
     * al presionar el botón de atrás
     */
    private void saveState(){
        lastActiveTabbedMode = isActiveTabbedMode();
        lastUsedToolbarText = getToolbar().getTitle();
        lastToolbarVisibility = getToolbar().getVisibility();
        lastStatusBarColor = getStatusBarColor();
    }

    /**
     * Permite a las subclases configurar que partes
     * de la actividad deben estar visibles, usando
     * setToolbarVisibility...
     */
    public abstract void setupActivityView();

    public void setupTabbedMode(){
        Log.i("active: ", String.valueOf(activeTabbedMode));
        Log.i("last_active: ", String.valueOf(lastActiveTabbedMode));

        if(activeTabbedMode && !lastActiveTabbedMode)
            activity.activeTabbedMode();
        if (!activeTabbedMode)
            activity.deactiveTabbedMode();
    }

    /**
     * Al quitarse este fragmento la barra de tareas
     * se deben colocar a como estaban antes
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        restoreState();
    }

    /**
     * Retorna los aspectos visuales a como estaban antes de
     * cambiar el fragmento
     */
    public void restoreState(){

        getToolbar().setTitle(lastUsedToolbarText);
        getToolbar().setVisibility(lastToolbarVisibility);

        getSearchView().setVisibility(lastToolbarVisibility);
        activity.getWindow().setStatusBarColor(lastStatusBarColor);

        if(activeTabbedMode && !lastActiveTabbedMode)
            activity.deactiveTabbedMode();

        else if (!activeTabbedMode && lastActiveTabbedMode)
            activity.activeTabbedMode();

        activeTabbedMode = lastActiveTabbedMode;
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void setFragmentOnScreen(Fragment fragment){
        activity.setFragmentOnScreen(fragment, "TAG");
    }

    /**
     * Reinicia la aplicación. Se utiliza cuando se cambia el idioma
     */
    public void restartApplication(){
        // activity.restartApplication();
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        activity.showMessage(msg);
    }

    /**
     * Realiza lo mismo que showMessage(String msg) solo
     * que esta utiliza un recurso de R.string
     * @param resource: R.string.<resource>
     */
    public void showStatusMessage(int resource){
        activity.showMessage(getResources().getString(resource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
