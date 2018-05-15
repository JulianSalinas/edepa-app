package imagisoft.rommie;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Locale;

import butterknife.ButterKnife;
import imagisoft.edepa.Preferences;


public abstract class MainActivityFragment extends Fragment {

    /**
     * Entero que representa el layout que está utilizando el
     * fragmento
     */
    protected int resource;

    /**
     * Al igual que el anterior es necesario recordar si los tabs
     * estaban visibles anteriormente
     */
    protected int lastTabLayoutVisibility;

    /**
     * Cuando se cambian los fragmentos es necesario conservar el
     * nombre que estaba en la barra de herramientas para poder
     * colocarlo al presionar atrás
     */
    protected CharSequence lastUsedToolbarText;

    /**
     * Para obtener un referencia a la actividad ya con el cast
     */
    protected MainActivityNavigation activity;

    /**
     * Variable para que todos los fragmentos tengan a mano las
     * preferencias compartidas.
     */
    protected final Preferences prefs = Preferences.getInstance();

    /**
     * Permite a las subclases utilizar los componentes de la actividad
     */
    public TabLayout getTabLayout(){
        return activity.getTabLayout();
    }

    public Toolbar getToolbar(){
        return activity.getToolbar();
    }

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
        activity.currentSection.setText(resource);
        activity.currentSection.setText(resource);
    }

    /**
     * Las subclases deben color el atributo resource aquí.
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity = (MainActivityNavigation) getActivity();
    }

    /**
     * Todas las subclases usan el mismo método, lo único que cambia
     * es el resource, por tanto se implementa aquí.
     * @return Vista del fragmento
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(resource, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * Cuando se cambia el fragmento, se debe recordar el estado
     * anterior del toolbar y del tablayout. Las subclases pueden editar
     * estas vistas sin verse afectadas.
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        lastUsedToolbarText = getToolbar().getTitle();
        lastTabLayoutVisibility = getTabLayout().getVisibility();
    }

    /**
     * Al quitarse este fragmento la barra de tareas y el tab layout
     * se debe colocar a como estaban antes
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.currentSection.setText(lastUsedToolbarText);
        getToolbar().setTitle(lastUsedToolbarText);
        getTabLayout().setVisibility(lastTabLayoutVisibility);
    }

    /**
     * Obtiene el lenguaje de la configuracíon. Si
     * es la primera vez, guarda en la configuración el idioma del dispositivo
     * @return "es" o "en"
     */
    public String getCurrentLang(){

        String lang = prefs.getStringPreference(activity, Preferences.LANG_KEY_VALUE);

        if(lang == null) {
            setCurrentLang(Locale.getDefault().getLanguage());
            return getCurrentLang();
        }

        return lang;
    }

    /**
     * Guarda el idioma actual en la configuración
     * @param lang "es" o "en"
     */
    public void setCurrentLang(String lang){
        prefs.setPreference(activity, Preferences.LANG_KEY_VALUE, lang);
    }

    /**
     * Obtiene el actual nombre de usuario, si es la primera vez, toma
     * el usuario desde el login por defecto
     */
    public String getCurrentUsername(){

        String username = prefs.getStringPreference(getActivity(), Preferences.USER_KEY_VALUE);

        if(username == null) {
            username = getDefaultUsername(getActivity());
            setCurrentUsername(username);
        }

        return username;

    }

    /**
     * Cuando el usuario hace login con el # de teléfono, su nombre está vacío por lo que
     * hay que crear uno. Al hacer login con Google o correo el nombre por defecto que se
     * coloca es el de dicha cuenta.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public String getDefaultUsername(Context context){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            return context.getResources().getString(R.string.text_anonymous);
        }

        return user.getDisplayName() == null ||
                user.getDisplayName().isEmpty() ?
                context.getResources().getString(R.string.text_anonymous) : user.getDisplayName();

    }

    /**
     * Permite cambiar el nombre de usuario para usar en el chat
     * @param username: Nombre de usuario del chat
     */
    public void setCurrentUsername(String username){
        prefs.setPreference(getActivity(), Preferences.USER_KEY_VALUE, username);
    }

    /**
     * Obtiene el estado actual de la alarma
     * @return True si las alarmas están permitidas
     */
    public Boolean getCurrentAlarmState(){
        return prefs.getBooleanPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE);
    }

    /**
     * Cambia el estado actual de las alarmas
     * @param state True si se desean recibir notificaciones
     */
    public void setCurrentAlarmState(boolean state){
        prefs.setPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE, state);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){
        assert getActivity() != null;
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.switchFragment(fragment);
    }

    /**
     * Reinicia la aplicación. Se utiliza cuando se cambia el idioma
     */
    public void restartApplication(){
        activity.restartApplication();
    }

    /**
     * Oculta tanto la toolbar como la barra de búsqueda que la
     * acompaña
     * @param visibility View.GONE o View.VISIBLE
     */
    public void setToolbarVisibility(int visibility){
        activity.getToolbarContainer().setVisibility(visibility);
        getToolbar().setVisibility(visibility);
        getSearchView().setVisibility(visibility);
    }

    /**
     * Permite a los fragmentos colocar los tabs de cronograma y
     * favoritos en la toolbar
     * @param visibility View.GONE o View.VISIBLE
     */
    public void setTabLayoutVisibility(int visibility){
        activity.getTabLayout().setVisibility(visibility);
    }

    /**
     * Permiten ver y colocar el color de la barra superior
     * donde se muestran las notificaciones
     */
    public void setStatusBarColor(int color){
        activity.getWindow().setStatusBarColor(color);
    }

    public int getStatusBarColor(){
        return activity.getWindow().getStatusBarColor();
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        activity.showStatusMessage(msg);
    }

    /**
     * Realiza lo mismo que showStatusMessage(String msg) solo
     * que esta utiliza un recurso de R.string
     * @param resource: R.string.<resource>
     */
    public void showStatusMessage(int resource){
        activity.showStatusMessage(getResources().getString(resource));
    }

}
