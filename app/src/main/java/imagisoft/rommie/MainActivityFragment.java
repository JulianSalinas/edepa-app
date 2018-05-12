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
     * Para obtener un referencia a la actividad ya con el cast
     */
    protected MainActivityNavigation activity;

    /**
     * Cuando se cambian los fragmentos es necesario conservar el
     * nombre que estaba en la barra de herramientas para poder
     * colocarlo al presionar atrás
     */
    protected CharSequence lastUsedToolbarText;

    /**
     * Al igual que el anterior es necesario recordar si los tabs
     * estaban visibles anteriormente
     */
    protected int lastTabLayoutVisibility;

    /**
     * Utilizada exclusivamente para depuración en el Logcat
     */
    protected final String TAG = "MainActivityFragment";

    /**
     * Variable para que todos los fragmentos tengan a mano las
     * preferencias compartidas.
     */
    protected final Preferences prefs = Preferences.getInstance();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity = (MainActivityNavigation) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(resource, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        lastUsedToolbarText = getToolbar().getTitle();
        lastTabLayoutVisibility = getTabLayout().getVisibility();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView()");
        super.onDestroyView();
        getToolbar().setTitle(lastUsedToolbarText);
        getTabLayout().setVisibility(lastTabLayoutVisibility);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach()");
        super.onDetach();
    }

    /**
     * Obtiene el lenguaje de la configuracíon. Si
     * es la primera vez, guarda en la configuración el idioma del dispositivo
     * @return "es" o "en"
     */
    public String getCurrentLang(){

        String lang = prefs.getStringPreference(getActivity(), Preferences.LANG_KEY_VALUE);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            setCurrentLang(lang);
        }

        return lang;

    }

    public void setCurrentLang(String lang){
        prefs.setPreference(getActivity(), Preferences.LANG_KEY_VALUE, lang);
    }

    /**
     * Obtiene el actual nombre de usuario, si el la primera vez toma
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

    public void setCurrentUsername(String username){
        prefs.setPreference(getActivity(), Preferences.USER_KEY_VALUE, username);
    }

    /**
     * Obtiene el estado actual de la alarma
     */
    public Boolean getCurrentAlarmState(){
        return prefs.getBooleanPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE);
    }

    public void setCurrentAlarmState(boolean state){
        prefs.setPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE, state);
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

        assert user != null;
        return user.getDisplayName() == null ||
                user.getDisplayName().isEmpty() ?
                context.getResources().getString(R.string.text_anonymous) : user.getDisplayName();

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
     * Reinicia la aplicación. Se utiliza cuando se cambia el idioma o el tema
     */
    public void restartApplication(){
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.restartApplication();
    }

    public void setToolbarVisibility(int visibility){
        activity.getToolbarContainer().setVisibility(visibility);
        getToolbar().setVisibility(visibility);
        getSearchView().setVisibility(visibility);
    }

    public void setStatusBarColor(int color){
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.getWindow().setStatusBarColor(color);
    }

    public int getStatusBarColor(){
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        return activity.getWindow().getStatusBarColor();
    }

    public void setTabLayoutVisibility(int visibility){
        activity.getTabLayout().setVisibility(visibility);
    }

    public TabLayout getTabLayout(){
        return activity.getTabLayout();
    }

    public Toolbar getToolbar(){
        return activity.getToolbar();
    }

    public MaterialSearchView getSearchView(){
        return activity.getSearchView();
    }

    public void setToolbarText(int resource){
        activity.getToolbar().setTitle(resource);
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        assert getActivity() != null;
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.showStatusMessage(msg);
    }

}
